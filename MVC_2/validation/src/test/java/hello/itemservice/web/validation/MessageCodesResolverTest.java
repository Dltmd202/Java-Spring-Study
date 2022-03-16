package hello.itemservice.web.validation;

import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import static org.assertj.core.api.Assertions.assertThat;

class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    public void MessageCodesResolverObject() throws Exception {
        //given
        String[] messagesCodes = codesResolver.resolveMessageCodes("required", "item");
        for (String messagesCode : messagesCodes) {
            System.out.println(messagesCode);
        }

        assertThat(messagesCodes).containsExactly("required.item", "required");
        //when

        //then
    }

    @Test
    public void messageCodesResolverField() throws Exception {
        //given
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }

        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required"
        );
        //when

        //then
    }

}