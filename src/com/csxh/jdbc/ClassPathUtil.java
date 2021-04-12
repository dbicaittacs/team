package com.csxh.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class ClassPathUtil {
    /**
     * 取得当前类所在的文件对象
     * @param clazz
     * @return
     */
    public static File getClassFile(Class<?> clazz){
        URL path = clazz.getResource(clazz.getName().substring(
                clazz.getName().lastIndexOf(".")+1)+".classs");
        if(path == null){
            String name = clazz.getName().replaceAll("[.]", "/");
            path = clazz.getResource("/"+name+".class");
        }
        return new File(path.getFile());
    }
    
    /**
     * 得到当前类的路径
     * @param clazz
     * @return
     * @throws Exception 
     */
    public static String getClassFilePath(Class<?> clazz) throws Exception{
    	String path= java.net.URLDecoder.decode(getClassFile(clazz).getAbsolutePath(),"UTF-8");
    	return path;

    }
    
    /**
     * 取得当前类所在的ClassPath根目录对象，比如tomcat下的classes路径
     * @param clazz
     * @return
     */
    public static File getClassRootFile(Class<?> clazz){
        File file = getClassFile(clazz);
        for(int i=0,count = clazz.getName().split("[.]").length; i<count; i++)
            file = file.getParentFile();
        if(file.getName().toUpperCase().endsWith(".JAR!")){
            file = file.getParentFile();
        }
        return file;
    }
    /**
     * 取得当前类所在的ClassPath路径
     * @param clazz
     * @return
     * @throws Exception 
     */
    public static String getClassRootPath(Class<?> clazz) throws Exception{
    	return java.net.URLDecoder.decode(getClassRootFile(clazz).getAbsolutePath(),"UTF-8");

    }
    
    public static String getClassRootPathResource(Class<?>clazz,String resourceNme) throws Exception{
    	String path= java.net.URLDecoder.decode(getClassRootFile(clazz).getAbsolutePath(),"UTF-8");
		return path+File.separator+resourceNme;
    }
    
    public static File getClassRootPathResourceAsFile(Class<?>clazz,String resourceName) throws Exception{
    	return new File(getClassRootPathResource(clazz,resourceName));
    }

    public static InputStream getClassRootPathResourceAsInputStream(Class<?>clazz,String resourceName) throws Exception{
    	return new FileInputStream(getClassRootPathResourceAsFile(clazz,resourceName));
    }
    
    public static OutputStream getClassRootPathResourceAsOutputStream(Class<?>clazz,String resourceName) throws Exception{
    	return new FileOutputStream(getClassRootPathResourceAsFile(clazz,resourceName));
    }

    
    public static void main(String[] args) throws Exception{
        System.out.println(getClassFilePath(ClassPathUtil.class));
        System.out.println(getClassRootPath(ClassPathUtil.class));
    }

}