package cn.argento.askia.unknown.windows.reg.args;
@Deprecated
public interface Converter<T> {

    class EmptyConverter implements Converter<Void>{

        @Override
        public String convert(Void v) {
            return "";
        }
    }


    String convert(T javaType);
}
