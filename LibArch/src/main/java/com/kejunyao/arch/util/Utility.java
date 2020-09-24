package com.kejunyao.arch.util;

import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * $类描述$
 *
 * @author kejunyao
 * @since 2020年09月06日
 */
public final class Utility {

    private Utility() {
    }

    private static <T> boolean checkIndex(List<T> list, int position) {
        return list != null && position >= 0 && position < list.size();
    }

    private static <T> boolean checkIndex(T[] array, int position) {
        return array != null && position >= 0 && position < array.length;
    }

    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNullOrEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNullOrEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNullOrEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNullOrEmpty(String[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNullOrEmpty(SparseArray<?> array) {
        return array == null || array.size() == 0;
    }

    public static boolean isNullOrEmpty(androidx.collection.ArrayMap<?, ?> arrayMap) {
        return arrayMap == null || arrayMap.size() == 0;
    }

    public static boolean isNullOrEmpty(Long[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNullOrEmpty(JSONArray array) {
        return array == null || array.length() == 0;
    }

    public static boolean isNullOrEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNullOrEmpty(RecyclerView.Adapter adapter) {
        return adapter == null || adapter.getItemCount() == 0;
    }

    public static int getLengthSafely(String str) {
        return str == null ? 0 : str.length();
    }

    public static int getSizeSafely(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    @Nullable
    public static <T> T getSafely(List<T> list, int position) {
        if (checkIndex(list, position)) {
            return list.get(position);
        }
        return null;
    }

    @Nullable
    public static <T> T getSafely(T[] array, int position) {
        if (checkIndex(array, position)) {
            return array[position];
        }
        return null;
    }

    @Nullable
    public static <T> T getFirstSafely(List<T> list) {
        return getSafely(list, 0);
    }

    @Nullable
    public static <T> T getLastSafely(List<T> list) {
        if (isNullOrEmpty(list)) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    @NonNull
    public static String join(Object... objects) {
        if (objects == null || objects.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object o : objects) {
            sb.append(o);
        }
        return sb.toString();
    }

    public static <T> String[] toStringArray(List<T> list) {
        if (isNullOrEmpty(list)) {
            return null;
        }
        final int size = list.size();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = list.get(i).toString();
        }
        return result;
    }

    public static boolean isTrue(Integer i, int v) {
        return i != null && i.intValue() == v;
    }

    public static boolean isTrue(Boolean b) {
        return b != null && b;
    }

    public static boolean isTrue(Long i, long v) {
        return i != null && i.longValue() == v;
    }
}
