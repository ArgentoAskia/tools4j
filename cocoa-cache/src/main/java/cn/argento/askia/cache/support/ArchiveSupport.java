package cn.argento.askia.cache.support;

import java.util.Map;
import java.util.Set;

/**
 * 缓存键值打包支持.
 * 比如打包成Map、javabean等
 */
public interface ArchiveSupport<K, V> {

    <JB> JB toJavaBean(Class<JB> jbClass);

    Map<K,V> toMap();

    Set<K> toKeySet();

}
