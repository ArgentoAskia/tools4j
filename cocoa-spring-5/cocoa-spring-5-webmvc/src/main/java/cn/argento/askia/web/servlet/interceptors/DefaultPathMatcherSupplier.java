package cn.argento.askia.web.servlet.interceptors;

import org.springframework.util.AntPathMatcher;

import java.util.function.Supplier;

/**
 * 默认PathMather提供器.
 *
 * <p>默认提供 {@link AntPathMatcher}, 此类暂不公开, 因为仅作兼容层使用</p>
 */
class DefaultPathMatcherSupplier implements Supplier<AntPathMatcher> {
    @Override
    public AntPathMatcher get() {
        return new AntPathMatcher();
    }
}
