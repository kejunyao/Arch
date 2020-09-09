package com.kejunyao.arch.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 网络工具类
 *
 * @author kejunyao
 * @since 2018年12月17日
 */
public final class NetworkUtils {

    private static final String TYPE_DISCONNECT = "Disconnect";
    private static final String TYPE_UNKNOWN    = "Unknown";
    private static final String TYPE_WIFI       = "WIFI";
    private static final String TYPE_MOBILE     = "Mobile";

    private NetworkUtils() {
    }

    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable(Context context) {
        boolean isAvailable = false;
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            isAvailable = info != null && info.isAvailable();
        } catch (Exception ignore) {
            ignore.printStackTrace();
        } finally {
            return isAvailable;
        }
    }

    @SuppressLint("MissingPermission")
    public static boolean isInWifiNetwork(Context context) {
        boolean isInWifiNetwork = false;
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            isInWifiNetwork = info != null && info.isAvailable() && info.getType() == ConnectivityManager.TYPE_WIFI;
        } catch (Exception ignore) {
        } finally {
            return isInWifiNetwork;
        }
    }

    @SuppressLint("MissingPermission")
    public static String getNetworkType(Context context) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info == null || !info.isAvailable()) {
                return TYPE_DISCONNECT;
            }
            int type = info.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                return TYPE_WIFI;
            }
            if (type == ConnectivityManager.TYPE_MOBILE) {
                return TYPE_MOBILE;
            }
        } catch (Exception ignore) {
        }
        return TYPE_UNKNOWN;
    }

    public static String getLocalIp() {
        String ip = "";
        try {
            // 获得本机的所有网络接口
            Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
            while (nifs.hasMoreElements()) {
                NetworkInterface nif = nifs.nextElement();
                // 获得与该网络接口绑定的 IP 地址，一般只有一个
                Enumeration<InetAddress> addresses = nif.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address) {
                        String address = addr.getHostAddress();
                        if (!"127.0.0.1".equals(address)) {
                            ip = address;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
        } finally {
            return ip;
        }
    }

    /**
     * http://www.trackip.net/i
     * https://ifconfig.me/all.json
     * http://ipinfo.io/ip
     * @return
     */
//    public static IPInfo getMyIP() {
//        IPInfo info = null;
//        try {
//            URL url = new URL("https://ifconfig.me/all.json");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            // JSON.
//            byte[] data = StreamUtility.readToEndAsArray(conn.getInputStream());
//            String json = new String(data);
//            Log.d("IPInfo", "json: " + json);
//            info = JSON.parseObject(json, IPInfo.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            return info;
//        }
//    }
}
