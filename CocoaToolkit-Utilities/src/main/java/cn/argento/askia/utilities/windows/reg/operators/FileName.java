package cn.argento.askia.utilities.windows.reg.operators;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Supplier;

public final class FileName<N> implements IFileName<N>{

    private File file;

    private N nextJump;

    public FileName(N nextJump){
        this.nextJump = nextJump;
    }

    @Override
    public N fileName(Supplier<File> fileSupplier) {
        file = fileSupplier.get();
        return nextJump;
    }

    @Override
    public N fileName(String fileName){
        file = new File(fileName);
        return nextJump;
    }

    @Override
    public N fileName(Path filePath) {
        file = filePath.toFile();
        return nextJump;
    }

    @Override
    public String toString() {
        String absolutePath = null;
        try {
            absolutePath = file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert absolutePath != null;
        if (!absolutePath.endsWith(" ")){
            absolutePath = absolutePath + " ";
        }
        return absolutePath;
    }
}
