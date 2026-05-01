package cn.argento.askia.supports;

/**
 * 注解处理器处理阶段周期
 *
 * @author Askia
 * @since 2026.4.26
 */
public enum ProcessingPhase {
    /**
     * 对应阶段3：注解处理（注解融合、特殊注解、顺序处理等）
     */
    ANNOTATION_PROCESSING,

    /**
     * 对应阶段4：核心处理过程（结合上下文）
     */
    CORE_PROCESSING,

    /**
     * 对应阶段5：后置处理（处理上下文、返回值等）
     */
    POST_PROCESSING
}
