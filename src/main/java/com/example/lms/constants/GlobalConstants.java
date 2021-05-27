package com.example.lms.constants;

import org.hibernate.validator.constraints.Range;

/**
 * @author wza
 * @date 5/19/21
 * @description 全局常用变量
 */
public class GlobalConstants {
    private GlobalConstants() {

    }

    /**
     * ip地址分隔符
     * 127.0.0.1
     */
    public static  final String IP_SEPARATOR = ".";

    /**
     * logId对应的key
     */
    public static final String SESSION_TOKEN_ID = "sessionTokenId";

    /**
     *
     */
    public static final  String PROJECT_CODE = "Data::Business";

    /***
     *
     */
    public static final String UNDERLINE_SEPARATOR = "_";

    /**
     * 失效
     */
    public static final Integer VALID = 1;

    /**
     * 生效
     */
    public static final Integer DISABLE = 2;

    public static final Integer NULL_USER_ID = -1;

    public static final Integer NULL_ADMIN_ID = -1;

    public static final Integer BOOK_COUNT = 1;

    public static final Integer OPERATION_SUCCESS = 1;

    public static final Integer OPERATION_FAILED = 2;

    public static final Integer NULL_BOOK_ID = -1;

    public static final Integer NULL_CATEGORY_ID = -1;

    public static final String ADMIN_KEY_PREFIX = "adminId::";

    public static final String BOOK_CATEGORY_KEY_PREFIX = "bookCategoryId::";

    public static final String BOOK_KEY_PREFIX = "bookId::";

    public static final String LEND_KEY_PREFIX = "lendId::";

    public static final String RETURN_KEY_PREFIX = "returnId::";

    public static final String USER_KEY_PREFIX = "userId::";

}
