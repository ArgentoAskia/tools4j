package cn.argento.askia.networks.miniFeign.beans;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeclaredApiBean extends ApiBean {

    private String host;
    private int port;
}