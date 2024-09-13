package cn.argento.askia.utilities.windows.reg.sub;

import cn.argento.askia.utilities.windows.reg.*;
import cn.argento.askia.utilities.windows.reg.args.*;

public final class RegAdd extends AbstractRegCommand {


    protected RegAdd() {
        super(RegSubCommands.REG_ADD);
    }

    @Override
    protected void registerArgBits() {


    }

    public final class RegAddOptionalArgs extends OptionalArgs
            implements ArgD<RegAddOptionalArgs>, ArgF<RegAddOptionalArgs>,
            ArgV<RegAddOptionalArgs>, ArgVE<RegAddOptionalArgs>,
            ArgT<RegAddOptionalArgs>, ArgS<RegAddOptionalArgs>,
            ArgReg32<RegAddOptionalArgs>, ArgReg64<RegAddOptionalArgs>{

        protected RegAddOptionalArgs(AbstractRegCommand proxy) {
            super(proxy);
        }

        @Override
        public RegAddOptionalArgs d(String data) {
            return null;
        }

        @Override
        public RegAddOptionalArgs f() {
            return null;
        }

        @Override
        public RegAddOptionalArgs s(String Separator) {
            return null;
        }

        @Override
        public RegAddOptionalArgs s() {
            return null;
        }

        @Override
        public RegAddOptionalArgs t(RegUtility.Type type) {
            return null;
        }

        @Override
        public RegAddOptionalArgs v(String ValueName) {
            return null;
        }

        @Override
        public RegAddOptionalArgs ve() {
            return null;
        }

        @Override
        public RegCommand build() {
            return null;
        }
    }
}
