package cn.argento.askia.supports.environment;

/**
 * 环境信息Bean
 * @param <EB>
 */
public interface EnvironmentBean<EB extends EnvironmentBean<EB>>{

    EnvironmentBean<EB> getBean(EB environmentBean);
}
