package cn.argento.askia.utilities.windows.reg.operators;


import cn.argento.askia.utilities.windows.reg.RegUtility;

public interface IKeyName<N> {
    N keyName(RegUtility.RootKeyConstants root, String subKey);
}
