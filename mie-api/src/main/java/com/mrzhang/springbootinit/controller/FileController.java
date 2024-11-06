package com.mrzhang.springbootinit.controller;

import cn.hutool.core.io.FileUtil;
import com.mrzhang.mieapicommon.model.entity.User;
import com.mrzhang.springbootinit.common.BaseResponse;
import com.mrzhang.springbootinit.common.ErrorCode;
import com.mrzhang.springbootinit.common.ResultUtils;
import com.mrzhang.springbootinit.constant.FileConstant;
import com.mrzhang.springbootinit.exception.BusinessException;
import com.mrzhang.springbootinit.manager.CosManager;
import com.mrzhang.springbootinit.model.dto.file.UploadFileRequest;
import com.mrzhang.springbootinit.model.enums.FileUploadBizEnum;
import com.mrzhang.springbootinit.service.UserService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.mrzhang.springbootinit.utils.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口
 *
 * @author <a href="https://github.com/zhangxyl">程序员小阳</a>
 * @from
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    /**
     * 文件上传
     *
     * @param multipartFile
     * @param uploadFileRequest
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile multipartFile,
                                           UploadFileRequest uploadFileRequest, HttpServletRequest request) {
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        if (fileUploadBizEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        validFile(multipartFile, fileUploadBizEnum);
        User loginUser = userService.getLoginUser(request);
        // 文件目录：根据业务、用户来划分
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), loginUser.getId(), filename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
            cosManager.putObject(filepath, file);
            // 返回可访问地址
            return ResultUtils.success(FileConstant.COS_HOST + filepath);
        } catch (Exception e) {
            log.error("file upload error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }

    /**
     * 校验文件
     *
     * @param multipartFile
     * @param fileUploadBizEnum 业务类型
     */
    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024L;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
        }
    }

    @PostMapping("/downLoad")
    private void downLoad() {
        List<User> userList = new ArrayList<User>();
        for (int i = 0; i <= 2000000; i++) {
            User user = new User();
            user.setUserName("123");
            user.setUserAccount("123");
            user.setUserAvatar("123");
            user.setGender(1);
            user.setUserRole("123");
            user.setUserPassword("123");
            user.setAccessKey("123");
            user.setSecretKey("123");
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            userList.add(user);
        }
        List<String> head = Arrays.asList("姓名", "账号", "昵称", "性别", "角色", "密码", "as", "sk", "创建时间", "更新时间");
        try {
            ExcelUtil.downloadExcel(userList, "人员清单", "人员清单", null, head, null);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载失败：" + e.getMessage());
        }

    }
}
