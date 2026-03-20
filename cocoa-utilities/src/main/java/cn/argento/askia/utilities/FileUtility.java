package cn.argento.askia.utilities;


import cn.argento.askia.exceptions.runtime.files.NotFileException;
import cn.argento.askia.exceptions.runtime.files.UnsupportedFileTypeRuntimeException;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 增删改价读写
 * Files File Path
 */
public class FileUtility {

    @SuppressWarnings("all")
    public static File createFileIfNotExist(String path) {
        File file = new File(path);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static File createRelativeFileIfNotExist(){
        URL location = FileUtility.class.getProtectionDomain().getCodeSource().getLocation();
        System.out.println(location.toString());

        return new File(location.toString());
    }


    public static File newWorkingDirectoryFile(String file, String workingDir){
        return Paths.get(workingDir, file).toFile();
    }


    public static FileInputStream openFileStream(File file) throws IOException {
        boolean flag = true;
        if (!file.exists()){
            flag = file.createNewFile();
        }
        if (flag){
            FileInputStream fileInputStream = new FileInputStream(file);
            return fileInputStream;
        }
        throw new FileNotFoundException("无法创建文件：" + file + ",createNewFile()方法返回false");
    }

    public static File[] scanPath(File file){
        return file.listFiles();
    }

    private static File[] scanPath(File file, FileFilter fileFilter){
        return file.listFiles(fileFilter);
    }

    private static File[] scanPath(File file, FilenameFilter filenameFilter){
        return file.listFiles(filenameFilter);
    }


    public static int scanFilesAndDirs(File pathToScan, List<File> filesRef, List<File> dirsRef, FileFilter fileFilter){
        // 无法扫描
        if (!pathToScan.exists() || !pathToScan.isDirectory()){
            return -1;
        }
        File[] files = null;
        if (fileFilter == null){
            files = pathToScan.listFiles();
        }
        else{
            files = pathToScan.listFiles(fileFilter);
        }

        Objects.requireNonNull(files);
        int fileCount = 0;
        for (File f :
                files) {
           if (f.isDirectory() && dirsRef != null){
               dirsRef.add(f);
           }
           if (f.isFile() && filesRef != null){
               filesRef.add(f);
               fileCount++;
           }
        }
        return fileCount;
    }

    public static int deepScanFilesAndDirs(File pathToScan, List<File> filesRef, List<File> dirsRef, FileFilter fileFilter){
        Queue<File> scanPathList = new LinkedList<>();
        scanPathList.offer(pathToScan);
        int totals = 0;
        List<File> filesTraverse = new ArrayList<>();
        List<File> dirsTraverse = new ArrayList<>();
        while(scanPathList.size() > 0){
            File poll = scanPathList.poll();
            int i = scanFilesAndDirs(poll, filesTraverse, dirsTraverse, fileFilter);
            // 添加进Ref然后继续扫面
            scanPathList.addAll(dirsTraverse);
            filesRef.addAll(filesTraverse);
            dirsRef.addAll(dirsTraverse);
            filesTraverse.clear();
            dirsTraverse.clear();
            totals += i;
        }
        return totals;
    }


    // 读取文件
    public static final String COMPRESS_FILE_SPECIAL_SP = "!" + FileSystems.getDefault().getSeparator();      // 压缩文件特殊分割符号
    private static final String COMPRESS_FILE_SPECIAL_SP_SPLIT_WIN = "!\\\\";
    private static final String COMPRESS_FILE_SPECIAL_SP_SPLIT_LINUX = "!/";

    /**
     * 读取一个文本文件的所有行.
     * <p>此方法是 {@link Files#readAllLines(Path, Charset)} 的增强版本，额外提供了 {@code jar} 包和 {@code zip} 包中的文本文件的读取。
     * <p>方法在读取目录下的文本文件时仍然采用 {@link Files#readAllLines(Path, Charset)} 进行，所以该方法的限制和 {@link Files#readAllLines(Path, Charset)} 相同
     * <p>对于jar包中的文本文件行读取，方法也采用如下的行终端符号：<ul>
     *     <li>\u000D followed by \u000A, CARRIAGE RETURN followed by LINE FEED</li>
     *     <li>\u000A, LINE FEED</li>
     *     <li>\u000D, CARRIAGE RETURN</li>
     * </ul>
     * <p><b>需注意，本方法适用于可通过单次操作读取所有行的简单场景。 该方法不适用于大型文件的读取。</b>
     * @param path 文件路径，如果时jar包和zip包则需要在jar包路径末尾补上!, 用以分割jar包路径和jar包内文件路径，比如：<pre><code>
     *             Paths.get("/etc/a.jar!", "etc/boot/efi.wim")
     * </code></pre>
     * @param cs 用于解码的文本charset
     * @return 所有文本行，List可修改
     * @throws IllegalArgumentException 如果提供的path不是一个合法的压缩文件路径
     * @throws FileNotFoundException 是压缩文件但没有提供压缩包内的文件路径
     * @throws UnsupportedFileTypeRuntimeException 如果提供的压缩文件格式不是zip或者jar时抛出(基于后缀判断而非文件魔数)
     * @throws NotFileException 如果提供的压缩包内文件不是一个文件而是目录时抛出
     * @throws IOException {@link Files#readAllLines(Path, Charset)}方法抛出的异常也会原样抛出,  或者 {@link JarFile} 读取和 {@link ZipFile} 读取时出问题了（比如提供了jar后缀的压缩包但该压缩包实际无法使用JarFile打开）也会抛出此异常
     * @see FileUtility#COMPRESS_FILE_SPECIAL_SP
     * @see Files#readAllLines(Path, Charset)
     * @see FileUtility#readAllLines(Path)
     * @see Files#readAllLines(Path)
     * @since 2026.2.3
     */
    public static List<String> readAllLines(Path path, Charset cs) throws IOException {
        String s = path.toString();
        boolean isCompressFile = false;
        String[] compressFiles = new String[0];
        if (s.contains(COMPRESS_FILE_SPECIAL_SP)){
            isCompressFile = true;
            if (SystemUtility.isWindowsNTOS()) {
                compressFiles = s.split(COMPRESS_FILE_SPECIAL_SP_SPLIT_WIN);
            }
            else{
                // 其他都采用Linux分割方法
                compressFiles = s.split(COMPRESS_FILE_SPECIAL_SP_SPLIT_LINUX);
            }
            if (compressFiles.length == 0){
                throw new IllegalArgumentException("Path携带压缩文件分隔符：" + COMPRESS_FILE_SPECIAL_SP + ", 但并不是一个有效的压缩文件路径: " + path
                        + ", 请确保您的路径符合：【Zip文件位置" + COMPRESS_FILE_SPECIAL_SP + "Zip内部文件路径的形式，例如：E:/A.jar" + COMPRESS_FILE_SPECIAL_SP + "BOOT-INF/classes");
            }
            if (compressFiles.length == 1){
                // 第一个路径肯定是压缩文件的位置，因此如果只有一个参数的话肯定也有问题
                throw new FileNotFoundException("请提供一个压缩包内的文件路径, 因为你的Path: " + path + "，并没有包含具体的文件路径!");
            }
        }
        if (!isCompressFile){
            // 如果是非压缩文件，则我们使用默认的文件读法即可
            return Files.readAllLines(path, cs);
        }
        else{
            // 压缩文件
            String compressFile = compressFiles[0];
            if (compressFile.endsWith(".jar")){
                // jar文件
                return readAllLineFromJarFile(compressFiles, cs);
            }
            else if (compressFile.endsWith(".zip")){
                return readAllLineFromZipFile(compressFiles, cs);
            }
            else{
                throw new UnsupportedFileTypeRuntimeException("目前仅支持zip文件和jar文件的内部文件读写，更多压缩文件支持敬请期待");
            }
        }
    }

    private static List<String> readAllLineFromJarFile(String[] compressFiles, Charset cs) throws IOException {
        File jarFile = new File(compressFiles[0]);
        JarFile jarFileObj = new JarFile(jarFile);
        StringJoiner fileInJarPathBuilder = new StringJoiner("/");
        for (int i = 1; i < compressFiles.length; i++){
            fileInJarPathBuilder.add(compressFiles[i].replace("\\", "/"));
        }
        JarEntry jarEntry = jarFileObj.getJarEntry(fileInJarPathBuilder.toString());
        if (jarEntry.isDirectory()){
            // 不能是目录
            throw new NotFileException("jar文件中的【" + jarFile.getAbsolutePath() + "】");
        }
        return entryReadAllLine(jarFileObj, jarEntry, cs);
    }
    private static List<String> readAllLineFromZipFile(String[] compressFiles, Charset cs) throws IOException {
        File zipFile = new File(compressFiles[0]);
        ZipFile zipFileObj = new ZipFile(zipFile);
        StringJoiner fileInJarPathBuilder = new StringJoiner(FileSystems.getDefault().getSeparator());
        for (int i = 1; i < compressFiles.length; i++){
            fileInJarPathBuilder.add(compressFiles[i]);
        }
        ZipEntry entry = zipFileObj.getEntry(fileInJarPathBuilder.toString());
        if (entry.isDirectory()){
            // 不能是目录
            throw new NotFileException("jar文件中的【" + entry.getName() + "】");
        }
        return entryReadAllLine(zipFileObj, entry, cs);
    }

    private static List<String> entryReadAllLine(ZipFile fileObj, ZipEntry entry, Charset cs) throws IOException {
        InputStream inputStream = fileObj.getInputStream(entry);
        CharsetDecoder decoder = cs.newDecoder();
        Reader reader = new InputStreamReader(inputStream, decoder);
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            List<String> result = new ArrayList<>();
            for (;;) {
                String line = bufferedReader.readLine();
                if (line == null)
                    break;
                result.add(line);
            }
            return result;
        }
    }

    /**
     * 读取一个文本文件的所有行.
     * <p>此方法是 {@link FileUtility#readAllLines(Path, Charset)} 的快捷调用，默认使用 {@link StandardCharsets#UTF_8} 来打开和解析文件
     *
     * @param path 文件路径，如果时jar包和zip包则需要在jar包路径末尾补上!, 用以分割jar包路径和jar包内文件路径，比如：<pre><code>
     *             Paths.get("/etc/a.jar!", "etc/boot/efi.wim")
     * </code></pre>
     * @return 所有文本行，List可修改
     * @throws IllegalArgumentException 如果提供的path不是一个合法的压缩文件路径
     * @throws FileNotFoundException 是压缩文件但没有提供压缩包内的文件路径
     * @throws UnsupportedFileTypeRuntimeException 如果提供的压缩文件格式不是zip或者jar时抛出(基于后缀判断而非文件魔数)
     * @throws NotFileException 如果提供的压缩包内文件不是一个文件而是目录时抛出
     * @throws IOException {@link Files#readAllLines(Path, Charset)}方法抛出的异常也会原样抛出,  或者 {@link JarFile} 读取和 {@link ZipFile} 读取时出问题了（比如提供了jar后缀的压缩包但该压缩包实际无法使用JarFile打开）也会抛出此异常
     * @see FileUtility#COMPRESS_FILE_SPECIAL_SP
     * @see Files#readAllLines(Path, Charset)
     * @see FileUtility#readAllLines(Path, Charset)
     * @see Files#readAllLines(Path)
     * @since 2026.2.3
     */
    public static List<String> readAllLines(Path path) throws IOException {
       return readAllLines(path, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        ArrayList<File> filesRef = new ArrayList<>();
        ArrayList<File> dirsRef = new ArrayList<>();
        int i = deepScanFilesAndDirs(new File("E:\\OpenSourceProjects\\yumitoy-backend"), filesRef, dirsRef, null);
        System.out.println(i);
        System.out.println(filesRef);
        System.out.println(dirsRef);
    }

    // MIMEType <==> fileSuffix

    private static final Map<String, String> MIME_TO_EXT = new HashMap<>();
    private static final String UNKNOWN_MIME_TYPE = "unknown";
    static {
        // MIME 类型大全，参考：https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Guides/MIME_types/Common_types
        // IMAGE组别
        MIME_TO_EXT.put("image/gif", "gif");
        MIME_TO_EXT.put("image/apng", "apng");
        MIME_TO_EXT.put("image/avif", "avif");
        MIME_TO_EXT.put("image/jpeg", "jpg");
        MIME_TO_EXT.put("image/jp2", "jpg2");
        MIME_TO_EXT.put("image/png", "png");
        MIME_TO_EXT.put("image/tiff", "tif");
        MIME_TO_EXT.put("image/bmp", "bmp");
        MIME_TO_EXT.put("image/svg+xml", "svg");
        MIME_TO_EXT.put("image/webp", "webp");
        MIME_TO_EXT.put("image/x-icon", "ico");
        MIME_TO_EXT.put("image/vnd.microsoft.icon", "ico");

        // audio组
        MIME_TO_EXT.put("audio/aac", "aac");
        MIME_TO_EXT.put("audio/midi", "midi");
        MIME_TO_EXT.put("audio/mpeg", "mp3");
        MIME_TO_EXT.put("audio/ogg", "ogg");
        MIME_TO_EXT.put("audio/opus", "opus");
        MIME_TO_EXT.put("audio/wav", "wav");
        MIME_TO_EXT.put("audio/webm", "weba");
        MIME_TO_EXT.put("audio/3gpp", "3gp");
        MIME_TO_EXT.put("audio/3gpp2", "3g2");

        // application 组
        MIME_TO_EXT.put("application/x-abiword", "abw");
        MIME_TO_EXT.put("application/x-freearc", "arc");
        MIME_TO_EXT.put("application/vnd.amazon.ebook", "azw");
        MIME_TO_EXT.put("application/octet-stream", "bin");
        MIME_TO_EXT.put("application/x-bzip", "bz");
        MIME_TO_EXT.put("application/x-bzip2", "bz2");
        MIME_TO_EXT.put("application/x-cdf", "cda");
        MIME_TO_EXT.put("application/x-csh", "csh");
        MIME_TO_EXT.put("application/msword", "doc");
        MIME_TO_EXT.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
        MIME_TO_EXT.put("application/vnd.ms-fontobject", "eot");
        MIME_TO_EXT.put("application/epub+zip", "epub");
        MIME_TO_EXT.put("application/gzip", "gz");
        MIME_TO_EXT.put("application/java-archive", "jar");
        MIME_TO_EXT.put("application/json", "json");
        MIME_TO_EXT.put("application/ld+json", "jsonld");
        MIME_TO_EXT.put("application/vnd.apple.installer+xml", "mpkg");
        MIME_TO_EXT.put("application/vnd.oasis.opendocument.presentation", "odp");
        MIME_TO_EXT.put("application/vnd.oasis.opendocument.spreadsheet", "ods");
        MIME_TO_EXT.put("application/vnd.oasis.opendocument.text", "odt");
        MIME_TO_EXT.put("application/ogg", "ogx");
        MIME_TO_EXT.put("application/pdf", "pdf");
        MIME_TO_EXT.put("application/x-httpd-php", "php");
        MIME_TO_EXT.put("application/vnd.ms-powerpoint", "ppt");
        MIME_TO_EXT.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx");
        MIME_TO_EXT.put("application/vnd.rar", "rar");
        MIME_TO_EXT.put("application/rtf", "rtf");
        MIME_TO_EXT.put("application/x-sh", "sh");
        MIME_TO_EXT.put("application/x-tar", "tar");
        MIME_TO_EXT.put("application/vnd.visio", "vsd");
        MIME_TO_EXT.put("application/xhtml+xml", "xhtml");
        MIME_TO_EXT.put("application/vnd.ms-excel", "xls");
        MIME_TO_EXT.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx");
        MIME_TO_EXT.put("application/xml", "xml");
        MIME_TO_EXT.put("application/vnd.mozilla.xul+xml", "xul");
        MIME_TO_EXT.put("application/zip", "zip");
        MIME_TO_EXT.put("application/x-7z-compressed", "7z");

        //  text组
        MIME_TO_EXT.put("text/css", "css");
        MIME_TO_EXT.put("text/csv", "csv");
        MIME_TO_EXT.put("text/html", "html");
        MIME_TO_EXT.put("text/calendar", "ics");
        MIME_TO_EXT.put("text/javascript", "js");
        MIME_TO_EXT.put("text/plain", "txt");
        MIME_TO_EXT.put("text/xml", "xml");

        // video组
        MIME_TO_EXT.put("video/x-msvideo", "avi");
        MIME_TO_EXT.put("video/mp4", "mp4");
        MIME_TO_EXT.put("video/mpeg", "mpeg");
        MIME_TO_EXT.put("video/ogg", "ogv");
        MIME_TO_EXT.put("video/mp2t", "ts");
        MIME_TO_EXT.put("video/webm", "webm");
        MIME_TO_EXT.put("video/3gpp", "3gp");
        MIME_TO_EXT.put("video/3gpp2", "3g2");
    }

    /**
     * 根据提供的MIME Type尝试获取对应的扩展名(Extension)
     * <p><b>此方法获取扩展名基于预先缓存的MIMEType_TO_EXT的哈希Map, 因此不能提供完全准确且完整的类型获取</b></p>
     * <p><b>此方法优先保证获取和解析的MIME Type为标准定义的, 一些历史遗留的MIME Type和非标准MIME Type可能无法解析, 如需要考虑更加精准的解析, 请使用本系列的其他包或者其他第三方库</b></p>
     * <p>当找不到匹配的Extension时, 此方法将返回{@link FileUtility#UNKNOWN_MIME_TYPE}, 该常量是一个字符串常量, 您在比较是为了节省性能，可以直接对比引用即可</p>
     *
     * @param mimeType 提供的Mime Type字符串，或者从content-type中获取的带; charset=UTF-8部分的MimeType字符串
     * @return 如果找到符合的Extension, 则返回，否则返回{@link FileUtility#UNKNOWN_MIME_TYPE}
     * @see FileUtility#UNKNOWN_MIME_TYPE
     * @since 2026.3.19
     */
    public static String getExtension(String mimeType) {
        if (mimeType == null) return null;
        // 去除可能的分号参数部分，如 "image/jpeg; charset=UTF-8"
        String baseType = mimeType.split(";")[0].trim().toLowerCase();
        return MIME_TO_EXT.getOrDefault(baseType, UNKNOWN_MIME_TYPE);
    }

}
