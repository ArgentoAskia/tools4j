package cn.argento.askia.networks.miniFeign.beans;

import cn.argento.askia.utilities.AnnotationUtility;
import cn.argento.askia.utilities.ReflectUtility;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;


public class ApiBeans {

    public List<ApiBean> analysisDeclaredApiInterface(Class<?> interfaceClass){
        if (interfaceClass.isInterface()){
            List<ApiBean> apiBeans = new ArrayList<>();
            // 先获取Class上的所有注解
            Annotation[] annotations = AnnotationUtility.getAnnotations(interfaceClass);
            String className = interfaceClass.getName();
            Set<Method> allMethods = ReflectUtility.getAllMethods(interfaceClass);
            for (Method m : allMethods) {
                ApiBean apiBean = new ApiBean();
                String methodName = m.getName();
                String id = className + "#" + methodName;
                apiBean.setId(id);
                apiBean.setDeclareMethod(m);
                apiBean.setDeclareAnnotationsOnClass(Arrays.asList(annotations));

                // method上的注解
                Annotation[] annotationsOnMethod = AnnotationUtility.getAnnotations(m);
                apiBean.setDeclareAnnotationOnMethod(Arrays.asList(annotationsOnMethod));

                Class<?> returnType = m.getReturnType();
                AnnotatedType annotatedReturnType = m.getAnnotatedReturnType();
                apiBean.setReturnType(annotatedReturnType.getType());
                apiBean.setReturnTypeClass(returnType);
                Annotation[] annotationsOnReturnType = annotatedReturnType.getAnnotations();
                apiBean.setDeclareAnnotationOnReturnType(Arrays.asList(annotationsOnReturnType));

                Parameter[] parameters = m.getParameters();
                Map<Parameter, List<Annotation>> declareAnnotationOnParams = new HashMap<>();
                for (Parameter parameter : parameters){
                    Annotation[] annotationsOnParameter = parameter.getAnnotations();
                    declareAnnotationOnParams.put(parameter, new ArrayList<>(Arrays.asList(annotationsOnParameter)));
                }
                apiBean.setDeclareAnnotationOnParams(declareAnnotationOnParams);

                // 异常
                AnnotatedType[] annotatedExceptionTypes = m.getAnnotatedExceptionTypes();
                Class<?>[] exceptionTypes = m.getExceptionTypes();
                List<Class<? extends Throwable>> collect = Arrays.stream(exceptionTypes).map((exceptionItem) -> (Class<Throwable>) exceptionItem).collect(Collectors.toList());
                apiBean.setExceptions(collect);
                Map<Type, List<Annotation>> typeListMap = new HashMap<>();
                for (AnnotatedType ex : annotatedExceptionTypes){
                    Type type = ex.getType();
                    Annotation[] annotationsOnException = ex.getAnnotations();
                    typeListMap.put(type, Arrays.asList(annotationsOnException));
                }
                apiBean.setDeclareAnnotationOnExceptions(typeListMap);

                apiBeans.add(apiBean);
            }

            return apiBeans;
        }
        throw new IllegalArgumentException("class" + interfaceClass + "must be an interface");
    }
}
