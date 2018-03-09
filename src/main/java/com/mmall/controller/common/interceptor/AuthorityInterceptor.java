package com.mmall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by fanlinglong on 2018/3/9.
 * Spring mvc 拦截器定义
 * 管理员权限验证以及是否登陆验证
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //解析HandlerMethod
        //获取请求方法名
        String methodName = handlerMethod.getMethod().getName();
        //请求Controller
        String className = handlerMethod.getBean().getClass().getSimpleName();
        //获取请求参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        //请求参数封装成String,记录到日志中
        StringBuffer stringBuffer = new StringBuffer();
        //迭代请求参数
        Iterator iterator = parameterMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String mapKey = (String) entry.getKey();
            String mapVaule = StringUtils.EMPTY;
            Object obj = entry.getValue();
            if (obj instanceof String[]){
                String [] strs = (String[]) obj;
                mapVaule = Arrays.toString(strs);
            }
            stringBuffer.append(mapKey).append("=").append(mapVaule);
        }
        //如果拦截到登陆方法,放行,防止进入死循环
        if (StringUtils.equals(className,"UserManagerController") && StringUtils.equals(methodName,"login")){
            log.info("权限拦截器拦截到请求,className:{},methodName:{}",className,methodName);
            return true;
        }
        log.info("权限拦截器拦截到请求,className:{},methodName:{},params:{}",className,methodName,stringBuffer.toString());
        User user = null;
        //获取token,查询redis验证是否登陆
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)){
            user = JsonUtil.String2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        }
        //如果为空或者不是管理员登陆
        if (user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN )){
            //这里必须重置返回,否则会报错
            response.reset();
            //设置返回编码格式
            response.setCharacterEncoding("UTF-8");
            //设置返回类型
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            if (user == null){
                //富文本上传,需要单独处理
                if (StringUtils.equals(className,"ProductManagerController")&&StringUtils.equals(methodName,"richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","请登陆管理员账号");
                    out.print(JsonUtil.obj2String(resultMap));
                }else {
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMsg("用户未登陆")));
                }
            }
            else{
                if (StringUtils.equals(className,"ProductManagerController")&&StringUtils.equals(methodName,"richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","权限不足,需要管理员权限");
                    out.print(JsonUtil.obj2String(resultMap));
                }else {
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMsg("用户无权限操作")));
                }
            }
            out.flush();
            out.close();
            //返回false,则不会进入到controller中
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
