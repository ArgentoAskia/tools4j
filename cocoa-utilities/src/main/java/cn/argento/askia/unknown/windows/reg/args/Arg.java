package cn.argento.askia.unknown.windows.reg.args;

import cn.argento.askia.unknown.windows.reg.commands.RegCommand;

/**
 * Sub Commands Args' Top-level interface.
 *
 * <p>
 *     Provide a build method to Build {@link RegCommand} interface objects for all sub commands implementations
 *     see {@linkplain Arg#build() build} method’s doc.
 *
 * @author Askia
 * @see RegCommand
 * @since 1.0
 */
public interface Arg {
    /**
     * Build the {@linkplain RegCommand RegCommand} Object.
     *
     * @apiNote
     *  All Implementations use private Constructor and build {@linkplain RegCommand RegCommand} Object with this method.
     *  All Implementations should NOT Be instantiated by NEW!
     *
     * @implNote
     *  if the command need to do something before execute, such as checked the format, rebuild the command String and so on..
     *  you can write out the extra use with this method! This Method need to return {@code this} for going on calling!
     *
     * @return RegCommand
     */
    RegCommand build();
}
