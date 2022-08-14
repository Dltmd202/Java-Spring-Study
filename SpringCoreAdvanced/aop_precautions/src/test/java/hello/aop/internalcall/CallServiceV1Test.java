package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(CallLogAspect.class)
class CallServiceV1Test {

    @Autowired CallServiceV1 callServiceV1;

    @Test
    void external() {
        callServiceV1.external();
    }
}