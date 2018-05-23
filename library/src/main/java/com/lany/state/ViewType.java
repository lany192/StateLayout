package com.lany.state;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 视图类型
 */
@Retention(RetentionPolicy.SOURCE)
public @interface ViewType {
    /**
     * 内容视图
     */
    int CONTENT = 0;
    /**
     * 错误视图
     */
    int ERROR = 1;
    /**
     * 空视图
     */
    int EMPTY = 2;
    /**
     * 加载视图
     */
    int LOADING = 3;
    /**
     * 网络异常视图
     */
    int NETWORK = 4;
}