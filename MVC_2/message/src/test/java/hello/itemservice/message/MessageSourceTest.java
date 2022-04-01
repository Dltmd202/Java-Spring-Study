package hello.itemservice.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class MessageSourceTest {


    @Autowired MessageSource ms;

    @Test
    public void helloMessage() throws Exception{
        String result = ms.getMessage("hello", null, null);
        System.out.println(result);
        assertThat(result).isEqualTo("안녕");
    }

    @Test
    public void notFoundMessageCode() throws Exception{
        assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    public void notFoundMessageCodeDefualtMessage() throws Exception{
        String result = ms.getMessage("no_code", null, "기본 메시지", null);
        assertThat(result).isEqualTo("기본 메시지");
    }

    @Test
    public void argumentMessage() throws Exception {
        //given
        String message = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
        assertThat(message).isEqualTo("안녕 Spring");
    }

    @Test
    public void defaultLand() throws Exception {
        assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
        assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
    }

    @Test
    public void enLang() throws Exception {
        //given
        assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");

        //when

        //then
    }


}