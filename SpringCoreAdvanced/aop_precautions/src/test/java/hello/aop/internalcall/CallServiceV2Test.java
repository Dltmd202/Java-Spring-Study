package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(CallLogAspect.class)
class CallServiceV2Test {

    @Autowired CallServiceV2 callServiceV2;

    @Test
    void external() {
        callServiceV2.external();
    }
}