package cn.sipin.cloud.order.service.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.sipin.cloud.order.service.util.UserLoginUtil;
import cn.siyue.platform.httplog.aspect.LogAspect;

@Aspect
@Component
public class MemberServiceLogAspect extends LogAspect {
    @Autowired
    private UserLoginUtil userLoginUtil;

    @Override
    public Long getUserId() {
        return userLoginUtil.getUserId();
    }

    @Override
    public String getSystemName() {
        return "Sales Member service";
    }
}
