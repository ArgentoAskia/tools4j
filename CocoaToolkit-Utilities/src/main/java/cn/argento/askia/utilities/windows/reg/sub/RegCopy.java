package cn.argento.askia.utilities.windows.reg.sub;

import cn.argento.askia.utilities.windows.reg.AbstractRegCommand;
import cn.argento.askia.utilities.windows.reg.RegSubCommands;
import cn.argento.askia.utilities.windows.reg.deprecated.RegCommand;

public final class RegCopy extends AbstractRegCommand {

    protected RegCopy() {
        super(RegSubCommands.REG_COPY);
    }

    @Override
    protected void registerArgBits() {

    }
}
