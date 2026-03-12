package cn.argento.askia.networks.miniFeign.beans;


import cn.argento.askia.networks.miniFeign.supports.Protocol;
import cn.argento.askia.networks.miniFeign.supports.http.HttpRequestMethod;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeclaredApiBean extends ApiBean {

    // 协议
    private Protocol protocol;
    // 请求主机
    private String host;
    // 请求端口
    private int port;
    // 请求Api
    private String api;
    // 请求方法
    private HttpRequestMethod method;
}