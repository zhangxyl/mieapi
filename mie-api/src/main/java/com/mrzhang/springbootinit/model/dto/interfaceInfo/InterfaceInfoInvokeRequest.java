package com.mrzhang.springbootinit.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口调用请求
 *
 * @author <a href="https://github.com/zhangxyl">程序员小阳</a>
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户请求参数
     */
    private String userRequestParams;

    private static final long serialVersionUID = 1L;
}