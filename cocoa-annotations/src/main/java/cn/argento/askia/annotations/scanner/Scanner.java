package cn.argento.askia.annotations.scanner;

public interface Scanner<P, R> {

    R scan(P param);
}
