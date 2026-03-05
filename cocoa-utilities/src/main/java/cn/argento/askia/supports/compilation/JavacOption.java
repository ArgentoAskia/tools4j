package cn.argento.askia.supports.compilation;

public enum JavacOption {
    G("-g");

    String option;
    String arg;
    JavacOption(String option){
        this.option = option;
    }

    JavacOption(String option, String arg){
        this(option);
        this.arg = arg;
    }
}
