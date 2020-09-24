package com.kejunyao.arch.net;

import android.text.TextUtils;
import com.kejunyao.arch.net.agent.CookieAgent;
import com.kejunyao.arch.net.agent.HeaderAgent;
import com.kejunyao.arch.net.agent.NetworkAgent;
import com.kejunyao.arch.net.agent.ParameterAgent;
import com.kejunyao.arch.net.header.Header;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Connection {

    private static boolean DEBUG = false;
    private static final String TAG = "Arc-HttpConnection";

    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CONTENT_TYPE   = "Content-Type";
    private static final String SET_COOKIE     = "Set-Cookie";
    private static final String UTF_8          = "UTF-8";
    private static final String OCTET_STREAM   = "application/octet-stream";
    private static final int BUFFER_SIZE       = 1024;

    protected static final String HTTP_PROTOCOL  = "http";
    protected static final String HTTPS_PROTOCOL = "https";
    private static final int CONNECT_TIMEOUT     = 10000;
    private static final int WIFI_READ_TIMEOUT   = 10000;
    private static final int GPRS_READ_TIMEOUT   = 30000;

    private static final String REQ_METHOD_GET   = "GET";
    private static final String REQ_METHOD_POST  = "POST";

    public enum NetworkError {
        OK,
        URL_ERROR,
        NETWORK_ERROR,
        AUTH_ERROR,     // 在该链接需要登录但是没有的情况下返回
        SERVER_ERROR,
        RESULT_ERROR,
        UNKNOWN_ERROR
    }

    protected JSONObject mResponse;
    private byte[] responseBytes;
    protected Map<String, List<String>> mResponseHeader;
    protected int mResponseCode;
    protected URL mUrl;
    protected Parameter mParameter;
    protected String mString;

    protected boolean mUseGet;
    protected boolean mNeedCustomHeader;

    private byte[] mPostData;
    private String mContentType;

    public Connection(String baseUrlString, String appendUrlString) {
        this(connect(baseUrlString, appendUrlString));
    }

    public Connection(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e(e, "URL error: ");
        }
        init(url);
    }

    public static String connect(String baseUrlString, String appendUrlString) {
        if (TextUtils.isEmpty(baseUrlString)) {
            return appendUrlString;
        }
        if (TextUtils.isEmpty(appendUrlString)) {
            return baseUrlString;
        }
        if (baseUrlString.charAt(baseUrlString.length() - 1) == '/') {
            baseUrlString = baseUrlString.substring(0, baseUrlString.length() - 1);
        }
        if (appendUrlString.charAt(0) == '/') {
            appendUrlString = appendUrlString.substring(1);
        }
        return contact(baseUrlString, "/", appendUrlString);
    }

    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }

    private void init(URL url) {
        mUseGet = false;
        mNeedCustomHeader = true;
        if (checkURL(url)) {
            mUrl = url;
        }
    }

    private HeaderAgent mHeaderAgent;
    public void setHeaderAgent(HeaderAgent agent) {
        mHeaderAgent = agent;
    }

    private NetworkAgent mNetworkAgent;
    public void setNetworkAgent(NetworkAgent agent) {
        mNetworkAgent = agent;
    }

    private CookieAgent mCookieAgent;
    public void setCookieAgent(CookieAgent agent) {
        mCookieAgent = agent;
    }

    private ParameterAgent mParameterAgent;
    public void setParameterAgent(ParameterAgent agent) {
        mParameterAgent = agent;
    }

    public JSONObject getResponse() {
        return mResponse;
    }

    public byte[] getResponseBytes() {
        return responseBytes;
    }

    public Map<String, List<String>> getResponseHeader() {
        return mResponseHeader;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public String getStringResponse() {
        return mString;
    }

    public Parameter getParameter() {
        return mParameter;
    }

    public Connection addParameter(String name, Object value) {
        if (mParameter == null) {
            mParameter = new Parameter();
        }
        mParameter.add(name, value);
        return this;
    }

    public void setUseGet(boolean useGet) {
        mUseGet = useGet;
    }

    public void setNeedCustomHeader(boolean needCustomHeader) {
        mNeedCustomHeader = needCustomHeader;
    }

    public void setPostData(byte[] data) {
        mPostData = data;
    }

    public static boolean isHttpProtocol(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        String tmp = url.trim().toLowerCase();
        return tmp.startsWith(HTTP_PROTOCOL) || tmp.startsWith(HTTPS_PROTOCOL);
    }

    public NetworkError requestByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        NetworkError resp = request(new MemoryResettableOutputStream(baos));
        responseBytes = baos.toByteArray();
        closeQuietly(baos);
        return resp;
    }

    /**
     * 请求json，该方法必须在后台线程中调用
     */
    public NetworkError requestJSON() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        NetworkError resp = request(new MemoryResettableOutputStream(baos));
        try {
            if (DEBUG) {
                d("requestJSON, Connection result: ", baos.toString());
            }
            if (resp == NetworkError.OK) {
                mResponse = new JSONObject(baos.toString());
            } else {
                if (DEBUG) {
                    e("requestJSON, Connection failed : ", resp.name());
                }
            }
        } catch (JSONException e) {
            if (DEBUG) {
                e(e, "requestJSON, JSON error: ");
            }
            return NetworkError.RESULT_ERROR;
        } finally {
            closeQuietly(baos);
        }
        return resp;
    }

    public NetworkError requestString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        NetworkError resp = request(new MemoryResettableOutputStream(baos));
        if (DEBUG) {
            d("requestString, Connection result: ", baos.toString());
        }
        if (resp == NetworkError.OK) {
            mString = baos.toString();
        } else {
            if (DEBUG) {
                e("requestString, Connection failed : ", resp.name());
            }
        }
        closeQuietly(baos);
        return resp;
    }

    /**
     * 请求文件，该方法必须在后台线程中调用
     */
    public NetworkError requestFile(File outFile) throws FileNotFoundException {
        if (outFile == null) {
            throw new IllegalArgumentException();
        }
        FileResettableOutputStream fos = null;
        try {
            fos = new FileResettableOutputStream(outFile);
        } catch (FileNotFoundException e) {
            if (DEBUG) {
                e(e,"requestFile, File not found: ");
            }
            throw e;
        }
        NetworkError resp = request(fos);
        closeQuietly(fos);
        try {
            if (resp != NetworkError.OK) {
                if (DEBUG) {
                    e(TAG, "requestFile, Connection failed : ", resp.name());
                }
                outFile.delete();
            }
        } catch (Exception ignored) {
        }
        return resp;
    }

    public NetworkError requestResponseHeader() {
        return request(null, false);
    }

    protected NetworkError request(ResettableOutputStream outputStream) {
        return request(outputStream, false);
    }

    protected NetworkError request(ResettableOutputStream outputStream, boolean requestHeaderOnly) {
        if (mUrl == null) {
            // url有问题
            return NetworkError.URL_ERROR;
        }

        if (mNetworkAgent != null && !mNetworkAgent.isNetworkAvailable()) {
            // 网络未链接，直接返回错误
            return NetworkError.NETWORK_ERROR;
        }

        if (mParameter == null) {
            // 如果用户没有指定参数，则加上基础参数
            mParameter = this.new Parameter();
        }

        // 处理参数，例如加密等
        Parameter finalParams;
        try {
            finalParams = onQueryCreated(mParameter);
        } catch (ConnectionException e) {
            return e.mError;
        }

        // 构造url
        String url = mUrl.toString();
        if (mUseGet) {
            // get parameters
            if (!finalParams.isEmpty()) {
                String query = mUrl.getQuery();
                String urlString = mUrl.toString();
                if (TextUtils.isEmpty(query)) {
                    urlString = contact(urlString, "?", finalParams.toEncodedString());
                } else {
                    urlString = contact(urlString, "&", finalParams.toEncodedString());
                }
                url = urlString;
            }
        }

        // 处理链接，例如加上签名等
        try {
            url = onURLCreated(url, finalParams);
        } catch (ConnectionException e) {
            return e.mError;
        }

        if (DEBUG) {
            d("request, connection url: ", url);
        }

        // post 数据
        if (!mUseGet) {
            // if post data is set outside, ignore all parameters
            if (mPostData != null && mPostData.length > 0) {
                mContentType = OCTET_STREAM;
            } else {
                if (!finalParams.isEmpty()) {
                    mPostData = finalParams.toEncodedString().getBytes();
                    if (DEBUG) {
                        d("request, [post]", finalParams);
                    }
                }
            }
        }

        long start_ms = System.currentTimeMillis();
        NetworkError err = innerRequest(url, outputStream, requestHeaderOnly);

        if (DEBUG) {
            long end_ms = System.currentTimeMillis();
            d(TAG, "request, Time(ms) spent in request: ", (end_ms - start_ms), ", ", url);
        }
        return err;
    }

    private void adjustCustomHeader(HttpURLConnection conn, String host) {
        if (conn == null) {
            return;
        }
        if (mParameterAgent != null && mParameter != null) {
            mParameterAgent.onParameter(mParameter.getParams());
        }
        if (mCookieAgent != null) {
            Header header = mCookieAgent.getCookie(host);
            if (header != null && header.name() != null) {
                String value = header.value();
                conn.addRequestProperty(header.name(), value == null ? "" : value);
            }
        }
        if (mHeaderAgent != null) {
            List<Header> headers = mHeaderAgent.getHeaders();
            if (headers != null) {
                for (Header header: headers) {
                    conn.addRequestProperty(header.name(), header.value());
                }
            }
        }
    }

    private NetworkError innerRequest(String url, ResettableOutputStream outputStream, boolean requestHeaderOnly) {
        ArrayList<String> retryUrls = new ArrayList<>();
        retryUrls.add(url);

        // 获取数据
        for (String retryUrl : retryUrls) {
            if (DEBUG) {
                d("innerRequest, hosted connection url: ", retryUrl);
            }
            HttpURLConnection conn = null;

            URL currUrl;
            try {
                currUrl = new URL(retryUrl);
            } catch (MalformedURLException e) {
                if (DEBUG) {
                    e(e, "innerRequest, URL error: ");
                }
                continue;
            }

            try {
                conn = (HttpURLConnection) currUrl.openConnection();
                conn.setConnectTimeout(CONNECT_TIMEOUT);
                if (mNetworkAgent != null && mNetworkAgent.isInWifiNetwork()) {
                    conn.setReadTimeout(WIFI_READ_TIMEOUT);
                } else {
                    conn.setReadTimeout(GPRS_READ_TIMEOUT);
                }

                if (mNeedCustomHeader) {
                    adjustCustomHeader(conn, currUrl.getHost());
                }

                if (mUseGet) {
                    conn.setRequestMethod(REQ_METHOD_GET);
                    conn.setDoOutput(false);
                } else {
                    conn.setRequestMethod(REQ_METHOD_POST);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    if (mPostData != null && mPostData.length > 0) {
                        conn.setRequestProperty(CONTENT_LENGTH, Integer.toString(mPostData.length));
                    }
                    if (!TextUtils.isEmpty(mContentType)) {
                        conn.setRequestProperty(CONTENT_TYPE, mContentType);
                    }
                }
                try {
                    conn = onConnectionCreated(conn);
                } catch (ConnectionException e) {
                    return e.mError;
                }
                conn.connect();
                // post data
                if (!mUseGet && mPostData != null && mPostData.length > 0) {
                    OutputStream out = conn.getOutputStream();
                    out.write(mPostData);
                    out.close();
                }

                int responseCode = conn.getResponseCode();
                mResponseCode = responseCode;
                mResponseHeader = conn.getHeaderFields();
                if (mCookieAgent != null && mResponseHeader != null) {
                    for (String key : mResponseHeader.keySet()) {
                        if (key != null && SET_COOKIE.equalsIgnoreCase(key)) {
                            List<String> fields = mResponseHeader.get(key);
                            mCookieAgent.setCookie(currUrl.getHost(), fields);
                            break;
                        }
                    }
                }
                NetworkError code = handleResponseCode(responseCode);
                if (requestHeaderOnly) {
                    return code;
                }
                if (code == NetworkError.OK) {
                    if (outputStream != null) {
                        BufferedInputStream bis = null;
                        try {
                            bis = new BufferedInputStream(conn.getInputStream(), 8192);
                            byte[] buffer = new byte[BUFFER_SIZE];
                            int count;
                            while ((count = bis.read(buffer, 0, BUFFER_SIZE)) > 0) {
                                outputStream.write(buffer, 0, count);
                            }
                            outputStream.flush();
                        } catch (Exception e) {
                            // 读取文件流的过程中异常有可能造成文件损坏，页面读取时造成ANR
                            if (DEBUG) {
                                e(e, "innerRequest, Connection Exception for ", currUrl.getHost(), " : read file stream error ");
                            }
                            outputStream.reset(); // 重置输出流
                            continue;
                        } finally {
                            closeQuietly(bis);
                        }
                    }
                } else {
                    try {
                        InputStream is = conn.getInputStream();
                        closeQuietly(is);
                    } catch (IOException ignored) {
                    }
                }

                // 执行到这里不会是网络问题，不再重试
                return code;
            } catch (Exception e) {
                if (DEBUG) {
                    e(e, "innerRequest, Connection Exception for ", currUrl.getHost(), " : ");
                }
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
        // 执行到这里说明网络有问题，一个都没有成功
        return NetworkError.NETWORK_ERROR;
    }

    /**
     * hook for subclasses to modify the parameters before connect, this method will be called before
     * {@code onURLCreated(String);}
     */
    protected Parameter onQueryCreated(Parameter params) throws ConnectionException {
        return params;
    }

    /**
     * hook for subclasses to modify url before connection created，{@code finalParams} is the result of
     * {@code onQueryCreated(Parameter);}
     */
    protected String onURLCreated(String url, Parameter finalParams) throws ConnectionException {
        return url;
    }

    /**
     * hook for subclasses to modify the connection before connect
     */
    protected HttpURLConnection onConnectionCreated(HttpURLConnection connection) throws ConnectionException {
        return connection;
    }

    protected boolean checkURL(URL url) {
        if (url == null) {
            return false;
        }
        String protocol = url.getProtocol();
        return HTTP_PROTOCOL.equals(protocol) || HTTPS_PROTOCOL.equals(protocol);
    }

    private NetworkError handleResponseCode(int code) {
        if (code == HttpURLConnection.HTTP_OK) {
            return NetworkError.OK;
        }
        if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            if (DEBUG) {
                e("handleResponseCode, Network Error : ", code);
            }
            return NetworkError.AUTH_ERROR;
        }
        if (DEBUG) {
            e("handleResponseCode, Network Error : ", code);
        }
        return NetworkError.SERVER_ERROR;
    }

    private static StringBuilder appendParameter(final StringBuilder sb, String key, String value, String encoding) {
        if (sb.length() > 0) {
            sb.append('&');
        }
        sb.append(key);
        sb.append('=');
        try {
            sb.append(URLEncoder.encode(value, encoding));
        } catch (UnsupportedEncodingException ignored) {
        }
        return sb;
    }

    private static StringBuilder appendParameter(final StringBuilder sb, String key, String value) {
        if (sb.length() > 0) {
            sb.append('&');
        }
        sb.append(key);
        sb.append('=');
        sb.append(value);
        return sb;
    }

    private static StringBuilder appendParameter(final StringBuilder sb, String key, String value, char delimiter) {
        if (sb.length() > 0) {
            sb.append(delimiter);
        }
        sb.append(key);
        sb.append('=');
        sb.append(value);
        return sb;
    }

    public class Parameter {
        // 参数是有序的，按照string的比较顺序
        private TreeMap<String, String> params;
        private boolean disallowEmptyValue = false;

        /**
         * 用户使用当前对象的构造方法时与相对应的Connection绑定.<br>
         * 例如：<br>
         * Connection conn = new Connection();<br>
         * Parameter para = conn.new Parameter();<br>
         * 返回的para已经被加载到conn中
         */
        public Parameter() {
            this(true);
        }

        public Parameter(boolean bindToConnection) {
            params = new TreeMap<>();
            disallowEmptyValue = false;
            if (bindToConnection) {
                // 参数是否要绑定到当前链接上
                Connection.this.mParameter = this;
            }
        }

        public void setDisallowEmptyValue(boolean value) {
            disallowEmptyValue = value;
        }

        public Parameter add(String key, String value) {
            return add(key, value, disallowEmptyValue);
        }

        public Parameter addMultiParams(Map<String, String> params, boolean allowEmpty) {
            if (params == null) {
                return this;
            }
            for (String key : params.keySet()) {
                add(key, params.get(key), allowEmpty);
            }
            return this;
        }

        public Parameter add(String key, String value, boolean allowEmpty) {
            if (TextUtils.isEmpty(value)) {
                if (!allowEmpty) {
                    return this;
                }
                value = "";
            }
            params.put(key, value);
            return this;
        }

        public Parameter add(String key, Object value) {
            if (value == null) {
                if (disallowEmptyValue) {
                    return this;
                }
                value = "";
            }
            params.put(key, String.valueOf(value));
            return this;
        }

        public Parameter add(String key, boolean value) {
            params.put(key, String.valueOf(value));
            return this;
        }

        public Parameter add(String key, int value) {
            params.put(key, String.valueOf(value));
            return this;
        }

        public String get(String key) {
            return params.get(key);
        }

        public boolean isEmpty() {
            return params.isEmpty();
        }

        public String toString() {
            return toString('&');
        }

        public String toString(char delimiter) {
            if (params.isEmpty()) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for (String key : params.keySet()) {
                sb = appendParameter(sb, key, params.get(key), delimiter);
            }
            return sb.toString();
        }

        public String toEncodedString() {
            return toEncodedString(UTF_8);
        }

        public String toEncodedString(String encoding) {
            if (params.isEmpty()) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for (String key : params.keySet()) {
                sb = appendParameter(sb, key, params.get(key), encoding);
            }
            return sb.toString();
        }

        public TreeMap<String, String> getParams() {
            return params;
        }
    }

    protected class ConnectionException extends Exception {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        protected NetworkError mError;

        public ConnectionException(NetworkError error) {
            mError = error;
        }
    }

    protected abstract class ResettableOutputStream extends OutputStream {
        protected OutputStream mOutputStream;

        public ResettableOutputStream(OutputStream outputStream) {
            if (outputStream == null) {
                throw new IllegalArgumentException("output stream is null");
            }
            mOutputStream = outputStream;
        }

        @Override
        public void close() throws IOException {
            mOutputStream.close();
        }

        @Override
        public void flush() throws IOException {
            mOutputStream.flush();
        }

        @Override
        public void write(byte[] buffer) throws IOException {
            mOutputStream.write(buffer);
        }

        @Override
        public void write(byte[] buffer, int offset, int count) throws IOException {
            mOutputStream.write(buffer, offset, count);
        }

        @Override
        public void write(int oneByte) throws IOException {
            mOutputStream.write(oneByte);
        }

        public abstract void reset();
    }

    protected class MemoryResettableOutputStream extends ResettableOutputStream {

        public MemoryResettableOutputStream(ByteArrayOutputStream outputStream) {
            super(outputStream);
        }

        @Override
        public void reset() {
            ((ByteArrayOutputStream) mOutputStream).reset();
        }
    }

    protected class FileResettableOutputStream extends ResettableOutputStream {
        private File mFile;

        public FileResettableOutputStream(File file) throws FileNotFoundException {
            super(new FileOutputStream(file));
            mFile = file;
        }

        @Override
        public void reset() {
            closeQuietly(mOutputStream);
            mFile.delete();
            try {
                mOutputStream = new FileOutputStream(mFile);
            } catch (FileNotFoundException e) {
                // 这里不会执行到，构造方法会抛出异常
            }
        }
    }

    private static String contact(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(string);
        }
        return sb.toString();
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignore) {
            }
        }
    }

    private void d(Object... msg) {
        NetLog.d(TAG, NetLog.msg(false, msg), info());
    }

    private void e(Object... msg) {
        NetLog.e(TAG, NetLog.msg(false, msg), info());
    }

    private void e(Throwable tr, Object... msg) {
        NetLog.e(TAG, tr, NetLog.msg(false, msg), info());
    }

    private String info() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nurl: ").append(mUrl).append('\n');
        sb.append("method: ").append(mUseGet ? REQ_METHOD_GET : REQ_METHOD_POST).append('\n');
        Map<String, List<String>> map = getResponseHeader();
        if (map != null && !map.isEmpty()) {
            sb.append("headers:");
            for (String key : map.keySet()) {
                if (key != null) {
                    sb.append('\n');
                    sb.append("\t\t").append(key).append(": ");
                    List<String> value = map.get(key);
                    if (value == null || value.isEmpty()) {
                        sb.append("null");
                    } else {
                        for (int i = 0, size = value.size(); i < size; i++) {
                            if (i > 0) {
                                sb.append(", ");
                            }
                            sb.append(value.get(i));
                        }
                    }
                }
            }
        }
        if (mHeaderAgent != null) {
            List<Header> headers = mHeaderAgent.getHeaders();
            if (headers != null && !headers.isEmpty()) {
                for (Header header : headers) {
                    sb.append('\n');
                    sb.append("\t\t").append(header.name()).append(": ").append(header.value());
                }
            }
        }
        if (mCookieAgent != null && mUrl != null) {
            Header cookie = mCookieAgent.getCookie(mUrl.getHost());
            if (cookie != null) {
                sb.append('\n');
                sb.append("Cookie: ").append(cookie.name()).append(", ").append(cookie.value());
            }
        }
        if (mParameter != null && mParameter.params != null && !mParameter.isEmpty()) {
            sb.append('\n');
            for (String key : mParameter.params.keySet()) {
                sb.append(key).append(": ").append(mParameter.params.get(key)).append('\n');
            }
        }
        return sb.toString();
    }
}
