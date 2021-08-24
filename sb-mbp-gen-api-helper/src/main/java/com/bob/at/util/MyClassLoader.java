package com.bob.at.util;

public class MyClassLoader extends ClassLoader {

    private byte[] classBytes;

    public MyClassLoader(byte[] classBytes) {
        this.classBytes = classBytes;
    }

    @Override
    protected Class<?> findClass(String name) {
        Class clazz = defineClass(classBytes, 0, classBytes.length);
        return clazz;
    }

}
