package com.sailing.dataextraction.common.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class SUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(SUtils.class);

	/**
	 * getIpAddress[解析ip地址]
	 * 创建人:  LIULEI
	 * 创建时间: Jul 25, 2017 4:49:03 PM
	 *
	 * @Title: getIpAddress
	 * @param request
	 * @return
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public static String getIpAddress(HttpServletRequest request) {
	    String ip = request.getHeader("x-forwarded-for");
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		    ip = request.getHeader("X-Real-IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getHeader("Proxy-Client-IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getHeader("HTTP_CLIENT_IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getRemoteAddr();
	    }
	    return ip;
	  }

	/**
	 * splitString[按字节截取字符串方法]
	 * 创建人:  LIULEI
	 * 创建时间: Dec 16, 2016 11:17:13 AM
	 *
	 * @Title: splitString
	 * @param str    要截取的字符串
	 * @param len    要截取的长度
	 * @param elide  省略符一般传递""
	 * @return
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
    public static String splitString(String str, int len, String elide)
    {
        if (str == null || len < 3)
        {
            return "";
        }
        byte[] strByte = str.getBytes();
        int strLen = strByte.length;
        if (len >= strLen || len < 1)
        {
            return str;
        }
        int count = 0;
        int index = 0;
        boolean flag = false;
        for (int i = len - 1; i > len - 4; i--)
        {
            int value = (int) strByte[i];
            if (i == len - 1 && value > 0)
            {
                flag = true;
                break;
            }
            index++;
            if (value < 0)
            {
                count++;
            }
            if (index == 3)
            {
                if (index == count)
                {
                    flag = true;
                }
                break;
            }
        }
        len = flag ? len : len - count%3;
        return new String(strByte, 0, len) + elide.trim();
    }

	/** 根据查询条件获取设备信息
	 * @param operationCondition
	 * @param operationIndex
	 * @param key
	 * @return
	 */
	public static String getDeviceNo(String operationCondition, String operationIndex, String key) {

		try {
			List<Map> conditon = JSON.parseArray(operationCondition, Map.class);
			int index = 0;
			String[] indexArr = operationIndex.replaceAll("，", ",").split(",");
			for (String indexStr : indexArr
					) {
				if (indexStr.contains(key) && indexStr.contains("=")) {
					index = Integer.parseInt(indexStr.split("=")[0]);
					return String.valueOf(conditon.get(index).values());
				}
			}
		}
		catch (Exception e){
			LOGGER.debug("查询设备编号失败!");
		}
		return "";
	}

	/**获取UUID
	 * @return
	 */
	public static String getUUID()
	{
		return UUID.randomUUID().toString().replaceAll("-", "");
	}


    /**
     * 创建文件（当目录不存在时将创建目录）
     *
     * @author LIUFEI YANG
     * @param fileName 文件名称
     * @param fileDir 文件目录
     * @return 文件路径
     */
	public static String createFile(String fileName, String fileDir){
        String excelPath = fileDir+ File.separator+fileName;
        File file = new File(fileDir);
        if (!file.isDirectory()){
            file.mkdirs();
        }
        File excelFile = new File(excelPath);
        if (!excelFile.isFile()){
            try {
                excelFile.createNewFile();
            } catch (IOException e) {
                LOGGER.error("创建文件"+excelPath+"失败");
            }
        }
        return excelPath;
	}

	public static void deleteFile(String filePath){
	    File file = new File(filePath);
	    if (file.exists()){
            try {
                deleteFolder(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteFolder(File folder) throws Exception {
        if (!folder.exists()) {
            throw new Exception("文件不存在");
        }
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    //递归直到目录下没有文件
                    deleteFolder(file);
                } else {
                    //删除
                    file.delete();
                }
            }
        }
        //删除
        folder.delete();
    }

    /**
     * 压缩文件
     * @param dirPath 压缩源文件路径
     * @param zipFileName 压缩目标文件路径
     * */
    public static String compress(String dirPath,String zipFileName){
        zipFileName = dirPath + "/" + zipFileName + ".zip";//添加文件的后缀名

        File dirFile = new File(dirPath);
        List<File> fileList = getAllFile(dirFile);

        byte[] buffer = new byte[512];
        ZipEntry zipEntry = null;
        int readLength = 0;     //每次读取出来的长度

        try {
            // 对输出文件做CRC32校验
            CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(zipFileName), new CRC32());
            ZipOutputStream zos = new ZipOutputStream(cos);

            for(File file:fileList){

                if(file.isFile()){   //若是文件，则压缩文件
                    zipEntry = new ZipEntry(getRelativePath(dirPath,file));
                    zipEntry.setSize(file.length());
                    zipEntry.setTime(file.lastModified());
                    zos.putNextEntry(zipEntry);

                    InputStream is = new BufferedInputStream(new FileInputStream(file));

                    while ((readLength = is.read(buffer,0,512)) != -1){
                        zos.write(buffer,0, readLength);
                    }
                    is.close();
                    System.out.println("file compress:" + file.getCanonicalPath());
                }else {     //若是空目录，则写入zip条目中

                    zipEntry = new ZipEntry(getRelativePath(dirPath, file));
                    zos.putNextEntry(zipEntry);
                    System.out.println("dir compress: " + file.getCanonicalPath() + "/");
                }
            }
            zos.close();  //最后得关闭流，不然压缩最后一个文件会出错
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipFileName;
    }

    /**
     * 得到源文件路径的所有文件
     * @param dirFile 压缩源文件路径
     * */
    public static List<File> getAllFile(File dirFile){

        List<File> fileList = new ArrayList<File>();

        File[] files = dirFile.listFiles();
        for(File file : files){//文件
            if(file.isFile()){
                fileList.add(file);
                System.out.println("add file:" + file.getName());
            }else {//目录
                if(file.listFiles().length != 0){//非空目录
                    fileList.addAll(getAllFile(file));//把递归文件加到fileList中
                }else {//空目录
                    fileList.add(file);
                    System.out.println("add empty dir:" + file.getName());
                }
            }
        }
        return fileList;
    }


    /**
     * 获取相对路径
     * @param dirPath 源文件路径
     * @param file 准备压缩的单个文件
     * */
    public static String getRelativePath(String dirPath, File file){
        File dirFile = new File(dirPath);
        String relativePath = file.getName();

        while (true){
            file = file.getParentFile();
            if(file == null){
                break;
            }
            if(file.equals(dirFile)){
                break;
            }
            else {
                relativePath = file.getName() + "/" + relativePath;
            }
        }
        return relativePath;
    }

    /**
     * MultipartFile 转 File
     * @param file
     * @return
     */
    public static File inputStreamToFile(MultipartFile file) {
        File fileInfo=null;
        if(file==null || file.isEmpty()){
            return fileInfo;
        }
        try {
            InputStream ins = file.getInputStream();
            fileInfo=new File(file.getOriginalFilename());
            OutputStream os = new FileOutputStream(fileInfo);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            LOGGER.info("文件转换异常：=>{}",e.getMessage());
        }
        return fileInfo;
    }

    /**
     * 实体类转Map
     * @author Liufei Yang
     * @date 2020/5/7 15:45
     * @param object
     * @return java.utils.Map<java.lang.String,java.lang.Object>
     **/
    public static Map<String, Object> entityToMap(Object object) {
        Map<String, Object> map = new HashMap();
        for (Field field : object.getClass().getDeclaredFields()){
            try {
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                Object o = field.get(object);
                map.put(field.getName(), o);
                field.setAccessible(flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
