package com.kejunyao.arch.file;

import android.content.Context;
import android.text.TextUtils;
import com.kejunyao.arch.util.Utility;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 文件工具类
 *
 * @author kejunyao
 * @since 2020年09月06日
 */
public final class FileUtils {

    private static final int KB = 1024;
    private static final int MB = KB * KB;

    private FileUtils() {
    }

    /**
     * 拷贝文件
     * @param source 源文件
     * @param target 目标文件
     * @return true，拷贝成功；false，拷贝失败
     */
    public boolean copy(File source, File target) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        boolean success = false;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(target);
            byte[] buffer = new byte[KB];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
            fos.flush();
            success = true;
        } catch (Exception ignore) {
        } finally {
            closeSafely(fos);
            closeSafely(fis);
        }
        return success;
    }

    /**
     * 快速拷贝文件（大文件拷贝，1M以上）
     * @param src 原文件路径
     * @param target 目标文件路径
     * @return true，拷贝成功；false，拷贝失败
     */
    public static boolean fastCopy(String src, String target) {
        return fastCopy(new File(src), new File(target));
    }

    /**
     * 快速拷贝文件（大文件拷贝，1M以上）
     * @param src 原文件
     * @param target 目标文件
     * @return true，拷贝成功；false，拷贝失败
     */
    public static boolean fastCopy(File src, File target) {
        FileChannel fic = null;
        FileChannel foc = null;
        boolean success = false;
        try {
            fic = new FileInputStream(src).getChannel();
            foc = new FileOutputStream(target).getChannel();
            MappedByteBuffer buff = fic.map(FileChannel.MapMode.READ_ONLY, 0, fic.size());
            foc.write(buff);
            success = true;
        } catch (Exception e) {
        } finally {
            closeSafely(foc);
            closeSafely(fic);
            return success;
        }
    }

    private static final String[] getAssetsDirs(Context context, String dir) {
        String[] dirs = null;
        try {
            dirs = context.getAssets().list(dir);
        } catch (Exception e) {
        } finally {
            return dirs;
        }
    }

    public static final List<String> getAssetsFiles(Context context, String dir) {
        LinkedList<String> tmpDir = new LinkedList<>();
        ArrayList<String> allFile = new ArrayList<>();
        tmpDir.add(dir);
        String tmp;
        while (!tmpDir.isEmpty()) {
            tmp = tmpDir.remove();
            if (TextUtils.isEmpty(tmp)) {
                continue;
            }
            String[] dirs = getAssetsDirs(context, tmp);
            if (Utility.isNullOrEmpty(dirs)) {
                allFile.add(tmp);
            } else {
                for (String d : dirs) {
                    String path = tmp + File.separator + d;
                    String[] ds = getAssetsDirs(context, path);
                    if (Utility.isNullOrEmpty(ds)) {
                        allFile.add(path);
                    } else {
                        tmpDir.add(path);
                    }
                }
            }
        }
        tmpDir.clear();
        return allFile;
    }

    /**
     * 获取指定文件夹下所有的文件夹及文件
     * @param dir 指定文件夹
     * @return 指定文件夹下所有的文件夹及文件
     */
    public static List<File> findAllFile(String dir) {
        if (TextUtils.isEmpty(dir)) {
            return null;
        }
        return findAllFile(new File(dir));
    }

    /**
     * 获取指定文件夹下所有的文件夹及文件
     * @param dir 指定文件夹
     * @return 指定文件夹下所有的文件夹及文件
     */
    public static List<File> findAllFile(File dir) {
        if (dir == null || !dir.exists()) {
            return null;
        }
        LinkedList<File> tmpDirectory = new LinkedList<>();
        LinkedList<File> allFile = new LinkedList<>();
        tmpDirectory.add(dir);
        File tmp;
        while (!tmpDirectory.isEmpty()) {
            tmp = tmpDirectory.removeFirst();
            if (tmp == null) {
                continue;
            }
            allFile.add(tmp);
            if (tmp.isDirectory()) {
                File[] files = tmp.listFiles();
                if (files == null || files.length == 0) {
                    continue;
                }
                for (File file : files) {
                    allFile.add(file);
                    if (file.isDirectory()) {
                        tmpDirectory.add(file);
                    }
                }
            }
        }
        return allFile;
    }

    public static boolean hasChildrenFiles(File director) {
        File[] files = director.listFiles();
        return files != null && files.length > 0;
    }

    public static String getFileSizeText(long fileLength) {
        if (fileLength >= MB) {
            return (fileLength / MB) + "MB";
        }
        if (fileLength >= KB) {
            return (fileLength / KB) + "KB";
        }
        return fileLength + "B";
    }

    public static File[] findFiles(String dir) {
        File parent = new File(dir);
        File[] files = parent.listFiles();
        return files;
    }

    /**
     * 是否为本地文件
     * @param source 文件来源
     * @return true，本地文件；false，非本地文件
     */
    public static boolean isLocalFile(String source) {
        return new File(source).exists();
    }

    public static void closeSafely(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignore) {
            }
        }
    }
}
