package cn.argento.askia.utilities.windows.reg;

/**
 * @apiNote
 */
public interface Command {

    /**
     * execute cmd and get result
     * impls should change the return type
     * @return result
     */
    Object exec();


    /**
     * get the String type of Command!
     * @return
     */
    String toString();

    Command build();

}
