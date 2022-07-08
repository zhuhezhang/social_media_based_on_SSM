package pers.zhz.interceptor;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import pers.zhz.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户拦截器
 */
public class UserInterceptor implements HandlerInterceptor {

    private static final Logger logger = Logger.getLogger(UserInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        Object session = request.getSession().getAttribute("loginUserId");//通过session验证用户是否登录
        if (session == null) {
            response.sendError(403);
            logger.warn("用户（IP地址：" + Utils.getIpAddress(request) + "）" + "未持有session访问" + requestURI + "路径被拒");
            return false;
        } else {
            logger.info("用户（IP地址：" + Utils.getIpAddress(request) + "；用户账号：" + session + "）" + "访问" + requestURI + "成功通过拦截器");
            return true;
        }
    }
}