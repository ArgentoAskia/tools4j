package cn.argento.askia.utilities.windows.reg.args;

import cn.argento.askia.utilities.windows.reg.commands.RegSubCommandManager;

@RegCommandArg
public interface ArgQuerySet<A extends Arg> extends Arg{
    enum QuerySetEnum {
        DONT_VIRTUALIZE, DONT_SILENT_FAIL, RECURSE_FLAG
    }
    
    
    @RegCommandArg(argName = "QUERY", formatStr = "QUERY")
    A query();

    @RegCommandArg(argName = "SET", formatStr = "SET [query...]")
    A set(QuerySetEnum... set);

    @RegCommandArg(argName = "SETR", formatStr = "SET [query] /s")
    A setRecursion(QuerySetEnum set);
}
