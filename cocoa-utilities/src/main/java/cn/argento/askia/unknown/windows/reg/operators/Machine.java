package cn.argento.askia.unknown.windows.reg.operators;

public class Machine {
    private String machineName;

    private Machine(String machineName){
        this.machineName = machineName;
    }

    @Override
    public String toString() {
        return "\\\\" + machineName;
    }

    public static Machine of(String machineName){
        return new Machine(machineName);
    }

}
