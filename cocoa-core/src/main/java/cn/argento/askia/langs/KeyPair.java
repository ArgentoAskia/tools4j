package cn.argento.askia.langs;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Map;

/**
 * 键值对引用{@code JavaBean}.
 * <p>此类的设计目标是一个不可变的{@code JavaBean}类, 提供基础的键值映射能力, 开发过程中有些时候我们需要存储一个单一的, 简单的映射关系, 使用{@link Map}又太过重量化, 此时我们可以考虑使用 {@link KeyPair}</p>
 * @param <K> key类型
 * @param <V> value类型
 * @since 2026.4.24
 * @author Askia
 */
public class KeyPair<K, V> {
    private final K key;
    private final V value;

    private KeyPair(K key, V value){
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }


    @Override
    public String toString() {
        return "{ '" + key + "'" + " = " +
                value + " }";
    }

    public static <K, V> KeyPair<K, V> ofValueNullable(K key, V value){
        if (key == null){
            throw new InvalidParameterException("key不能为null");
        }
        return new KeyPair<>(key, value);
    }
    public static <K, V> KeyPair<K, V> ofNullable(K key, V value){
        return new KeyPair<>(key, value);
    }
    public static <K, V> KeyPair<K, V> of(K key, V value){
        if (key == null){
            throw new InvalidParameterException("key不能为null");
        }
        if (value == null){
            throw new InvalidParameterException("value不能为null");
        }
        return new KeyPair<>(key, value);
    }

    /**
     * 添加{@link KeyPair}到{@link Map}中.
     * @param map Map结构, 不能为 {@code null}
     * @param keyPair {@link KeyPair}, 不能为 {@code null}
     * @param <K> key类型
     * @param <V> value类型
     * @since 2026.4.24
     */
    public static <K, V> void putKeyPair2Map(Map<? super K, ? super V> map, KeyPair<K, V> keyPair){
        if (map == null){
            throw new InvalidParameterException("map不能为null");
        }
        if (keyPair == null){
            throw new InvalidParameterException("keyPair不能为null");
        }
        map.put(keyPair.key,
                keyPair.value);
    }
}
