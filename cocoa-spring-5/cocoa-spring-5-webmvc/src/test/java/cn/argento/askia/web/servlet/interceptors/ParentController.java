package cn.argento.askia.web.servlet.interceptors;


import cn.argento.askia.utilities.classes.ClassUtility;
import cn.argento.askia.utilities.collection.ArrayUtility;

import java.io.File;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * 通用接口API控制器, 记录一些通用的接口信息
 *
 * fixme: 此包中的类在后期需要制作成代理, Api全部变成接口
 */
public class ParentController {

    // MIME Types
    public static final String RESPONSE_VIEW_TYPE_JSON = "application/json";
    public static final String RESPONSE_VIEW_TYPE_HTML = "text/html";

    // API URL 前缀!
    protected static final String API_VER = "v1";
    protected static final String PREFIX = "/api";
    public static final String API_URL = PREFIX + "/" + API_VER;
    public static final String COMMON_PATH = "/**";

    // API URL For System config module
    @Deprecated
    protected static final String systemModuleApiPrefix = "/api/v1/system/";
    @Deprecated
    public static final String initPassword = systemModuleApiPrefix + "initPassword";
    @Deprecated
    public static final String defaultAvatar = systemModuleApiPrefix + "defaultAvatar";
    @Deprecated
    public static final String updateNameLimitedTime = systemModuleApiPrefix + "updateNameLimitedTime";
    @Deprecated
    public static final String dictStatus = systemModuleApiPrefix + "dict/status";
    @Deprecated
    public static final String defaultName = systemModuleApiPrefix + "defaultName";
    @Deprecated
    public static final String productNotice = systemModuleApiPrefix + "notice";

    public static final String SYSTEM_PREFIX = "/system";
    public static final String SYSTEM_API_PREFIX = API_URL + SYSTEM_PREFIX;

    // ==================== system module public api(被前端调用) ===============
    // 系统说明
    public static final String SYSTEM_NOTICE = "/notice";
    // Addresses相关
    public static final String SYSTEM_ADDRESS = "/address";
    // ==================== system module public api ===============

    // config相关
    public static final String SYSTEM_API_NOTICE = SYSTEM_NOTICE;
    public static final String SYSTEM_API_INIT_PASSWORD = "/initPassword";
    public static final String SYSTEM_API_DEFAULT_AVATAR = "/defaultAvatar";
    public static final String SYSTEM_API_UPDATE_NAME_LIMITED_TIME = "/updateNameLimitedTime";
    public static final String SYSTEM_API_DEFAULT_NAME = "/defaultName";
    public static final String SYSTEM_API_DEFAULT_IMAGE = "/defaultImage";
    public static final String SYSTEM_API_INTERNAL_CONFIG = "/internal/config";


    // DICT字段查询
    public static final String SYSTEM_DICT_PRODUCT = "/dict/product";
    public static final String SYSTEM_DICT_STATUS = "/dict/status";
    public static final String SYSTEM_API_DICT = SYSTEM_DICT_PRODUCT;
    public static final String SYSTEM_API_DICT_STATUS = SYSTEM_DICT_STATUS;
    // locale相关

    public static final String SYSTEM_API_LOCALE = "/locale";
    public static final String SYSTEM_API_LOCALE_NAME = "/locale/name";
    // Page相关
    public static final String SYSTEM_PAGE = "/page";
    public static final String SYSTEM_API_PAGE = SYSTEM_PAGE;


    // API URL FOR WMS module
    public static final String WMS_PREFIX = "/wms";
    public static final String WMS_API_PREFIX = ParentController.API_URL + WMS_PREFIX;
    // ProductController 前缀URL(因为归属wms模块的内容比较多, 有组件数据, 有出品方, 有商品, 因此我们还设置了子前缀)
    public static final String WMS_PRODUCTS = "/products";
    public static final String WMS_BRANDS = "/brands";
    public static final String WMS_SECTIONS = "/sections";

    // API相关(API无需设置子前缀)
    public static final String WMS_API_PRODUCT_GET_PRICES = "/getProductPrices";
    public static final String WMS_API_GET_PRODUCT = "/getProduct";
    public static final String WMS_API_GET_PRODUCTS = "/getProducts";
    public static final String WMS_API_GET_ALL_PRODUCTS = "/getAllProducts";


    // API URL FOR USER module
    public static final String USER_PREFIX = "/user";
    public static final String USER_API_PREFIX = ParentController.API_URL + USER_PREFIX;
    // 权限相关(API)
    public static final String USER_API_GET_ROLE_ID = "/getRoleIds";
    public static final String USER_API_GET_USER_ROLE_IDS = "/getUserRoleIds";
    public static final String USER_API_GET_ADMIN_TYPE = "/getAdminType";
    public static final String USER_API_GET_NON_USER_TYPE = "/getNonUserType";
    public static final String USER_API_GET_ALL_ROLES = "/getAllRoles";
    // 用户信息相关(API)
    public static final String USER_API_GET_USER_ID = "/getUserId";
    public static final String USER_API_GET_DEFAULT_ADDRESS_ID = "/getDefaultAddressId";
    public static final String USER_API_GET_DEFAULT_ADDRESS= "/getDefaultAddress";
    public static final String USER_API_GET_USER_MESSAGE = "/getUserMessage";
    // 用户验证相关API
    public static final String USER_API_CHECK_USER = "/checkUser";

    // 用户购物车相关
    // 获取用户购物车最小数据源
    public static final String USER_API_GET_USER_SHOP_CARD_MIN = "/getUserShopCardMin";

    // 用户历史
    public static final String USER_HISTORY = "/history";
    // 用户Order
    public static final String USER_ORDER = "/order";
    // 用户地址
    public static final String USER_ADDRESS = "/address";
    // 用户Account
    public static final String USER_ACCOUNT = "/account";


    // 同步订单
    public static final String USER_CARD_ORDER_SYNC = "/cardOrderSync";
    // 用户购物车
    public static final String USER_CARD = "/card";
    // 购物车修改数量
    public static final String USER_CARD_QTY = USER_CARD + "/cardOrderQty";
    // 购物车订单
    public static final String USER_CARD_ORDER = "/cardOrder";
    // 购物车订单移除商品
    public static final String USER_CARD_ORDER_PRODUCT_REMOVE = "/cardOrderProductRemove";


    // API URL For Order Module
    public static final String ORDER_PREFIX = "/order";
    // =========================== 事件API ===========================
    // 发生在订单提交之后的事件
    public static final String ORDER_SUBMITTED = "/onOrderSubmitted";
    public static final String ORDER_PAID = "/onOrderPaid";
    public static final String ORDER_BALANCING = "/onOrderBalancing";
    public static final String ORDER_BALANCED = "/onOrderBalanced";

    // =========================== 事件API ===========================
    // 获取正在提交中的订单
    public static final String ORDER_GET_SUBMITTING = "/orderGetSubmitting";
    public static final String ORDER_USER_CLOSED = "/userClosingOrder";     // 用户在个人档案的订单选项页中点击关闭订单蓝色按钮触发



    // api层
    public static final String ORDER_API_PREFIX = ParentController.API_URL + ORDER_PREFIX;

    public static final String ORDER_API_USER = "/userOrder";

    public static final String ORDER_API_CREATE = "/createOrder";
    public static final String ORDER_API_CREATE_ADD_PRODUCTS = ORDER_API_CREATE + "/addProducts";
    public static final String ORDER_API_CREATE_REMOVE_PRODUCTS = ORDER_API_CREATE + "/removeProducts";
    public static final String ORDER_API_CREATE_UPDATE_PRODUCTS_QTY = ORDER_API_CREATE + "/updateProductQty";
    public static final String ORDER_API_REMOVE_PRODUCTS_IN_ORDER = "/removeProducts0";
    public static final String ORDER_API_MODIFY = "/modifyOrder";
    public static final String ORDER_API_ROLLBACK_MODIFY = "/rollbackModifyOrder";
    public static final String ORDER_API_CLOSE = "/closeOrder";
    public static final String ORDER_API_SUBMIT = "/submitOrder";

    // 重定向层
    // 检查订单状态
    public static final String ORDER_CHECK_STATUS = "/orderStatusChecked";





    // admin管理系统前缀
    public static final String ADMIN_PREFIX = "/admin";
    // common
    public static final String ADMIN_COMMON = "/common";
    public static final String ADMIN_COMMON_GET_ORDER_STATUS = "/getOrderStatus";
    public static final String ADMIN_COMMON_GET_PRODUCT_IDS = "/getProductIds";
    public static final String ADMIN_COMMON_GET_USER_IDS = "/getUserIds";
    public static final String ADMIN_COMMON_GET_USER_NAMES = "/getUserNames";
    public static final String ADMIN_COMMON_GET_USER_EMAILS = "/getUserEmails";
    public static final String ADMIN_COMMON_GET_SF_CODES = "/getSFCodes";
    public static final String ADMIN_COMMON_GET_PRODUCT_CODES = "/getProductCodes";
    public static final String ADMIN_COMMON_GET_PRODUCT_NAMES = "/getProductNames";
    public static final String ADMIN_COMMON_GET_ORDER_IDS = "/getOrderIds";
    public static final String ADMIN_COMMON_GET_OPERATOR_IDS = "/getOperatorIds";
    public static final String ADMIN_COMMON_GET_OPERATOR_NAMES = "/getOperatorNames";

    public static final String ADMIN_COMMON_GET_USERS_MATCHED = "/getUsersMatched";
    public static final String ADMIN_COMMON_GET_PRODUCTS_MATCHED = "/getProductsMatched";
    public static final String ADMIN_COMMON_GET_ADDRESSES_MATCHED = "/getAddressesMatched";
    public static final String ADMIN_COMMON_GET_ORDERS_MATCHED = "/getOrdersMatched";

    public static final String ADMIN_COMMON_GET_DIALOGS_PRODUCTS = "/getDialogProducts";

    // product相关
    public static final String ADMIN_PRODUCT = "/product";
    public static final String ADMIN_PRODUCTS = "/products";
    // 左上角顶部标签页选择组件API(创建状态)
    public static final String ADMIN_CREATE_STATUS_PRODUCTS = ADMIN_PRODUCT + "/created/status";
    // 右上角名字搜索
    public static final String ADMIN_SEARCH_BY_NAME = ADMIN_PRODUCT + "/search/name";

    public static final String ADMIN_SEARCH_BY_KEYWORD = ADMIN_PRODUCT + "/search/keyword";
    
    // 左上角名称搜索
    public static final String ADMIN_SEARCH_BY_CLASSIC = ADMIN_PRODUCT + "/search/classic";
    // 商品状态
    public static final String ADMIN_DICT_PRODUCT_STATUS = ADMIN_PRODUCT + "/status";
    public static final String ADMIN_DICT_PRODUCT_TYPE = ADMIN_PRODUCT + "/type";
    public static final String ADMIN_DICT_PRODUCT_SIZE = ADMIN_PRODUCT + "/size";

    // 更新商品前上锁
    public static final String ADMIN_PRODUCT_LOCK = ADMIN_PRODUCT + "/lock";
    public static final String ADMIN_PRODUCT_UNLOCK = ADMIN_PRODUCT + "/unlock";
    public static final String ADMIN_PRODUCT_ENFORCE_UNLOCK = ADMIN_PRODUCT + "/enforce/unlock";

    // 确认SKU是否重复
    public static final String ADMIN_PRODUCT_CHECK_SKU_UNIQUE = ADMIN_PRODUCT + "/check/sku/unique";

    // 上下架
    public static final String ADMIN_PRODUCT_ON_OFF_SHELVES = ADMIN_PRODUCT + "/putOnOffShelves";

    // 状态改变
    public static final String ADMIN_PRODUCT_CREATED_STATUS = ADMIN_PRODUCT + "/productCreatedStatus";

    public static final String ADMIN_PRODUCT_MODIFY_STATUS = ADMIN_PRODUCT + "/modifyProductStatus";

    // 改价相关API
    public static final String ADMIN_PRODUCT_MODIFY_PRICE = ADMIN_PRODUCT + "/modifyProductPrice";
    // 该库存API
    public static final String ADMIN_PRODUCT_MODIFY_STOCK = ADMIN_PRODUCT + "/modifyProductStock";




    // user相关
    public static final String ADMIN_USER = "/user";
    public static final String ADMIN_USERS= "/users";
    public static final String ADMIN_USER_GET_ROLES = ADMIN_USER + "/getRoles";
    public static final String ADMIN_USER_GET_STATUSES = ADMIN_USER + "/getUserStatus";
    public static final String ADMIN_USER_SEARCH = ADMIN_USER + "/search";
    public static final String ADMIN_USER_STATUS = ADMIN_USER + "/status";
    public static final String ADMIN_USERS_STATUS = ADMIN_USERS + "/status";

    public static final String ADMIN_USERS_BATCH_IMPORT = ADMIN_USERS + "/batchImport";

    // ADMIN LOGIN相关
    public static final String ADMIN_LOGIN = "/login";
    public static final String ADMIN_LOGIN_LICENCE = ADMIN_LOGIN + "/licence";
    public static final String ADMIN_LOGIN_TOKEN = ADMIN_LOGIN + "/token";


    // ADMIN ORDERS相关
    public static final String ADMIN_ORDERS = "/orders";
    public static final String ADMIN_ORDER = "/order";
    public static final String ADMIN_ORDER_SEARCH = ADMIN_ORDER + "/search";
    public static final String ADMIN_ORDER_TIMELINE = ADMIN_ORDER + "/orderTimeline";
    public static final String ADMIN_ORDER_USER_MESSAGES = ADMIN_ORDER + "/orderUserMessages";
    public static final String ADMIN_ORDER_PRODUCTS = ADMIN_ORDER + "/orderProducts";
    public static final String ADMIN_ORDER_BALANCE_PRODUCTS = ADMIN_ORDER + "/orderBalanceProducts";
    public static final String ADMIN_ORDER_PAID_NEXT_STATUS = ADMIN_ORDER + "/paidOrderNextStatus";
    public static final String ADMIN_ORDER_ZHUAN_REN_ZHUAN_PAI = ADMIN_ORDER + "/zhuanRenZhuanPai";
    public static final String ADMIN_ORDER_SF_CODE_WRITE = ADMIN_ORDER + "/sfCodeWrite";
    // 关闭订单
    public static final String ADMIN_ORDER_CLOSE = ADMIN_ORDER + "/closed";
    // 导出预定单
    public static final String ADMIN_ORDER_EXPORT_PRE_ORDER = ADMIN_ORDER + "/exportPreOrder";
    // 开启补款
    public static final String ADMIN_ORDER_BEGIN_BALANCE = ADMIN_ORDER + "/orderBeginBalance";
    // 改价
    public static final String ADMIN_ORDER_PRICE_MODIFY = ADMIN_ORDER + "/orderPriceModify";
    // 结束补款
    public static final String ADMIN_ORDER_END_BALANCE = ADMIN_ORDER + "/orderEndBalance";
    // 修改balance相关的数据
    public static final String ADMIN_ORDER_END_BALANCE_MODIFY = ADMIN_ORDER + "/orderEndBalanceModify";
    // 发送补款通知
    public static final String ADMIN_ORDER_SEND_BALANCE_EMAIL = ADMIN_ORDER + "/sendBalanceEmail";


    // 系统模块
    // config配置相关
    public static final String ADMIN_SYSTEM = "/system";
    public static final String ADMIN_SYSTEMS = "/systems";
    public static final String ADMIN_SYSTEM_CONFIG = ADMIN_SYSTEM + "/config";

}
