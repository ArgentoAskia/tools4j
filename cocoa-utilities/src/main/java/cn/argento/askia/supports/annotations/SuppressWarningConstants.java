package cn.argento.askia.supports.annotations;

/**
 * 该枚举类列举了常用IDE的SuppressWarnings选项！
 *
 */
@SuppressWarnings("unused")
public final class SuppressWarningConstants {
    /*
        oracle javac 1.8.0_311 support
     */
    public static final String UNCHECKED = "unchecked";
    public static final String ALL = "all";
    public static final String DEPRECATION = "deprecation";
    public static final String AUXILIARY_CLASS = "auxiliaryclass";
    public static final String CAST = "cast";
    public static final String CLASSFILE = "classfile";
    public static final String DEP_ANN = "dep-ann";
    public static final String DIV_ZERO = "divzero";
    public static final String EMPTY = "empty";
    public static final String FALLTHROUGH = "fallthrough";
    public static final String FINALLY = "finally";
    public static final String OPTIONS = "options";
    public static final String OVERLOADS = "overloads";
    public static final String OVERRIDES = "overrides";
    public static final String PATH = "path";
    public static final String PROCESSING = "processing";
    public static final String RAW_TYPES = "rawtypes";
    public static final String SERIAL = "serial";
    public static final String STATIC = "static";
    public static final String TRY = "try";
    public static final String VARARGS = "varargs";

    /**
     * eclipse support:
     * https://help.eclipse.org/latest/index.jsp?topic=/org.eclipse.jdt.doc.user/tasks/task-suppress_warnings.htm
     */
    public static final String BOXING = "boxing";
    public static final String HIDING = "hiding";
    public static final String INCOMPLETE_SWITCH = "incomplete-switch";
    public static final String JAVADOC = "javadoc";
    public static final String NLS = "nls";
    public static final String NULL = "null";
    public static final String RESOURCE = "resource";
    public static final String RESTRICTION = "restriction";
    public static final String REMOVAL = "removal";
    public static final String STATIC_ACCESS = "static-access";
    public static final String STATIC_METHOD = "static-method";
    public static final String SUPER = "super";
    public static final String SYNTHETIC_ACCESS = "synthetic-access";
    public static final String SYNC_OVERRIDE = "sync-override";
    public static final String UNQUALIFIED_FIELD_ACCESS = "unqualified-field-access";
    public static final String UNLIKELY_ARG_TYPE = "unlikely-arg-type";
    public static final String UNUSED = "unused";


    /**
     * IntelliJ idea support
     * https://gist.github.com/vegaasen/157fbc6dce8545b7f12c
     */
    public static final String MAGIC_CHARACTER = "MagicCharacter";
    public static final String MAGIC_NUMBER  = "MagicNumber";
    public static final String COMPARATOR_METHOD_PARAMETER_NOT_USED  = "ComparatorMethodParameterNotUsed";
    public static final String JDBC_PREPARESTATEMENT_WITH_NONCONSTANT_STRING  = "JDBCPrepareStatementWithNonConstantString";
    public static final String ITERATOR_HAS_NEXT_CALLS_ITERATOR_NEXT  = "IteratorHasNextCallsIteratorNext";
    public static final String ITERATOR_NEXT_CAN_NOT_THROW_NO_SUCH_ELEMENT_EXCEPTION  = "IteratorNextCanNotThrowNoSuchElementException";
    public static final String JDBC_EXECUTE_WITH_NON_CONSTANT_STRING  = "JDBCExecuteWithNonConstantString";
    public static final String STRING_EQUALS_EMPTY_STRING  = "StringEqualsEmptyString";
    // JDK 5.0 only
    public static final String STRINGBUFFER_MAYBE_STRINGBUILDER  = "StringBufferMayBeStringBuilder";
    public static final String STRING_BUFFER_TO_STRING_IN_CONCATENATION  = "StringBufferToStringInConcatenation";
    public static final String ASSERT_STATEMENT  = "AssertStatement";
    public static final String ASSERT_EQUALS_BETWEEN_INCONVERTIBLE_TYPES  = "AssertEqualsBetweenInconvertibleTypes";
    public static final String AWAIT_NOT_IN_LOOP  = "AwaitNotInLoop";
    public static final String AWAIT_WITHOUT_CORRESPONDING_SIGNAL  = "AwaitWithoutCorrespondingSignal";
    public static final String BREAK_STATEMENT  = "BreakStatement";
    public static final String BREAK_STATEMENT_WITH_LABEL  = "BreakStatementWithLabel";
    public static final String CATCH_GENERIC_CLASS  = "CatchGenericClass";
    public static final String CLONE_DOESNT_CALL_SUPER_CLONE  = "CloneDoesntCallSuperClone";
    public static final String CLONE_DOESNT_DECLARE_CLONE_NOT_SUPPORTED_EXCEPTION  = "CloneDoesntDeclareCloneNotSupportedException";
    public static final String CLONE_CALLS_CONSTRUCTORS  = "CloneCallsConstructors";
    public static final String CLONE_IN_NON_CLONEABLE_CLASS  = "CloneInNonCloneableClass";
    public static final String MISSPELLED_COMPARE_TO  = "MisspelledCompareTo";
    public static final String CONTINUE_OR_BREAK_FROM_FINALLY_BLOCK  = "ContinueOrBreakFromFinallyBlock";
    public static final String CONTINUE_STATEMENT  = "ContinueStatement";
    public static final String CONTINUE_STATEMENT_WITH_LABEL  = "ContinueStatementWithLabel";
    public static final String DEFAULT_NOT_LAST_CASE_IN_SWITCH  = "DefaultNotLastCaseInSwitch";
    public static final String MISSPELLED_EQUALS  = "MisspelledEquals";
    public static final String EQUALS_BETWEEN_INCONVERTIBLE_TYPES  = "EqualsBetweenInconvertibleTypes";
    public static final String ARRAY_EQUALS  = "ArrayEquals";
    public static final String BIG_DECIMAL_EQUALS  = "BigDecimalEquals";
    public static final String EQUALS_WHICH_DOESNT_CHECK_PARAMETER_CLASS  = "EqualsWhichDoesntCheckParameterClass";
    public static final String FINAL_CLASS  = "FinalClass";
    public static final String FINAL_METHOD  = "FinalMethod";
    public static final String FINAL_METHOD_IN_FINAL_CLASS  = "FinalMethodInFinalClass";
    public static final String FINALIZE_CALLED_EXPLICITLY  = "FinalizeCalledExplicitly";
    public static final String FINALIZE_DECLARATION  = "FinalizeDeclaration";
    public static final String FINALIZE_DOESNT_CALL_SUPER_FINALIZE  = "FinalizeDoesntCallSuperFinalize";
    public static final String FINALIZE_NOT_PROTECTED  = "FinalizeNotProtected";
    public static final String FOR_LOOP_REPLACEABLE_BY_WHILE  = "ForLoopReplaceableByWhile";
    public static final String FOR_LOOP_REPLACEABLE_BY_FOR_EACH  = "ForLoopReplaceableByForEach";
    public static final String FOR_LOOP_THAT_DOESNT_USE_LOOP_VARIABLE  = "ForLoopThatDoesntUseLoopVariable";
    public static final String FOR_LOOP_WITH_MISSING_COMPONENT  = "ForLoopWithMissingComponent";
    public static final String MISSPELLED_HASHCODE  = "MisspelledHashcode";
    public static final String IF_STATEMENT_WITH_IDENTICAL_BRANCHES  = "IfStatementWithIdenticalBranches";
    public static final String IF_STATEMENT_WITH_NEGATED_CONDITION  = "IfStatementWithNegatedCondition";
    public static final String LIST_INDEX_OF_REPLACEABLE_BY_CONTAINS  = "ListIndexOfReplaceableByContains";
    public static final String INSTANCEOF_INTERFACES  = "InstanceofInterfaces";
    public static final String INSTANCEOF_THIS  = "InstanceofThis";
    public static final String INSTANCEOF_CATCH_PARAMETER  = "InstanceofCatchParameter";
    public static final String INSTANCEOF_INCOMPATIBLE_INTERFACE  = "InstanceofIncompatibleInterface";
    public static final String NOTIFY_CALLED_ON_CONDITION  = "NotifyCalledOnCondition";
    public static final String NOTIFY_NOT_IN_SYNCHRONIZED_CONTEXT  = "NotifyNotInSynchronizedContext";
    public static final String NAKED_NOTIFY  = "NakedNotify";
    public static final String NOTIFY_WITHOUT_CORRESPONDING_WAIT  = "NotifyWithoutCorrespondingWait";
    public static final String FINAL_PRIVATE_METHOD  = "FinalPrivateMethod";
    public static final String PROTECTED_MEMBER_IN_FINAL_CLASS  = "ProtectedMemberInFinalClass";
    public static final String PUBLIC_CONSTRUCTOR_IN_NON_PUBLIC_CLASS  = "PublicConstructorInNonPublicClass";
    public static final String NON_PRIVATE_SERIALIZATION_METHOD  = "NonPrivateSerializationMethod";
    public static final String READ_RESOLVE_AND_WRITE_REPLACE_PROTECTED  = "ReadResolveAndWriteReplaceProtected";
    public static final String RETURN_INSIDE_FINALLY_BLOCK  = "ReturnInsideFinallyBlock";
    public static final String SERIAL_PERSISTENT_FIELDS_WITH_WRONG_SIGNATURE  = "SerialPersistentFieldsWithWrongSignature";
    public static final String SERIAL_VERSION_UID_WITH_WRONG_SIGNATURE  = "SerialVersionUIDWithWrongSignature";
    public static final String SET_UP_DOESNT_CALL_SUPER_SET_UP  = "SetUpDoesntCallSuperSetUp";
    public static final String SET_UP_WITH_INCORRECT_SIGNATURE  = "SetUpWithIncorrectSignature";
    public static final String MISSPELLED_SET_UP  = "MisspelledSetUp";
    public static final String SIGNAL_WITHOUT_CORRESPONDING_AWAIT  = "SignalWithoutCorrespondingAwait";
    public static final String SIZE_REPLACEABLE_BY_IS_EMPTY  = "SizeReplaceableByIsEmpty";
    public static final String FINAL_STATIC_METHOD  = "FinalStaticMethod";
    public static final String STATIC_NON_FINAL_FIELD  = "StaticNonFinalField";
    public static final String SUITE_NOT_DECLARED_STATIC  = "SuiteNotDeclaredStatic";
    public static final String SWITCH_STATEMENT  = "SwitchStatement";
    public static final String SWITCH_STATEMENT_WITH_TOO_FEW_BRANCHES  = "SwitchStatementWithTooFewBranches";
    public static final String SWITCH_STATEMENT_DENSITY  = "SwitchStatementDensity";
    public static final String SWITCH_STATEMENT_WITH_TOO_MANY_BRANCHES  = "SwitchStatementWithTooManyBranches";
    public static final String SWITCH_STATEMENT_WITHOUT_DEFAULT_BRANCH  = "SwitchStatementWithoutDefaultBranch";
    public static final String SYNCHRONIZED_METHOD  = "SynchronizedMethod";
    public static final String TEAR_DOWN_DOESNT_CALL_SUPER_TEAR_DOWN  = "TearDownDoesntCallSuperTearDown";
    public static final String TEAR_DOWN_WITH_INCORRECT_SIGNATURE  = "TearDownWithIncorrectSignature";
    public static final String MISSPELLED_TEAR_DOWN  = "MisspelledTearDown";
    public static final String THIS_ESCAPED_IN_OBJECT_CONSTRUCTION  = "ThisEscapedInObjectConstruction";
    public static final String THROW_CAUGHT_LOCALLY  = "ThrowCaughtLocally";
    public static final String THROW_INSIDE_CATCH_BLOCK_WHICH_IGNORES_CAUGHT_EXCEPTION  = "ThrowInsideCatchBlockWhichIgnoresCaughtException";
    public static final String THROW_FROM_FINALLY_BLOCK  = "ThrowFromFinallyBlock";
    public static final String MISSPELLED_TO_STRING  = "MisspelledToString";
    public static final String WAIT_CALLED_ON_CONDITION  = "WaitCalledOnCondition";
    public static final String WAIT_NOT_IN_LOOP  = "WaitNotInLoop";
    public static final String WAIT_OR_AWAIT_WITHOUT_TIMEOUT  = "WaitOrAwaitWithoutTimeout";
    public static final String WAIT_WHILE_HOLDING_TWO_LOCKS  = "WaitWhileHoldingTwoLocks";
    public static final String WAIT_WHILE_NOT_SYNCED  = "WaitWhileNotSynced";
    public static final String WAIT_WITHOUT_CORRESPONDING_NOTIFY  = "WaitWithoutCorrespondingNotify";
    public static final String WHILE_LOOP_REPLACEABLE_BY_FOR_EACH  = "WhileLoopReplaceableByForEach";


    public static final String ARRAYS_ASLIST_WITH_ZERO_OR_ONE_ARGUMENT  = "ArraysAsListWithZeroOrOneArgument";
    public static final String REDUNDANT_ARRAY_CREATION  = "RedundantArrayCreation";
    public static final String SAME_PARAMETER_VALUE  = "SameParameterValue";
    public static final String UNUSED_RETURN_VALUE  = "UnusedReturnValue";
}
