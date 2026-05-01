package cn.argento.askia.scanner;

public interface Scanner<P, R> {

    R scan(P param);
}
