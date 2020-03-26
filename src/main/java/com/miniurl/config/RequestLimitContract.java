package com.miniurl.config;
import com.miniurl.utils.RequestLimit;
import com.miniurl.utils.RequestLimitException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import com.miniurl.utils.RedisUtils;

@Aspect
@Component
public class RequestLimitContract {
    @Resource
    private RedisUtils redisUtils;
    private static final Logger logger = LoggerFactory.getLogger("requestLimitLogger");

    @Before("within(@org.springframework.web.bind.annotation.RestController *) && @annotation(limit)")
    public void requestLimit(final JoinPoint joinPoint , RequestLimit limit) throws RequestLimitException {
        //System.out.println("前提方法");
        try {
            Object[] args = joinPoint.getArgs();
            HttpServletRequest request = null;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof HttpServletRequest) {
                    request = (HttpServletRequest) args[i];
                    break;
                }
            }
            if (request == null) {
                throw new RequestLimitException("方法中缺失HttpServletRequest参数");
            }
            String ip = request.getLocalAddr();
            String url = request.getRequestURL().toString();
            String key = "req_limit_".concat(url).concat(ip);
            if (redisUtils.get(key) == null || redisUtils.get(key).equals("0")) {
                redisUtils.set(key, 1,limit.time());
            } else {
                redisUtils.incr(key, 1);
            }
            int count = Integer.parseInt(redisUtils.get(key).toString());
            if (count > limit.count()) {
                logger.info("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + limit.count() + "]");
                throw new RequestLimitException();
            }
        }catch (RequestLimitException e){
            throw e;
        }catch (Exception e){
            logger.error("限制访问发生异常",e);
        }
    }
}