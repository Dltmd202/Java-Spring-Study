package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV2 {

//    private final ApplicationContext context;
    private final ObjectProvider<CallServiceV2> callServiceV2ObjectProvider;

    public CallServiceV2(ObjectProvider<CallServiceV2> callServiceV2ObjectProvider) {
        this.callServiceV2ObjectProvider = callServiceV2ObjectProvider;
    }

    public void external(){
        log.info("call external");
//        CallServiceV2 call = context.getBean(CallServiceV2.class);
        CallServiceV2 call = callServiceV2ObjectProvider.getObject();
        call.internal();
    }

    public void internal() {
        log.info("call internal");
    }
}
