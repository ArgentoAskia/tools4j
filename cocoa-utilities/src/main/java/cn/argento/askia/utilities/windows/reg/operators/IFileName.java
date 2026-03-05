package cn.argento.askia.utilities.windows.reg.operators;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IFileName<N> {
    N fileName(Supplier<File> fileSupplier);
    N fileName(String filePath);
    N fileName(Path filePath);
}
