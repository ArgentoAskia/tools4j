package cn.argento.askia.utilities.windows.reg.sub;

import cn.argento.askia.utilities.windows.reg.AbstractRegCommand;
import cn.argento.askia.utilities.windows.reg.RegSubCommands;
import cn.argento.askia.utilities.windows.reg.deprecated.RegCommand;

public final class RegExport extends AbstractRegCommand {

    protected RegExport(RegSubCommands commands) {
        super(commands);
    }

    protected RegExport(RegSubCommands commands, int argsCounts) {
        super(commands, argsCounts);
    }

    @Override
    protected void registerArgBits() {

    }
}
