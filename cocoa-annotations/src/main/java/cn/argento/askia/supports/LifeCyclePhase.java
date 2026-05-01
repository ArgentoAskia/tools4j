package cn.argento.askia.supports;

import cn.argento.askia.annotations.AnnotationProcessor;
import cn.argento.askia.annotations.phase.*;
import cn.argento.askia.utilities.collection.ArrayUtility;
import cn.argento.askia.utilities.collection.CollectionUtility;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * 注解处理器生命周期枚举
 *
 * @author Askia
 * @since 2026.4.26
 */
@SuppressWarnings("all")
public enum LifeCyclePhase {
    LAST_PHASE(null){
        @Override
        public Class<? extends Annotation> getPhaseAnnotation() {
            return currentPhase.phaseAnnotation;
        }

        @Override
        public Annotation asPhaseAnnotation(Annotation annotation) {
            return currentPhase.asPhaseAnnotation(annotation);
        }
    },

    /**
     * 阶段1：扫描阶段
     */
    SCANNING(ScanPhase.class){
        @Override
        public Class<ScanPhase> getPhaseAnnotation() {
            return (Class<ScanPhase>) phaseAnnotation;
        }
        @Override
        public ScanPhase asPhaseAnnotation(Annotation annotation) {
            if (annotation == null){
                return null;
            }
            if (annotation.annotationType() == ScanPhase.class){
                return (ScanPhase) annotation;
            }
            return null;
        }
    },

    /**
     * 阶段2：检查阶段
     */
    CHECKING(CheckPhase.class){
        @Override
        public Class<CheckPhase> getPhaseAnnotation() {
            return (Class<CheckPhase>) phaseAnnotation;
        }

        @Override
        public CheckPhase asPhaseAnnotation(Annotation annotation) {
            if (annotation == null){
                return null;
            }
            if (annotation.annotationType() == CheckPhase.class){
                return (CheckPhase) annotation;
            }
            return null;
        }
    },

    /**
     * 对应阶段3：注解处理（注解融合、特殊注解、顺序处理等）
     */
    ANNOTATION_PROCESSING(AnnotationProcessingPhase.class){
        @Override
        public Class<AnnotationProcessingPhase> getPhaseAnnotation() {
            return (Class<AnnotationProcessingPhase>) phaseAnnotation;
        }

        @Override
        public AnnotationProcessingPhase asPhaseAnnotation(Annotation annotation) {
            if (annotation == null){
                return null;
            }
            if (annotation.annotationType() == AnnotationProcessingPhase.class){
                return (AnnotationProcessingPhase) annotation;
            }
            return null;
        }
    },

    /**
     * 对应阶段4：核心处理过程（结合上下文）
     */
    MAIN_PROCESSING(MainProcessingPhase.class){
        @Override
        public Class<MainProcessingPhase> getPhaseAnnotation() {
            return (Class<MainProcessingPhase>) phaseAnnotation;
        }

        @Override
        public MainProcessingPhase asPhaseAnnotation(Annotation annotation) {
            if (annotation == null){
                return null;
            }
            if (annotation.annotationType() == MainProcessingPhase.class){
                return (MainProcessingPhase) annotation;
            }
            return null;
        }
    },

    /**
     * 对应阶段5：后置处理（处理上下文、返回值等）
     */
    POST_PROCESSING(PostProcessingPhase.class){
        @Override
        public Class<PostProcessingPhase> getPhaseAnnotation() {
            return (Class<PostProcessingPhase>) phaseAnnotation;
        }

        @Override
        public PostProcessingPhase asPhaseAnnotation(Annotation annotation) {
            if (annotation == null){
                return null;
            }
            if (annotation.annotationType() == PostProcessingPhase.class){
                return (PostProcessingPhase) annotation;
            }
            return null;
        }
    },

    /**
     * 阶段6：完成阶段
     */
    FINISHING(CompletionPhase.class){
        @Override
        public Class<CompletionPhase> getPhaseAnnotation() {
            return (Class<CompletionPhase>) phaseAnnotation;
        }

        @Override
        public CompletionPhase asPhaseAnnotation(Annotation annotation) {
            if (annotation == null){
                return null;
            }
            if (annotation.annotationType() == CompletionPhase.class){
                return (CompletionPhase) annotation;
            }
            return null;
        }
    };

    LifeCyclePhase(Class<? extends Annotation> phaseAnnotation){
        this.phaseAnnotation = phaseAnnotation;
    }

    // 阶段绑定的注解
    protected Class<? extends Annotation> phaseAnnotation;

    /**
     * 获取阶段常量对应的注解 {@link Class} 实例
     * @return {@link Class} 实例
     */
    public abstract <T extends Annotation> Class<T> getPhaseAnnotation();

    /**
     * 视图方法, 尝试将注解转为 {@code Phase} 注解
     * @param annotation 任何注解
     * @return {@code Phase} 注解, 如果无法转换则返回 {@code null}
     */
    public abstract <T extends Annotation> T asPhaseAnnotation(Annotation annotation);


    // 静态方法
    public static Annotation[] filterPhase(Annotation[] annotations){
        if (annotations == null){
            return null;
        }
        List<Annotation> phaseList = new ArrayList<>();
        final LifeCyclePhase[] values = values();
        // 遍历外层提供的注解
        for (Annotation annotation : annotations){
            // 遍历内层LifeCyclePhase注解
            for (LifeCyclePhase lifeCyclePhase : values){
                if (lifeCyclePhase.getPhaseAnnotation() == annotation.annotationType()){
                    phaseList.add(annotation);
                }
            }
        }
        return phaseList.toArray(new Annotation[0]);
    }

    private static LifeCyclePhase currentPhase = LifeCyclePhase.SCANNING;

    public static void setCurrentPhase(LifeCyclePhase lifeCyclePhase){
        currentPhase = lifeCyclePhase;
    }
}
