package com.mrzhang.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mrzhang.mieapicommon.model.entity.InterfaceInfo;


/**
* @author MrZhang
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-10-11 16:39:58
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
