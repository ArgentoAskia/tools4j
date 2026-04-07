package cn.argento.askia.unknown.windows.reg.operators;


import cn.argento.askia.unknown.windows.reg.RegUtility;

public interface IKeyName<N> {
    N keyName(RegUtility.RootKeyConstants root, String subKey);
}
