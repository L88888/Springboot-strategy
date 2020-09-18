package com.sailing.dataextraction.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Liufei Yang
 * @version 1.0
 * @className FtpClientUtil
 * @description FTP客户端工具类
 * @date 2020/8/24 16:48
 **/
@Slf4j
@Component
public class FtpClientUtil {

    /** 主机 */
    @Value("${ftp.hostname}")
    private String hostname;
    /** 端口 */
    @Value("${ftp.port}")
    private String port;
    /** 用户 */
    @Value("${ftp.user}")
    private String user;
    /** 密码 */
    @Value("${ftp.password}")
    private String password;
    /** 传输模式 */
    @Value("${ftp.transfer.mode}")
    private boolean transferMode;
    /** 编码集 */
    @Value("${ftp.control.encoding}")
    private String controlEncoding;
    /** 目录 */
    @Value("${ftp.directory}")
    private String directory;
    @Value("${ftp.dirSymbol}")
    private String dirSymbol;

    private Set<String> arrDirs;

    private FTPClient ftpClient;

    public FtpClientUtil(){
        this.arrDirs = new HashSet<>();
    }

    public Set<String> initArrDirs(){
        this.arrDirs = new HashSet<>();
        return this.arrDirs;
    }

    /**
     * 创建FTPClient
     * @return FTPClient
     */
    public void initFtpClient(){
        // 连接FTP服务器
        try {
            if (!this.checkFTPClient()){
                this.ftpClient = new FTPClient();
            } else {
                return;
            }
            this.ftpClient.connect(hostname);
            // 登陆FTP服务器
            this.ftpClient.login(user, password);
            //验证FTP服务器是否登录成功
            int replyCode = this.ftpClient.getReplyCode();
            if(!FTPReply.isPositiveCompletion(replyCode)){
                log.info("登录验证失败");
                return ;
            }
            // 设置编码集
            this.ftpClient.setControlEncoding(controlEncoding);
            this.ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 设置传输模式
            if (transferMode){
                // 主动模式
                this.ftpClient.enterLocalActiveMode();
            } else {
                // 被动模式
                this.ftpClient.enterLocalPassiveMode();
            }
            // 设置默认工作目录
            this.ftpClient.changeWorkingDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Ftp服务器上文件
     * @param ftpdir Ftp 目录
     * @return
     */
    public List<FTPFile> getFiles(String ftpdir){
        List<FTPFile> result = new ArrayList<>();
        try {
            if (!this.checkFTPClient()){
                this.initFtpClient();
            }
            if (StringUtils.isNotEmpty(ftpdir)){
                this.ftpClient.changeWorkingDirectory(ftpdir);
            }
            FTPFile[] ftpFiles = this.ftpClient.listFiles();
            for (FTPFile ftpFile : ftpFiles) {
                String fileName = ftpFile.getName();
                if (ftpFile.isFile() && ".zip".equals(fileName.substring(fileName.lastIndexOf("."), fileName.length()))){
                    result.add(ftpFile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.colseFtpClient();
        }
        return result;
    }

    /**
     * 递归遍历所有目录，查找拥有指定缀的文件目录
     * @param ftpDir 目录
     * @param ext 文件后缀
     * @return
     * @throws IOException
     */
    public void listDir(String ftpDir, String ext){
        try {
            if (!this.checkFTPClient()){
                this.initFtpClient();
            }
            log.info("FTP目录：{}", ftpDir);
            //更换目录到当前目录
            boolean state = this.ftpClient.changeWorkingDirectory(ftpDir);
            if (!state){
                return;
            }

            FTPFile[] files = this.ftpClient.listFiles();
            for (FTPFile file : files) {
                if (file.isFile()) {
                    if (StringUtils.isNotEmpty(ext)){
                        if (file.getName().endsWith(ext)){
                            this.arrDirs.add(ftpDir);
                        }
                    } else {
                        this.arrDirs.add(ftpDir);
                    }
                } else if (file.isDirectory()) {
                    // 需要加此判断。否则，ftp默认将‘项目文件所在目录之下的目录（./）’与‘项目文件所在目录向上一级目录下的目录（../）’都纳入递归，这样下去就陷入一个死循环了。需将其过滤掉。
                    if (!".".equals(file.getName()) && !"..".equals(file.getName())) {
                        listDir(ftpDir.concat(dirSymbol).concat(file.getName()), ext);
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     * @param saveDir 文件保存目录
     * @param ftpFile FTPFile
     * @return 文件路径
     */
    public String downloadFile(String ftpdir, String saveDir, FTPFile ftpFile){
        String filePath = "";
        OutputStream ops = null;
        try {
            if (!this.checkFTPClient()){
                this.initFtpClient();
            }
            filePath = SUtils.createFile(ftpFile.getName(), saveDir);
            File file = new File(filePath);
            ops = new FileOutputStream(file);
            this.ftpClient.changeWorkingDirectory(ftpdir);
            this.ftpClient.retrieveFile(ftpFile.getName(), ops);
            ops.close();
        } catch (IOException  e){
            filePath = "";
            SUtils.deleteFile(filePath);
            e.printStackTrace();
        } finally {
            try {
                ops.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.colseFtpClient();
        }
        return filePath;
    }

    /**
     * 删除文件
     * @param ftpDir Ftp目录
     * @param fileName 文件名称
     * @return
     */
    public boolean removeFile(String ftpDir, String fileName){
        boolean result = true;
        try {
            if (!this.checkFTPClient()){
                this.initFtpClient();
            }
            this.ftpClient.changeWorkingDirectory(ftpDir);
            result = this.ftpClient.deleteFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.colseFtpClient();
        }
        return result;
    }

    /**
     * 关闭FTP连接
     */
    public void colseFtpClient(){
        if (this.ftpClient != null && this.ftpClient.isConnected()) {
            try {
                this.ftpClient.disconnect();
                this.ftpClient = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查FTPClient状态
     * return Statue
     * @throws IOException
     */
    public boolean checkFTPClient() {
        try {
            if (this.ftpClient != null && this.ftpClient.isConnected()){
                log.info("FTPClient状态：{}", ftpClient.isConnected());
                return true;
            } else {
                log.info("FTPClient状态：{}", false);
                return false;
            }
        }catch (Exception e){
            this.ftpClient = null;
            return false;
        }
    }
}
