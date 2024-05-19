package cn.argento.askia.utilities.windows.reg;

import cn.argento.askia.utilities.IOStreamUtility;

import javax.annotation.processing.Processor;
import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCommand implements Command {
    protected StringBuilder commandBuilder;
    protected BitSet argsDuplicatedSet;

    private static final int BIT_SET_INIT_SIZE = 128;

    private Map<String, BitSet> argBitSetMap;

    protected AbstractCommand(){
        this("");
    }
    protected AbstractCommand(String cmdInit){
        commandBuilder = new StringBuilder(cmdInit);
        argsDuplicatedSet = new BitSet(BIT_SET_INIT_SIZE);
        argBitSetMap = new HashMap<>();
        registerArgBits();
    }

    protected abstract void registerArgBits();

    protected void appendCMD(String cmd, boolean appendSpace){
        commandBuilder.append(cmd);
        if (appendSpace){
            commandBuilder.append(' ');
        }
    }
    protected void needAppendWhiteSpace(){
        if (!commandBuilder.toString().endsWith(" ")){
            commandBuilder.append(" ");
        }
    }


    protected void register(String... argNames){
        final int currentExistBitSize = argBitSetMap.size();
        for (int i = 0; i < argNames.length; i++) {
            BitSet bitSet = new BitSet(currentExistBitSize + 1 + i);
            bitSet.set(currentExistBitSize + i, true);
            argBitSetMap.put(argNames[i], bitSet);
        }
    }

    protected void register(String argName, BitSet bit){
        argBitSetMap.put(argName, bit);
    }
    protected void registerSame(String key, String sameAs){
        final BitSet bitSet = argBitSetMap.get(sameAs);
        assert bitSet != null;
        argBitSetMap.put(key, (BitSet) bitSet.clone());
    }


    // false = bit not exist
    protected boolean cmpBit(String argName){
        final BitSet bitSet = argBitSetMap.get(argName);
        final BitSet clone = (BitSet) argsDuplicatedSet.clone();
        clone.and(bitSet);
        return clone.equals(bitSet);
    }

    protected void addBit(String argName){
        final BitSet bitSet = argBitSetMap.get(argName);
        argsDuplicatedSet.or(bitSet);
    }



    protected abstract Object analyzeResult(byte[] result);

    @Override
    public Object exec() {
        final Runtime runtime = Runtime.getRuntime();
        final String cmd = toString();
        if (cmd != null && !cmd.equalsIgnoreCase("")){
            try {
                final Process exec = runtime.exec(cmd);
                final InputStream inputStream = exec.getInputStream();
                final byte[] bytesResult = IOStreamUtility.readAllBytes(inputStream);
                inputStream.close();
                return analyzeResult(bytesResult);
            } catch (IOException e) {
                throw new RuntimeException("can't run the COMMAND: " + cmd + ", maybe the cmd is invalid");
            }
        }
        throw new RuntimeException("cmd is empty or cmd was not build!");
    }

    @Override
    public String toString() {
        if (commandBuilder.length() != 0){
            return commandBuilder.toString();
        }
        return null;
    }
}
