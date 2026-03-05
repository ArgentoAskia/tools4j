package cn.argento.askia.utilities.windows.reg.operators;

import cn.argento.askia.utilities.windows.reg.RegUtility;

public interface IRemoteKeyName<N> extends IKeyName<N>{

    N keyName(Machine remoteMachine,
              RegUtility.MachineRootKeyConstants root,
              String subKey);
}
