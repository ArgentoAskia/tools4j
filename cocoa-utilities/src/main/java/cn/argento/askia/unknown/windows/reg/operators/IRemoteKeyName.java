package cn.argento.askia.unknown.windows.reg.operators;

import cn.argento.askia.unknown.windows.reg.RegUtility;

public interface IRemoteKeyName<N> extends IKeyName<N>{

    N keyName(Machine remoteMachine,
              RegUtility.MachineRootKeyConstants root,
              String subKey);
}
