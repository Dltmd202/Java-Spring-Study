package hello.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTemplate {

    public void execute(){
        long startTme = System.currentTimeMillis();
//        log.info("비즈니스 로직1 실행");
        call();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTme;
        log.info("resultTime={}", resultTime);
    }

    protected abstract void call();
}
