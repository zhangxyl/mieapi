package com.mrzhang.springbootinit.controller;

import com.mrzhang.mieapicommon.model.entity.User;
import com.mrzhang.springbootinit.common.BaseResponse;
import com.mrzhang.springbootinit.common.ErrorCode;
import com.mrzhang.springbootinit.common.ResultUtils;
import com.mrzhang.springbootinit.exception.BusinessException;
import com.mrzhang.springbootinit.model.dto.postthumb.PostThumbAddRequest;
import com.mrzhang.springbootinit.service.PostThumbService;
import com.mrzhang.springbootinit.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 帖子点赞接口
 *
 * @author <a href="https://github.com/zhangxyl">程序员小阳</a>
 * @from 
 */
@RestController
@RequestMapping("/post_thumb")
@Slf4j
public class PostThumbController {

    @Resource
    private PostThumbService postThumbService;

    @Resource
    private UserService userService;

    /**
     * 点赞 / 取消点赞
     *
     * @param postThumbAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doThumb(@RequestBody PostThumbAddRequest postThumbAddRequest,
                                         HttpServletRequest request) {
        if (postThumbAddRequest == null || postThumbAddRequest.getPostId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long postId = postThumbAddRequest.getPostId();
        int result = postThumbService.doPostThumb(postId, loginUser);
        return ResultUtils.success(result);
    }

}
