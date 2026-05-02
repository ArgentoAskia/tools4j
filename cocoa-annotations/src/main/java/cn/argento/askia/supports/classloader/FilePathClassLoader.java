package cn.argento.askia.supports.classloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilePathClassLoader extends ClassLoader{
    private Path classFilePath;
    public FilePathClassLoader(){
        this(ClassLoader.getSystemClassLoader());
    }

    public FilePathClassLoader(ClassLoader parent) {
        super(parent);
    }

    public void setClassFilePath(Path classFilePath) {
        this.classFilePath = classFilePath;
    }

    protected Class<?> findClass(String name, Path path) throws ClassNotFoundException {
        // 是否是合法的文件
        if (path == null || !Files.isRegularFile(path)) {
            throw new ClassNotFoundException("文件" + path + "不存在或者可能并非是合法的文件");
        }
        if (!path.toString().endsWith("class")){
            throw new ClassNotFoundException("文件" + path + "并非是class文件");
        }

        try {
            byte[] bytes = Files.readAllBytes(path);
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Failed to load class " + name, e);
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, classFilePath);
    }
}
