package com.hello.java.test;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

/**
 * 모든 테스트가 하나의 인스턴스를 공유함
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {

    @FastTest
    @DisplayName("스터디 만들기")
    void create_new_study(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Study(-10));
        String message = exception.getMessage();
        assertEquals("limit은 0보다 커야 한다.", exception.getMessage());
        Study study = new Study(-10);

        /**
         * 깨지는 것을 한 번에 모두 확인할 수 있다.
         */
        assertAll(
                () -> assertNotNull(study),
                () -> assertEquals(StudyStatus.DRAFT, study.getStatus(),
                        () -> "스터디를 처음 만들면 " + StudyStatus.DRAFT  + " 상태다."),
                () -> assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은 0보다 커야 한다.")
        );
    }

    @DisplayName("반복 테스트")
    @RepeatedTest(value = 10, name = " {displayName}, {currentRepetition}/{totalRepetitions}")
    void repeatTest(RepetitionInfo repetitionInfo){
        System.out.println();
        System.out.println("test " + repetitionInfo.getCurrentRepetition() + " / " +repetitionInfo.getTotalRepetitions());
    }

    @DisplayName("파라미터 테스트")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(strings = {"날씨가", "많이", "추워지고", "있네요."})
    void parameterizedTest(String message){
        System.out.println(message);
    }

    @DisplayName("파라미터 테스트")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(ints = {10, 20, 40})
    @EmptySource
    @NullSource
    @NullAndEmptySource
    void parameterizedTest2(Integer limit){
        System.out.println(limit);
    }

    @DisplayName("파라미터 테스트")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(ints = {10, 20, 40})
    @EmptySource
    @NullSource
    @NullAndEmptySource
    void parameterizedTest3(@ConvertWith(StudyConverter.class) Study study){
        System.out.println(study.getLimit());
    }

    @DisplayName("파라미터 테스트")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바 스터디'", "2, 스프링"})
    void parameterizedTest4(Integer limit, String name){
        Study study = new Study(limit, name);
        System.out.println(study.getLimit());
    }

    @DisplayName("파라미터 테스트")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바 스터디'", "2, 스프링"})
    void parameterizedTest5(ArgumentsAccessor argumentsAccessor){

        Study study = new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
        System.out.println(study.getLimit());
    }

    @DisplayName("파라미터 테스트")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바 스터디'", "2, 스프링"})
    void parameterizedTest6(@AggregateWith(StudyAggregator.class) Study study){
        System.out.println(study.getLimit());
    }

    static class StudyAggregator implements ArgumentsAggregator{

        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            return new Study(accessor.getInteger(0), accessor.getString(1));
        }
    }

    static class StudyConverter extends SimpleArgumentConverter{

        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            assertEquals(Study.class, targetType, "Can only convert to Study");
            return new Study(Integer.parseInt(source.toString()));
        }
    }


    @SlowTest
    @Tag("slow")
    void create_new_study_assume(){
        String testEnv = System.getenv("TEST_ENV");

        /**
         * assumeTrue 값이 True일때만 아래 테스트를 실행
         */
        assumeTrue("LOCAL".equalsIgnoreCase(testEnv));

        assumingThat("LOCAL".equalsIgnoreCase(testEnv), () -> {

        });
        System.out.println(testEnv);

    }


    @Test
    @EnabledOnOs({OS.MAC, OS.LINUX})
    @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_9})
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "local")
    void create_new_study_assume_annotation(){
        String testEnv = System.getenv("TEST_ENV");
        System.out.println(testEnv);

    }

    /**
     *     @Disabled
     *     일단 작동 안함
     */
    @Test
    void create_new_study_time_wait(){
        assertTimeout(Duration.ofMillis(10), () -> {
            new Study(10);
            Thread.sleep(300);
        });
        System.out.println("StudyTest.create1");
    }


    /**
     *     @Disabled
     *     일단 작동 안함
     */
    @Test
    void create_new_study_time(){
        assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
            new Study(10);
            Thread.sleep(300);
        });
        // TODO ThreadLocal 상에서 에러가 생길 수 있다.
        // ex. @Transactional이 ThreadLocal을 활용하여 rollback을 하는데
        // rollback이 안되고 db에 반영될 수 있음
        // 사용에 주의
        System.out.println("StudyTest.create1");
    }



    /**
     * 반드시 static return void
     * private 안됨
     */
    @BeforeAll
    static void beforeAll(){
        System.out.println("StudyTest.beforeAll");
    }

    /**
     * 반드시 static return void
     * private 안됨
     */
    @AfterAll
    static void afterAll(){
        System.out.println("StudyTest.afterAll");
    }

    @BeforeEach
    void beforeEach(){
        System.out.println("StudyTest.beforeEach");
    }

    @AfterEach
    void afterEach(){
        System.out.println("StudyTest.afterEach");
    }

}