package cn.argento.askia.supports.matcher;

import java.util.concurrent.ConcurrentHashMap;

public class PathMatchers {

    private static final ConcurrentHashMap<String, PathMatcher> cache;


    static {
        cache = new ConcurrentHashMap<>();
    }

    public static PathMatcher newWildcardMatcher(String pattern){
        PathMatcher pathMatcher = cache.get(pattern);
        if (pathMatcher == null){
            synchronized (PathMatchers.class){
                pathMatcher = cache.get(pattern);
                if (pathMatcher == null){
                    pathMatcher = new WildcardMatcher(pattern);
                    cache.put(pattern, pathMatcher);
                }
            }
        }
        return pathMatcher;
    }
}
