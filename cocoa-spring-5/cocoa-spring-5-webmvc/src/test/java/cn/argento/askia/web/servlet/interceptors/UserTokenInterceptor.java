package cn.argento.askia.web.servlet.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * 用户信息拦截器, 使用此拦截器来拦截用户是否有携带Token来见我
 */
@Slf4j
@Interceptor(value = {
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_PRODUCTS,
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_PRODUCTS + ParentController.COMMON_PATH,
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_PRODUCT,
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_PRODUCT + ParentController.COMMON_PATH,
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_USERS,
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_USERS + ParentController.COMMON_PATH,
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_USER + ParentController.COMMON_PATH,
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_USER,
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_LOGIN_TOKEN,
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_COMMON + ParentController.COMMON_PATH,
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_ORDERS,
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_ORDERS + ParentController.COMMON_PATH,
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_ORDER + ParentController.COMMON_PATH,
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_ORDER,
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_SYSTEM,
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_SYSTEMS,
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_SYSTEMS + ParentController.COMMON_PATH,
        ParentController.ADMIN_PREFIX + ParentController.ADMIN_SYSTEM + ParentController.COMMON_PATH
}, order = 2000)
public class UserTokenInterceptor implements HandlerInterceptor {

}
