package cn.argento.askia.utilities.windows.reg.operators;

import cn.argento.askia.utilities.windows.reg.RegUtility;

public final class RemoteKeyName<N> extends KeyName<N> implements IRemoteKeyName<N> {

    private Machine machine;
    private RegUtility.MachineRootKeyConstants machineRootKey;

    public RemoteKeyName(N nextJump) {
        super(nextJump);
    }

    public N keyName(Machine remoteMachine,
                     RegUtility.MachineRootKeyConstants root,
                     String subKey){
        this.machine = remoteMachine;
        this.machineRootKey = root;
        this.subKey = subKey;
        return nextJump;
    }

    @Override
    public String toString() {
        formatSubKeyString();
        return machine.toString() + "\\" + machineRootKey.toString() + subKey;
    }
}
