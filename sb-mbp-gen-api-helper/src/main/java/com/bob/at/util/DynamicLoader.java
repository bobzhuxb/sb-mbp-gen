package com.bob.at.util;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 动态加载器
 * @author Bob
 */
public class DynamicLoader {

    private static Pattern PATTERN = Pattern.compile("public\\s+class\\s+(\\w+)");

    /**
     * 自动从Java源码中分析出类名，并编译Java源码
     * @param javaSrc Java源码
     * @return Key：全路径类名   Value：字节码
     */
    public static Map<String, byte[]> compile(String javaSrc) throws IOException {
        Matcher matcher = PATTERN.matcher(javaSrc);
        if (matcher.find()) {
            return compile(matcher.group(1) + ".java", javaSrc);
        }
        return null;
    }

    /**
     * 编译Java源码
     * @param javaName Java类名，例如：TestClass.java
     * @param javaSrc Java源码
     * @return Key：全路径类名   Value：字节码
     */
    public static Map<String, byte[]> compile(String javaName, String javaSrc) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager stdManager = compiler.getStandardFileManager(null, null, null);

        try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
            JavaFileObject javaFileObject = MemoryJavaFileManager.makeStringSource(javaName, javaSrc);
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, Arrays.asList(javaFileObject));
            if (task.call()) {
                return manager.getClassBytes();
            }
        }
        return null;
    }

    /**
     * 编译Java源码
     * @param fullFileNameList 全路径文件名列表
     * @return Key：全路径类名   Value：字节码
     */
    public static Map<String, byte[]> compile(List<String> fullFileNameList) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager stdManager = compiler.getStandardFileManager(null, null, null);

        try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
            Iterable<? extends JavaFileObject> javaFileObjects =
                    stdManager.getJavaFileObjectsFromStrings(fullFileNameList);
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, javaFileObjects);
            if (task.call()) {
                return manager.getClassBytes();
            }
        }
        return null;
    }

}

/**
 * JavaFileManager that keeps compiled .class bytes in memory.
 */
@SuppressWarnings("unchecked")
final class MemoryJavaFileManager extends ForwardingJavaFileManager {

    /**
     * Java source file extension.
     */
    private final static String EXT = ".java";

    private Map<String, byte[]> classBytes;

    public MemoryJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
        classBytes = new HashMap<>();
    }

    public Map<String, byte[]> getClassBytes() {
        return classBytes;
    }

    @Override
    public void close() throws IOException {
        classBytes = new HashMap<>();
    }

    @Override
    public void flush() throws IOException {
    }

    /**
     * A file object used to represent Java source coming from a string.
     */
    private static class StringInputBuffer extends SimpleJavaFileObject {
        final String code;

        StringInputBuffer(String name, String code) {
            super(toURI(name), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharBuffer getCharContent(boolean ignoreEncodingErrors) {
            return CharBuffer.wrap(code);
        }

        public Reader openReader() {
            return new StringReader(code);
        }
    }

    /**
     * A file object that stores Java bytecode into the classBytes map.
     */
    private class ClassOutputBuffer extends SimpleJavaFileObject {
        private String name;

        ClassOutputBuffer(String name) {
            super(toURI(name), Kind.CLASS);
            this.name = name;
        }

        @Override
        public OutputStream openOutputStream() {
            return new FilterOutputStream(new ByteArrayOutputStream()) {
                @Override
                public void close() throws IOException {
                    out.close();
                    ByteArrayOutputStream bos = (ByteArrayOutputStream) out;
                    classBytes.put(name, bos.toByteArray());
                }
            };
        }
    }

    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location,
                                               String className,
                                               JavaFileObject.Kind kind,
                                               FileObject sibling) throws IOException {
        if (kind == JavaFileObject.Kind.CLASS) {
            return new ClassOutputBuffer(className);
        } else {
            return super.getJavaFileForOutput(location, className, kind, sibling);
        }
    }

    static JavaFileObject makeStringSource(String name, String code) {
        return new StringInputBuffer(name, code);
    }

    static URI toURI(String name) {
        File file = new File(name);
        if (file.exists()) {
            return file.toURI();
        } else {
            try {
                final StringBuilder newUri = new StringBuilder();
                newUri.append("mfm:///");
                newUri.append(name.replace('.', '/'));
                if (name.endsWith(EXT)) {
                    newUri.replace(newUri.length() - EXT.length(), newUri.length(), EXT);
                }
                return URI.create(newUri.toString());
            } catch (Exception exp) {
                return URI.create("mfm:///com/sun/script/java/java_source");
            }
        }
    }
}

