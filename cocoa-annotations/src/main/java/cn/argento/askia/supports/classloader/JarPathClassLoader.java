package cn.argento.askia.supports.classloader;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class JarPathClassLoader extends ClassLoader {

    private final Path jarPath;
    private final FileSystem jarFs;

    public JarPathClassLoader(Path jarPath) throws IOException {
        this(jarPath, ClassLoader.getSystemClassLoader());
    }

    public JarPathClassLoader(Path jarPath, ClassLoader parent) throws IOException {
        super(parent);
        this.jarPath = jarPath.toAbsolutePath();
        // 创建 ZIP 文件系统访问 JAR 内部
        this.jarFs = FileSystems.newFileSystem(URI.create("jar:" + jarPath.toUri()), new HashMap<>());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = name.replace('.', '/') + ".class";
        Path classFile = jarFs.getPath(path);
        if (!Files.isRegularFile(classFile)) {
            throw new ClassNotFoundException(name + " not found in " + jarPath);
        }
        try {
            byte[] bytes = Files.readAllBytes(classFile);
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Failed to load class " + name, e);
        }
    }

    public void close() throws IOException {
        jarFs.close(); // 释放资源
    }
}
