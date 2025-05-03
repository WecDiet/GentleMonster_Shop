package com.gentlemonster.GentleMonsterBE.Contants;

public class MessageKey {
    // **** Auth
    public static final String REGISTER_SUCCESSFULLY = "auth.register.register_successfully";
    public static final String REGISTER_FAILED = "auth.register.register_failed";
    public static final String LOGIN_SUCCESSFULLY = "auth.login.login_successfully";
    public static final String LOGIN_FAILED = "auth.login.login_failed";
    public static final String USER_ALREADY_EXIST = "error.auth.register.user_already_exists";
    public static final String USER_NOT_EXIST_LOGIN = "error.auth.login.user_not_exists";
    public static final String WRONG_INPUT = "error.auth.login.wrong_email_or_password";
    public static final String ACCOUNT_LOCKED = "error.auth.login.account_locked";
    public static final String DIFFERENT_PASSWORD = "error.auth.change.different_password";
    public static final String WRONG_PASSWORD = "error.auth.change.wrong_password";
    public static final String UPDATE_PASSWORD_SUCCESSFULLY = "auth.change.update_password_success";
    public static final String UPDATE_USER_SUCCESSFULLY = "auth.change.update_user_success";
    public static final String REFRESHTOKEN_NOT_EXIST = "error.auth.refreshtoken.not_exist";
    public static final String REFRESHTOKEN_EXPIRED = "error.auth.refreshtoken.expired";
    public static final String ACCESSTOKEN_SUCCESS = "auth.refreshtoken.get_access_token_success";
    public static final String UPDATE_AVATAR_SUCCESS = "auth.avatar.update_success";

    // ROLES
    public static final String ADD_ROLE_SUCCESS = "role.add_success";
    public static final String EDIT_ROLE_SUCCESS = "role.edit_success";
    public static final String DELETE_ROLE_SUCCESS = "role.delete_success";
    public static final String ROLE_EXISTED = "role.existed";
    public static final String ROLE_NOT_FOUND = "role.not_found";
    public static final String ROLE_GET_SUCCESS = "role.get_success";
    public static final String ROLE_NOT_EXIST = "role.not_exist";

    // USERS
    public static final String USER_GET_SUCCESS = "users.get_success";
    public static final String USER_CREATE_SUCCESS = "users.create_success";
    public static final String USER_PHONE_EXISTED = "users.phone_existed";
    public static final String USER_UPDATE_SUCCESS = "users.update_success";
    public static final String USER_DELETE_SUCCESS = "user.delete_success";
    public static final String NOT_DELETE_ADMIN_USER = "user.not_delete_admin";
    public static final String USER_GET_ONE_SUCCESS = "user.get_one_success";
    public static final String USER_EMAIL_INVALID = "user.email_invalid";
    public static final String USER_NOT_EXIST = "user.not_exist";
    public static final String USER_ROLE_NULL = "user.role_null";

    // PAYMENT TYPE
    public static final String PAYMENT_TYPE_CREATE_SUCCESS = "payment_type.create_success";
    public static final String PAYMENT_TYPE_UPDATE_SUCCESS = "payment_type.update_success";
    public static final String PAYMENT_TYPE_DELETE_SUCCESS = "payment_type.delete_success" ;
    public static final String PAYMENT_TYPE_GET_SUCCESS = "payment_type.get_success" ;

    // CATEGORY
    public static final String CATEGORY_CREATE_SUCCESS = "category.create_success";
    public static final String CATEGORY_UPDATE_SUCCESS = "category.update_success";
    public static final String CATEGORY_DELETE_SUCCESS = "category.delete_success" ;
    public static final String CATEGORY_GET_SUCCESS = "category.get_success" ;
    public static final String CATEGORY_NOT_FOUND = "category.not_found";
    public static final String CATEGORY_EXISTED = "category.existed";
    public static final String CATEGORY_REQUIRED = "category.required";

    // BANNER
    public static final String BANNER_CREATE_SUCCESS = "banner.create_success";
    public static final String BANNER_UPDATE_SUCCESS = "banner.update_success";
    public static final String BANNER_DELETE_SUCCESS = "banner.delete_success" ;
    public static final String BANNER_GET_SUCCESS = "banner.get_success" ;
    public static final String BANNER_NOT_FOUND = "banner.not_found";
    public static final String BANNER_EXISTED = "banner.existed";
    public static final String BANNER_IMAGE_UPDATE_SUCCESS = "banner.image.update_success";
    public static final String BANNER_REQUIRED = "banner.required";
    public static final String BANNER_EMPTY = "banner.empty";

    // PRODUCT
    public static final String PRODUCT_CREATE_SUCCESS = "product.create_success";
    public static final String PRODUCT_UPDATE_SUCCESS = "product.update_success";
    public static final String PRODUCT_DELETE_SUCCESS = "product.delete_success" ;
    public static final String PRODUCT_GET_SUCCESS = "product.get_success" ;
    public static final String PRODUCT_NOT_EXISTED = "product.not_existed";
    public static final String PRODUCT_IMAGE_UPDATE_SUCCESS = "product.image.update_success";
    public static final String PRODUCT_LIKED = "product.liked_success";
    public static final String PRODUCT_LIKE_FAILED = "product.like_failed";
    public static final String PRODUCT_UNLIKED = "product.unlike_success";
    public static final String PRODUCT_UNLIKED_FAILED = "product.unlike_failed";
    public static final String PRODUCT_NOT_FOUND = "product.not_found";
    public static final String PRODUCT_REQUIRED = "product.required";
    public static final String PRODUCT_EMPTY = "product.empty";
    public static final String PRODUCT_EXISTED = "product.existed";
    public static final String FIND_PRODUCT_CATEGORY_SLIDER_NULL = "product.category_slider_is_null";

    // PRODUCT TYPE
    public static final String PRODUCT_TYPE_CREATE_SUCCESS = "product_type.create_success";
    public static final String PRODUCT_TYPE_UPDATE_SUCCESS = "product_type.update_success";
    public static final String PRODUCT_TYPE_DELETE_SUCCESS = "product_type.delete_success";
    public static final String PRODUCT_TYPE_GET_SUCCESS = "product_type.get_success";
    public static final String PRODUCT_TYPE_NOT_FOUND = "product_type.not_found";
    public static final String PRODUCT_TYPE_EXISTED = "product_type.existed";
    public static final String PRODUCT_TYPE_REQUIRED = "product_type.required";
    public static final String PRODUCT_TYPE_EMPTY = "product_type.empty";
    public static final String SLIDER_NOT_IN_CATEGORY = "product_type.slider_not_in_category";

    // SLIDER
    public static final String SLIDER_CREATE_SUCCESS = "slider.create_success";
    public static final String SLIDER_UPDATE_SUCCESS = "slider.update_success";
    public static final String SLIDER_DELETE_SUCCESS = "slider.delete_success";
    public static final String SLIDER_GET_SUCCESS = "slider.get_success";
    public static final String SLIDER_NOT_FOUND = "slider.not_found";
    public static final String SLIDER_EXISTED = "slider.existed";
    public static final String SLIDER_REQUIRED = "slider.required";
    public static final String SLIDER_EMPTY = "slider.empty";
    public static final String SLIDER_HIGHLIGHTED_EXISTED = "slider.highlighted_existed";

    // COLLABORATION
    public static final String COLLABORATION_CREATE_SUCCESS = "collaboration.create_success";
    public static final String COLLABORATION_UPDATE_SUCCESS = "collaboration.update_success";
    public static final String COLLABORATION_DELETE_SUCCESS = "collaboration.delete_success";
    public static final String COLLABORATION_GET_SUCCESS = "collaboration.get_success";
    public static final String COLLABORATION_NOT_FOUND = "collaboration.not_found";
    public static final String COLLABORATION_EXISTED = "collaboration.existed";
    public static final String COLLABORATION_REQUIRED = "collaboration.required";
    public static final String COLLABORATION_EMPTY = "collaboration.empty";

    // WAREHOUSE
    public static final String WAREHOUSE_CREATE_SUCCESS = "warehouse.create_success";
    public static final String WAREHOUSE_UPDATE_SUCCESS = "warehouse.update_success";
    public static final String WAREHOUSE_DELETE_SUCCESS = "warehouse.delete_success";
    public static final String WAREHOUSE_GET_SUCCESS = "warehouse.get_success";
    public static final String WAREHOUSE_NOT_FOUND = "warehouse.not_found";
    public static final String WAREHOUSE_EXISTED = "warehouse.existed";
    public static final String WAREHOUSE_REQUIRED = "warehouse.required";
    public static final String WAREHOUSE_EMPTY = "warehouse.empty";
    public static final String WAREHOUSE_WRONG_ROLE = "warehouse.wrong.role";
    public static final String FULLNAME_MISMATCH = "warehouse.mismatch";
    public static final String WAREHOUSE_PRODUCT_TYPE_CREATE_SUCCESS = "warehouse.product_type.create_success";
    public static final String WAREHOUSE_PRODUCT_TYPE_UPDATE_SUCCESS = "warehouse.product_type.update_success";
    public static final String WAREHOUSE_PRODUCT_TYPE_DELETE_SUCCESS = "warehouse.product_type.delete_success";
    public static final String WAREHOUSE_PRODUCT_TYPE_GET_SUCCESS = "warehouse.product_type.get_success";
    public static final String WAREHOUSE_PRODUCT_TYPE_NOT_FOUND = "warehouse.product_type.not_found";
    public static final String WAREHOUSE_PRODUCT_TYPE_EXISTED = "warehouse.product_type.existed";
    public static final String WAREHOUSE_PRODUCT_TYPE_REQUIRED = "warehouse.product_type.required";
    public static final String WAREHOUSE_PRODUCT_TYPE_EMPTY = "warehouse.product_type.empty";
    public static final String WAREHOUSE_PRODUCT_TYPE_QUANTITY_REQUIRED = "warehouse.product_type.quantity_required";
    public static final String WAREHOUSE_PRODUCT_TYPE_QUANTITY_INVALID = "warehouse.product_type.quantity_invalid";

    // PRODUCT WAREHOUSE
    public static final String PRODUCT_WAREHOUSE_CREATE_SUCCESS = "product_warehouse.create_success";
    public static final String PRODUCT_WAREHOUSE_UPDATE_SUCCESS = "product_warehouse.update_success";
    public static final String PRODUCT_WAREHOUSE_DELETE_SUCCESS = "product_warehouse.delete_success";
    public static final String PRODUCT_WAREHOUSE_GET_SUCCESS = "product_warehouse.get_success";
    public static final String PRODUCT_WAREHOUSE_NOT_FOUND = "product_warehouse.not_found";
    public static final String PRODUCT_WAREHOUSE_EXISTED = "product_warehouse.existed";
    public static final String PRODUCT_WAREHOUSE_REQUIRED = "product_warehouse.required";
    public static final String PRODUCT_WAREHOUSE_EMPTY = "product_warehouse.empty";
    public static final String PRODUCT_WAREHOUSE_QUANTITY_INVALID = "product_warehouse.quantity.invalid";
    public static final String PRODUCT_WAREHOUSE_DONT_PUBLIC = "product_warehouse.dont_public";

    // CITY
    public static final String CITY_CREATE_SUCCESS = "city.create_success";
    public static final String CITY_UPDATE_SUCCESS = "city.update_success";
    public static final String CITY_DELETE_SUCCESS = "city.delete_success";
    public static final String CITY_GET_SUCCESS = "city.get_success";
    public static final String CITY_NOT_FOUND = "city.not_found";
    public static final String CITY_EXISTED = "city.existed";
    public static final String CITY_REQUIRED = "city.required";
    public static final String CITY_EMPTY = "city.empty";

    // SUBSIDIARY
    public static final String SUBSIDIARY_CREATE_SUCCESS = "subsidiary.create_success";
    public static final String SUBSIDIARY_UPDATE_SUCCESS = "subsidiary.update_success";
    public static final String SUBSIDIARY_DELETE_SUCCESS = "subsidiary.delete_success";
    public static final String SUBSIDIARY_GET_SUCCESS = "subsidiary.get_success";
    public static final String SUBSIDIARY_NOT_FOUND = "subsidiary.not_found";
    public static final String SUBSIDIARY_EXISTED = "subsidiary.existed";
    public static final String SUBSIDIARY_REQUIRED = "subsidiary.required";
    public static final String SUBSIDIARY_EMPTY = "subsidiary.empty";
    public static final String SUBSIDIARY_CITY_NOT_FOUND = "subsidiary.city_not_found";
    public static final String SUBSIDIARY_CITY_REQUIRED = "subsidiary.city_not_required";
    public static final String SUBSIDIARY_CITY_EMPTY = "subsidiary.city_not_empty";
    public static final String SUBSIDIARY_CITY_NOT_EXIST = "subsidiary.city_not_existed";
}

