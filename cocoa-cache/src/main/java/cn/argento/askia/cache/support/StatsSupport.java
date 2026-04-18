package cn.argento.askia.cache.support;

/**
 * 命中率支持
 */
public interface StatsSupport {
    long getHitCount();
    long getMissCount();
    double getHitRate();
    void resetStats();
}
