package com.gentlemonster.GentleMonsterBE.Contants;

public class Enpoint {

    public static final String API_PREFIX_ADMIN = "/pri/gentlemonster/manage";
    public static final String API_PREFIX_SHOP = "/us/int/shop";
//    public static final String API_PREFIX_DETAIL = "/us//shop";

    public static final class Auth{
        public static final String BASE = API_PREFIX_SHOP + "/customer";
        public static final String LOGIN = "/login";
        public static final String REGISTER = "/account_register";
        public static final String CHANGE_PASSWORD = "/change_password";
        public static final String CHANGE_USER_INFO = "/change_user-info";
    }

    public static final class User{
        public static final String BASE = API_PREFIX_ADMIN + "/users";
        public static final String NEW = "/new";
        public static final String EDIT = "/user_detail/{userID}";
        public static final String ID = "/user_detail/{userID}";
        public static final String DELETE = "/user_detail/{userID}";
        public static final String SEARCH_USER = "/search";
        public static final String DELETE_MANY = "/delete-many";
    }

    public static final class Role{
        public static final String BASE = API_PREFIX_ADMIN + "/roles";
        public static final String NEW = "/new";
        public static final String EDIT = "/role_detail/{roleID}";
        public static final String DELETE = "/role_detail/{roleID}";
        public static final String DELETE_MANY = "/delete-many";
    }


    public static final class Category{
        public static final String BASE = API_PREFIX_ADMIN + "/categories";
        public static final String NEW = "/new";
        public static final String EDIT = "/category_detail/{categoryID}";
        public static final String DELETE = "/category_detail/{categoryID}";
        public static final String DELETE_MANY = "/delete-many";
    }

    public static final class Slider{
        public static final String BASE = API_PREFIX_ADMIN + "/sliders";
        public static final String ID = "/slider_detail/{sliderID}";
        public static final String NEW = "/new";
        public static final String EDIT = "/slider_detail/{sliderID}";
        public static final String DELETE = "/slider_detail/{sliderID}";
        public static final String DELETE_MANY = "/delete-many";
        public static final String PUBLIC_SLIDER = "/list/{slug}";
    }

    public static final class Product_Type{
        public static final String BASE = API_PREFIX_ADMIN + "/product_types";
        public static final String NEW = "/new";
        public static final String EDIT = "/product_type_detail/{productTypeID}";
        public static final String DELETE = "/product_type_detail/{productTypeID}";
        public static final String ID = "/product_type_detail/{productTypeID}";
        public static final String PUBLIC_PRODUCT_TYPE = API_PREFIX_SHOP + "/list/{categorySlug}/{sliderSlug}";
    }

    public static final class Collaboration{
        public static final String BASEADMIN = API_PREFIX_ADMIN + "/collaborations";
        public static final String BASESHOP = API_PREFIX_SHOP + "/collaborations";
        public static final String NEW = "/new";
        public static final String EDIT = "/collaboration_detail/{collaborationID}";
        public static final String DELETE = "/collaboration_detail/{collaborationID}";
        public static final String ID = "/collaboration_detail/{collaborationID}";
    }

    public static final class Banner{
        public static final String BASE = API_PREFIX_ADMIN + "/banners";
        public static final String NEW = "/new";
        public static final String EDIT = "/{bannerID}";
        public static final String DELETE = "/{bannerID}";
        public static final String ID = "/{bannerID}";
        public static final String PUBLIC_BANNER = "/us";
    }

    public static final class Product{
        public static final String BASE = API_PREFIX_ADMIN + "/products";
        public static final String NEW = "/new";
        public static final String EDIT = "/product_detail/{productID}";
        public static final String DELETE = "/product_detail/{productID}";
        public static final String ID = "/product_detail/{productID}";
        public static final String SEARCH_PRODUCT = "/search";
        public static final String DELETE_MANY = "/delete-many";
        public static final String ID_PRODUCT = API_PREFIX_SHOP + "/item/{productTypeName}/{productID}";
    }

    public static final class Warehouse{
        public static final String BASE = API_PREFIX_ADMIN + "/warehouses";
        public static final String NEW = "/new";
        public static final String EDIT = "/warehouse_detail/{warehouseID}";
        public static final String DELETE = "/warehouse_detail/{warehouseID}";
        public static final String ID = "/warehouse_detail/{warehouseID}";
        public static final String SEARCH_WAREHOUSE = "/search";
        public static final String DELETE_MANY = "/delete-many";

        public static final String GET_WAREHOUSE = "/ware_product";
        public static final String ADD_PRODUCT = "/ware_product/new";
        public static final String EDIT_PRODUCT = "/ware_product/{productWarehouseID}";
        public static final String DELETE_PRODUCT = "/ware_product/{productWarehouseID}";
        public static final String ID_PRODUCT = "/ware_product/{productWarehouseID}";
    }

    public static final class City{
        public static final String BASE = API_PREFIX_ADMIN + "/cities";
        public static final String NEW = "/new";
        public static final String EDIT = "/{cityID}";
        public static final String DELETE = "/{cityID}";
    }


}
