package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {

    @Test
    public void stringToInteger() throws Exception {
        //given
        StringToIntegerConverter converter = new StringToIntegerConverter();

        //when
        Integer result = converter.convert("10");

        //then
        assertThat(result).isEqualTo(10);
    }

    @Test
    public void intergerToString() throws Exception {
        //given
        IntegerToStringConverter converter = new IntegerToStringConverter();

        //when
        String result = converter.convert(10);

        //then
        assertThat(result).isEqualTo("10");
    }

    @Test
    public void stringToIpPort() throws Exception {
        //given
        IpPortToStringConverter converter = new IpPortToStringConverter();

        //when
        IpPort source = new IpPort("127.0.0.1", 8080);
        String result = converter.convert(source);

        //then
        assertThat(result).isEqualTo("127.0.0.1:8080");
    }

    @Test
    public void ipPortToString() throws Exception {
        //given
        StringToIpPortConverter converter = new StringToIpPortConverter();

        //when
        String source = "127.0.0.1:8080";
        IpPort result = converter.convert(source);

        //then
        assertThat(result).isEqualTo(new IpPort("127.0.0.1", 8080));
    }

}