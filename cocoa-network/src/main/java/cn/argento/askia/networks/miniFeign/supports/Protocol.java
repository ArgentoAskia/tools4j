package cn.argento.askia.networks.miniFeign.supports;

import lombok.Getter;

/**
 * 传输协议相关
 *
 * @author Askia
 */
@Getter
public enum Protocol {
    HTTP("http"), HTTPS("https");

    private String protocol;

    Protocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return protocol + "://";
    }
}
