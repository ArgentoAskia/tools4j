package cn.argento.askia.networks.miniFeign.beans;

import lombok.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class ApiBean {
    //  常量
    // 唯一ID
    private String id;
    // 声明的方法
    private Method declareMethod;
    // 声明的类
    private Class<?> declareClass;
    // 类上发现的注解
    private List<Annotation> declareAnnotationsOnClass;
    // 直接标记在方法上的注解
    // 标记位置 --> 注解列表
    private List<Annotation> declareAnnotationOnMethod;

    // 各个位置的注解
    // 标记在返回值上的注解
    private Type returnType;        // 返回值类型
    private Class<?> returnTypeClass;
    private List<Annotation> declareAnnotationOnReturnType;

    // 标记在方法参数上的注解
    private Map<Parameter, List<Annotation>> declareAnnotationOnParams;

    // 标记在异常上的注解
    private List<Class<? extends Throwable>> exceptions;
    private Map<Type, List<Annotation>> declareAnnotationOnExceptions;

    // copy Constructor
    protected ApiBean(ApiBean apiBean){
        this.declareAnnotationOnExceptions = apiBean.declareAnnotationOnExceptions;
        this.declareAnnotationOnMethod = apiBean.declareAnnotationOnMethod;
        this.declareAnnotationOnParams = apiBean.declareAnnotationOnParams;
        this.declareAnnotationOnReturnType = apiBean.declareAnnotationOnReturnType;
        this.declareAnnotationsOnClass = apiBean.declareAnnotationsOnClass;
        this.declareClass = apiBean.declareClass;
        this.declareMethod = apiBean.declareMethod;
        this.id = apiBean.id;
    }
}
