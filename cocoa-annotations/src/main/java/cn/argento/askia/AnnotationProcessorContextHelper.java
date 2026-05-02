package cn.argento.askia;


import cn.argento.askia.annotations.ApiStatus;
import cn.argento.askia.context.AnnotationProcessorContext;
import cn.argento.askia.context.BeanNotFoundException;
import cn.argento.askia.context.MoreThenOneBeanException;
import cn.argento.askia.context.MutableAnnotationProcessorContext;
import cn.argento.askia.utilities.lang.StringUtility;


@ApiStatus.Internal
class AnnotationProcessorContextHelper {

    /**
     * 将 {@link AnnotationProcessorContext} 转为 {@link MutableAnnotationProcessorContext}
     * @param context 实现了 {@link MutableAnnotationProcessorContext} 的子实现
     * @return {@link MutableAnnotationProcessorContext} 对象, 转换失败将返回 {@code null}
     */
    static MutableAnnotationProcessorContext getMutableContext(AnnotationProcessorContext context){
        if (context instanceof MutableAnnotationProcessorContext){
            return (MutableAnnotationProcessorContext) context;
        }
        return null;
    }

    /**
     * 先按照类型，再按照值来找Bean对象
     * @param tClass
     * @param beanName
     * @param context
     * @return
     * @throws BeanNotFoundException
     */
    static Object findBean(Class<?> tClass, String beanName, AnnotationProcessorContext context) throws BeanNotFoundException {
        try {
            Object beanByType = context.getBeanByType(tClass);
            if (beanByType == null){
                beanByType = context.getBeanByInheritType(tClass);
            }
            if (beanByType == null){
                throw new BeanNotFoundException("找不到类型为" + tClass + "的Bean, 开启按照名字查找");
            }
            return beanByType;
        }
        catch (MoreThenOneBeanException | BeanNotFoundException e) {
            Object beanByName = null;
            if (StringUtility.isBlank(beanName)){
                throw new IllegalArgumentException("beanName不能为空, 因为容器按照" + tClass + "无法找到");
            }
            else{
                beanByName = context.getBeanByName(beanName);
                if (beanByName == null){
                    // 必须要有但是找不到
                    throw new BeanNotFoundException("找不到名字为" + beanName + "的Bean, 因此抛出异常", e);
                }
                return beanByName;
            }
        }
    }
}
