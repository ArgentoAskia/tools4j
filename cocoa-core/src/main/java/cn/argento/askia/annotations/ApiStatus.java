package cn.argento.askia.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Api状态管理注解.
 *
 * <p>该类提供了基于{@code org.jetbrains.annotations.ApiStatus}扩展的管理状态注解</p>
 *
 * <b>当前包并不提供此注解的处理器</b>
 *
 * @author Askia
 */
public class ApiStatus {

    /**
     * 内部Api.
     *
     * <p>仅在项目内部使用，作为公共API对外暴露。未来版本可能随时更改或删除，不推荐任何外部代码依赖。</p>
     */
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Internal {
        /**
         * API的设计或主要维护者。在企业中，这有助于快速定位问题的负责人
         * @return 作者列表
         */
        String[] author() default {};

        /**
         * <b>强烈推荐。</b>明确API元素是从哪个版本开始引入的，帮助使用者判断其项目是否需要升级才能依赖此功能
         * @return 版本信息
         */
        String version() default "";

        /**
         * 一段简短的文本，用于解释此API为何处于当前状态。例如，对于实验性API，可以注明其设计目标和已知限制。
         *
         * @return 描述信息
         */
        String description() default "";

        /**
         *
         * @return 更新时间
         */
        String updateTime() default "";

        /**
         *
         * @return 是否仅用于测试
         */
        boolean forTestOnly() default false;
    }

    /**
     * 实验性Api.
     *
     * <p>正在积极开发、征集反馈的阶段，可能会被重新设计或完全抛弃。可以尝鲜使用，但不建议用于生产环境</p>
     */
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Experimental{
        /**
         * API的设计或主要维护者。在企业中，这有助于快速定位问题的负责人
         * @return 作者列表
         */
        String[] author() default {};

        /**
         * <b>强烈推荐。</b>明确API元素是从哪个版本开始引入的，帮助使用者判断其项目是否需要升级才能依赖此功能
         * @return 版本信息
         */
        String version() default "";

        /**
         * 一段简短的文本，用于解释此API为何处于当前状态。例如，对于实验性API，可以注明其设计目标和已知限制。
         *
         * @return 描述信息
         */
        String description() default "";

        /**
         *
         * @return 更新时间
         */
        String updateTime() default "";

        /**
         * 特别适用于 @Experimental 状态的API。提供一个链接（如GitHub Issue、邮件列表），鼓励早期使用者提交问题或建议
         *
         * @return 反馈渠道
         */
        String feedback() default "";

    }

    /**
     * 新Api
     *
     * 特指一个刚刚发布的全新API，可能需要更多的实际使用验证
     */
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface New{
        /**
         * API的设计或主要维护者。在企业中，这有助于快速定位问题的负责人
         * @return 作者列表
         */
        String[] author() default {};

        /**
         * <b>强烈推荐。</b>明确API元素是从哪个版本开始引入的，帮助使用者判断其项目是否需要升级才能依赖此功能
         * @return 版本信息
         */
        String version();

        /**
         * 一段简短的文本，用于解释此API为何处于当前状态。例如，对于实验性API，可以注明其设计目标和已知限制。
         *
         * @return 描述信息
         */
        String description() default "";

        /**
         *
         * @return 更新时间
         */
        String updateTime() default "";
    }

    /**
     * 维护中的Api
     * 保证在当前大版本中不会有破坏性变更，但未来不确定
     */
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Maintained{
        /**
         * API的设计或主要维护者。在企业中，这有助于快速定位问题的负责人
         * @return 作者列表
         */
        String[] author() default {};

        /**
         * <b>强烈推荐。</b>明确API元素是从哪个版本开始引入的，帮助使用者判断其项目是否需要升级才能依赖此功能
         * @return 版本信息
         */
        String version() default "";

        /**
         * 自从那个版本开始,此API进入维护状态
         * @return 版本
         */
        String since();

        /**
         * 一段简短的文本，用于解释此API为何处于当前状态。例如，对于实验性API，可以注明其设计目标和已知限制。
         *
         * @return 描述信息
         */
        String description() default "";

        /**
         *
         * @return 更新时间
         */
        String updateTime() default "";
    }

    /**
     * 演进中的Api
     * 介乎实验和稳定之间，经过多个大版本验证基本通过，但在部分小版本中可能存在误差
     */
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Evolving{
        /**
         * API的设计或主要维护者。在企业中，这有助于快速定位问题的负责人
         * @return 作者列表
         */
        String[] author() default {};

        /**
         * <b>强烈推荐。</b>明确API元素是从哪个版本开始引入的，帮助使用者判断其项目是否需要升级才能依赖此功能
         * @return 版本信息
         */
        String version() default "";

        /**
         * 一段简短的文本，用于解释此API为何处于当前状态。例如，对于实验性API，可以注明其设计目标和已知限制。
         *
         * @return 描述信息
         */
        String description() default "";

        /**
         *
         * @return 更新时间
         */
        String updateTime() default "";
    }

    /**
     * 稳定Api
     *
     * 经过充分测试，可安全用于生产环境。后续的任何变更都将严格遵守向后兼容原则
     */
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Stable{
        /**
         * API的设计或主要维护者。在企业中，这有助于快速定位问题的负责人
         * @return 作者列表
         */
        String[] author() default {};

        /**
         * <b>强烈推荐。</b>明确API元素是从哪个版本开始引入的，帮助使用者判断其项目是否需要升级才能依赖此功能
         * @return 版本信息
         */
        String version() default "";

        /**
         * 一段简短的文本，用于解释此API为何处于当前状态。例如，对于实验性API，可以注明其设计目标和已知限制。
         *
         * @return 描述信息
         */
        String description() default "";

        /**
         *
         * @return 更新时间
         */
        String updateTime() default "";
    }

    /**
     * 不再推荐使用的Api
     *
     * 此Api仍然可用，并且仍然部分功能暂且无法被替代，但Api开发者出于性能等各种原因不再推荐你使用，该Api在后续的版本中仍然会被保留或转为废弃，
     */
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface NotRecommended{
        /**
         * API的设计或主要维护者。在企业中，这有助于快速定位问题的负责人
         * @return 作者列表
         */
        String[] author() default {};

        /**
         * <b>强烈推荐。</b>明确API元素是从哪个版本开始引入的，帮助使用者判断其项目是否需要升级才能依赖此功能
         * @return 版本信息
         */
        String version() default "";

        /**
         * 一段简短的文本，用于解释此API为何处于当前状态。例如，对于实验性API，可以注明其设计目标和已知限制。
         *
         * @return 描述信息
         */
        String description() default "";

        /**
         *
         * @return 更新时间
         */
        String updateTime() default "";

        /**
         *
         * @return 不推荐的原因
         */
        String reason();
    }

    /**
     * 废弃的Api
     *
     * 不再推荐使用，因为有更好、更高效的替代方案。它会在文档中被标记，但仍会存在一个“弃用期”，让开发者有时间迁移代码
     */
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Deprecated{

        /**
         * 被哪个API替换了
         * @return api名称
         */
        String replacedBy();
        /**
         * API的设计或主要维护者。在企业中，这有助于快速定位问题的负责人
         * @return 作者列表
         */
        String[] author() default {};

        /**
         * <b>强烈推荐。</b>明确API元素是从哪个版本开始引入的，帮助使用者判断其项目是否需要升级才能依赖此功能
         * @return 版本信息
         */
        String version() default "";

        /**
         * 一段简短的文本，用于解释此API为何处于当前状态。例如，对于实验性API，可以注明其设计目标和已知限制。
         *
         * @return 描述信息
         */
        String description() default "";

        /**
         *
         * @return 更新时间
         */
        String updateTime() default "";
    }

    /**
     * 待移除的Api
     * 是弃用状态的一个更强的子集，明确表示该API已在计划中，会在指定的未来版本中被彻底删除。
     */
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ScheduledForRemoval{
        /**
         * 计划移除的版本
         * @return 计划移除的版本
         */
        String forRemoval();

        /**
         * API的设计或主要维护者。在企业中，这有助于快速定位问题的负责人
         * @return 作者列表
         */
        String[] author() default {};

        /**
         * <b>强烈推荐。</b>明确API元素是从哪个版本开始引入的，帮助使用者判断其项目是否需要升级才能依赖此功能
         * @return 版本信息
         */
        String version() default "";

        /**
         * 一段简短的文本，用于解释此API为何处于当前状态。例如，对于实验性API，可以注明其设计目标和已知限制。
         *
         * @return 描述信息
         */
        String description() default "";

        /**
         *
         * @return 更新时间
         */
        String updateTime() default "";
    }

    /**
     * 已移除的Api
     * API已被彻底删除，仅用于文档记录，提醒开发者该功能已不存在
     */
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Removed{
        /**
         * API的设计或主要维护者。在企业中，这有助于快速定位问题的负责人
         * @return 作者列表
         */
        String[] author() default {};

        /**
         * <b>强烈推荐。</b>明确API元素是从哪个版本开始引入的，帮助使用者判断其项目是否需要升级才能依赖此功能
         * @return 版本信息
         */
        String since();

        /**
         * 一段简短的文本，用于解释此API为何处于当前状态。例如，对于实验性API，可以注明其设计目标和已知限制。
         *
         * @return 描述信息
         */
        String description() default "";

        /**
         *
         * @return 更新时间
         */
        String updateTime() default "";

    }

    /**
     * Spi专属
     * todo 此注解需要重新考虑属性
     *
     * 特别用于标记服务提供者接口（Service Provider Interface），提示开发者“只应实现，不应直接调用”
     */
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface OverrideOnly{
        /**
         * API的设计或主要维护者。在企业中，这有助于快速定位问题的负责人
         * @return 作者列表
         */
        String[] author() default {};

        /**
         * <b>强烈推荐。</b>明确API元素是从哪个版本开始引入的，帮助使用者判断其项目是否需要升级才能依赖此功能
         * @return 版本信息
         */
        String since();

        /**
         * 一段简短的文本，用于解释此API为何处于当前状态。例如，对于实验性API，可以注明其设计目标和已知限制。
         *
         * @return 描述信息
         */
        String description() default "";

        /**
         *
         * @return 更新时间
         */
        String updateTime() default "";
    }
}
