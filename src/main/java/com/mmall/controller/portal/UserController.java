package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by fanlinglong on 2018/1/6.
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;
    /**
     * 用户登陆
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password,HttpSession session, HttpServletResponse httpServletResponse){
        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
            CookieUtil.writeLoginToken(httpServletResponse,session.getId());
            RedisPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()), Const.RedisCacheExTime.REDIS_SESSION_EXTIME);
        }
        return response;
    }

    /**
     * 注销登陆
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletResponse response, HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        CookieUtil.delLoginToken(request,response);
        RedisPoolUtil.del(loginToken);
        return  ServerResponse.createBySuccess();
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    /**
     * 校验用户名和邮箱
     * @param str 校验值
     * @param type 校验字段
     * @return
     */
    @RequestMapping(value = "checkValid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
        return  iUserService.checkValid(str,type);
    }

    /**
     * 获取用户登陆信息
     * @param request
     * @return
     */
    @RequestMapping(value = "getUserInfo.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse <User> getUserInfo(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if(user!=null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
    }

    /**
     * 忘记密码：根据用户名返回问题
     * @param username
     * @return
     */
    @RequestMapping(value = "forgetGetQuestion.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    /**
     * 忘记密码：校验问题，答案
     * @param username 用户名
     * @param question 忘记密码提示问题
     * @param answer   忘记密码答案
     * @return
     */
    @RequestMapping(value = "forgetCheckAnswer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    /**
     * 根据token重置密码
     * @param username  用户名
     * @param passwordNew 新密码
     * @param forgetToken token
     * @return
     */
    @RequestMapping(value = "forgetRsetPassword.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRsetPassword(String username,String passwordNew,String forgetToken){
        return iUserService.forgetRsetPassword(username,passwordNew,forgetToken);
    }

    /**
     * 登陆状态重置密码
     * @param request  request
     * @param passwordOld 旧密码
     * @param passwordNew 新密码
     * @return
     */
    @RequestMapping(value = "resetPassword.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpServletRequest request,String passwordOld,String passwordNew){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if(user==null){
            return ServerResponse.createByErrorMsg("用户未登陆,请重新登陆");
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    /**
     * 更新个人信息
     * @param request 用户request
     * @param user 用户信息
     * @return
     */
    @RequestMapping(value = "updateInfomation.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInfomation(HttpServletRequest request,User user){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User currentUser = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if(currentUser == null){
            return ServerResponse.createByErrorMsg("用户未登陆");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if(response.isSuccess()){
            response.getData().setUsername(currentUser.getUsername());
            RedisPoolUtil.setEx(loginToken,JsonUtil.obj2String(response.getData()), Const.RedisCacheExTime.REDIS_SESSION_EXTIME);
        }
        return response;
    }

}
