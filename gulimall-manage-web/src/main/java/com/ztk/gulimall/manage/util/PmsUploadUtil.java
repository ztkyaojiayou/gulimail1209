package com.ztk.gulimall.manage.util;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PmsUploadUtil {
    public static String uploadImage(MultipartFile multipartFile) {//MultipartFile类用于接收从浏览器（后台管理系统）传过来的文件
        String imUrl ="http://192.168.16.129";

        //配置fdfs的全局链接地址
        String tracker = PmsUploadUtil.class.getResource("/tracker.conf").getPath();//获得配置文件的路径
        try {
            ClientGlobal.init(tracker);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TrackerClient trackerClient = new TrackerClient();
        //获取到一个trackerServer实例
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //通过trackerServer获得一个Storage链接客户端
        StorageClient storageClient = new StorageClient(trackerServer, null);
        try {
            byte[] bytes = multipartFile.getBytes();//获得要上传文件的二进制的对象
            String originalFilename = multipartFile.getOriginalFilename();//获取要上传的文件的全名
            int i = originalFilename.lastIndexOf(".");//获取要上传的文件的全名的最后一个冒号，用于获取文件的后缀名（文件类型名）
            String extName = originalFilename.substring(i + 1);//通过substring（截取字符串）方法获取文件的后缀名（文件类型名）

            String[] uploadinfos = storageClient.upload_file(bytes, extName, null);

            for (String uploadinfo : uploadinfos) {
                imUrl += "/" + uploadinfo;

                System.out.println(imUrl);
                //运行结果如下：
                //http://192.168.16.129/group1
                //http://192.168.16.129/group1/M00/00/00/wKgQgV366lSAC3TnAAPVx8nuHtk161.png

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imUrl;
    }
}
