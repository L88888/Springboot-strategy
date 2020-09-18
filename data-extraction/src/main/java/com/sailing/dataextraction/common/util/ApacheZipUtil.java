package com.sailing.dataextraction.common.util;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * apache包的Zip工具类
 * @author LIUFEI YANG
 * @version 1.0
 * @className ApacheZipUtil
 * @date 2019/10/23 15:26
 */
public class ApacheZipUtil {

    private static byte[] _byte = new byte[1024] ;

    /**
     * 对.zip文件进行解压缩
     *
     * @author LIUFEI YANG
     * @date 2019/10/23 15:29
     * @param zipFileDir  解压缩文件
     * @param descDir  压缩的目标地址
     * @return void
     */
    @SuppressWarnings("rawtypes")
    public static void unZipFile(String zipFileDir, String descDir) {
        ZipFile zipFile = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            zipFile = new ZipFile(new File(zipFileDir) , "GBK") ;
            for(Enumeration entries = zipFile.getEntries(); entries.hasMoreElements() ; ){
                ZipEntry entry = (ZipEntry)entries.nextElement() ;
                File file = new File(descDir + File.separator + entry.getName());
                if( entry.isDirectory() ){
                    file.mkdirs();
                }else{
                    File parent = file.getParentFile() ;
                    if( !parent.exists() ){
                        parent.mkdirs() ;
                    }
                    inputStream = zipFile.getInputStream(entry);
                    outputStream = new FileOutputStream(file) ;
                    int len = 0 ;
                    while( (len = inputStream.read(_byte)) > 0){
                        outputStream.write(_byte, 0, len);
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                }
            }
        } catch (IOException ignored) {
        } finally {
            try {
                if (zipFile != null){
                    zipFile.close();
                }
                if (outputStream != null){
                    outputStream.close();
                }
                if (inputStream != null){
                    inputStream.close();
                }
            } catch (IOException ignored) {}
        }
    }
}
