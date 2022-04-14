package hello.typeconverter.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 127.0.0.1:8080
 */
@Getter
@EqualsAndHashCode
public class IpPort {

    private String ip;
    private int port;

    public IpPort(String id, int port) {
        this.ip = id;
        this.port = port;
    }
}
