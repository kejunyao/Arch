package com.kejunyao.arch.cache;

import androidx.collection.ArrayMap;

/**
 * 全局缓存
 *
 * @author kejunyao
 * @since 2020年09月06日
 */
public final class GlobalMemoryCache {

    private volatile static GlobalMemoryCache sInstance;

    private GlobalMemoryCache() {
    }

    public static final GlobalMemoryCache getInstance() {
        if (sInstance == null) {
            synchronized (GlobalMemoryCache.class) {
                if (sInstance == null) {
                    sInstance = new GlobalMemoryCache();
                }
            }
        }
        return sInstance;
    }

    private ArrayMap<String, Object> sCache;

    /**
     * 初始化Cache
     */
    private void initCache() {
        if (sCache == null) {
            sCache = new ArrayMap<>();
        }
    }

    /**
     * 移除缓存变量
     *
     * @param key 缓存变量标识key
     */
    public void remove(String key) {
        if (sCache == null) {
            return;
        }
        synchronized (GlobalMemoryCache.class) {
            sCache.remove(key);
        }
    }

    /**
     * 全局缓存中是否存在含key的变量
     *
     * @param key 缓存变量标识key
     */
    public boolean has(String key) {
        return sCache != null && sCache.containsKey(key);
    }

    /**
     * 设置全局缓存
     *
     * @param key   缓存变量标识key
     * @param value 缓存变量值
     */
    public void set(String key, Object value) {
        synchronized (GlobalMemoryCache.class) {
            initCache();
            sCache.put(key, value);
        }
    }

    /**
     * 获取缓存变量
     *
     * @param key 缓存变量标识key
     * @param def 没有获取到值时的默认值
     * @return 缓存变量值
     */
    public Object get(String key, Object def) {
        if (sCache != null) {
            return sCache.get(key);
        }
        return def;
    }

    /**
     * 缓存变量标识key对应的缓存变量值是否为true
     *
     * @param key 缓存变量标识key
     * @return
     */
    public boolean is(String key) {
        if (sCache != null) {
            Object o = sCache.get(key);
            if (o instanceof Boolean) {
                return ((Boolean) o).booleanValue();
            }
        }
        return false;
    }

    /**
     * 获取long类型的缓存变量值
     *
     * @param key 缓存变量标识key
     * @param def 没有获取到值时的默认值
     * @return long类型的缓存变量值
     */
    public long getLong(String key, long def) {
        Object o = get(key, null);
        if (o instanceof Long) {
            return ((Long) o).longValue();
        }
        return def;
    }

    /**
     * 获取int类型的缓存变量值
     *
     * @param key 缓存变量标识key
     * @param def 没有获取到值时的默认值
     * @return int类型的缓存变量值
     */
    public int getInt(String key, int def) {
        Object o = get(key, null);
        if (o instanceof Integer) {
            return ((Integer) o).intValue();
        }
        return def;
    }

    /**
     * 获取short类型的缓存变量值
     *
     * @param key 缓存变量标识key
     * @param def 没有获取到值时的默认值
     * @return short类型的缓存变量值
     */
    public short getShort(String key, short def) {
        Object o = get(key, null);
        if (o instanceof Short) {
            return ((Short) o).shortValue();
        }
        return def;
    }

    /**
     * 获取byte类型的缓存变量值
     *
     * @param key 缓存变量标识key
     * @param def 没有获取到值时的默认值
     * @return byte类型的缓存变量值
     */
    public byte getByte(String key, byte def) {
        Object o = get(key, null);
        if (o instanceof Byte) {
            return ((Byte) o).byteValue();
        }
        return def;
    }

    /**
     * 获取float类型的缓存变量值
     *
     * @param key 缓存变量标识key
     * @param def 没有获取到值时的默认值
     * @return float类型的缓存变量值
     */
    public float getFloat(String key, float def) {
        Object o = get(key, null);
        if (o instanceof Float) {
            return ((Float) o).floatValue();
        }
        return def;
    }

    /**
     * 获取double类型的缓存变量值
     *
     * @param key 缓存变量标识key
     * @param def 没有获取到值时的默认值
     * @return double类型的缓存变量值
     */
    public double getDouble(String key, double def) {
        Object o = get(key, null);
        if (o instanceof Double) {
            return ((Double) o).doubleValue();
        }
        return def;
    }

    /**
     * 获取String类型的缓存变量值
     *
     * @param key 缓存变量标识key
     * @param def 没有获取到值时的默认值
     * @return String类型的缓存变量值
     */
    public String getString(String key, String def) {
        Object o = get(key, null);
        if (o instanceof String) {
            return o.toString();
        }
        return def;
    }

    /**
     * 获取Boolean类型的缓存变量值
     *
     * @param key 缓存变量标识key
     * @param def 没有获取到值时的默认值
     * @return Boolean类型的缓存变量值
     */
    public boolean getBoolean(String key, boolean def) {
        Object o = get(key, null);
        if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue();
        }
        return def;
    }
}