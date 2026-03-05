package cn.argento.askia.processors;

import java.io.*;
import java.lang.annotation.Annotation;
import java.util.Properties;

/**
 * 抽象实现IAnnotationProcessor
 * @param <A>
 */
public abstract class AbstractAnnotationProcessor<A extends Annotation> implements AnnotationProcessor<A> {

    protected Properties properties;

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    protected void loadProperties(File propertiesFile){
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(propertiesFile);
            loadProperties(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void loadProperties(InputStream inputStream){
        try {
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
