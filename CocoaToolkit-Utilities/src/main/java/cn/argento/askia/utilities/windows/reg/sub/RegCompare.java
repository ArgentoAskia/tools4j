package cn.argento.askia.utilities.windows.reg.sub;

import cn.argento.askia.utilities.windows.reg.AbstractRegCommand;
import cn.argento.askia.utilities.windows.reg.RegSubCommands;

public final class RegCompare extends AbstractRegCommand {

    protected RegCompare() {
        super(RegSubCommands.REG_COMPARE);
    }

    @Override
    protected void registerArgBits() {

    }
}
