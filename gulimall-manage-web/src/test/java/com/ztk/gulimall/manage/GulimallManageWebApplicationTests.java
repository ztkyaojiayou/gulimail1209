package com.ztk.gulimall.manage;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
class GulimallManageWebApplicationTests {

    @Test
    public void contextLoads() throws IOException, MyException {
        //配置fdfs的全局链接地址
       String tracker = GulimallManageWebApplicationTests.class.getResource("/tracker.conf").getPath();//获得配置文件的路径
        ClientGlobal.init(tracker);

        TrackerClient trackerClient = new TrackerClient();
        //获取到一个trackerServer实例
        TrackerServer trackerServer = trackerClient.getConnection();
        //通过trackerServer获得一个Storage链接客户端
        StorageClient storageClient = new StorageClient(trackerServer, null);
        String[] uploadinfos = storageClient.upload_file("f:/test1.png", "png", null);
        String url = "http://192.168.16.129";

        for (String uploadinfo:uploadinfos) {
            url += "/" + uploadinfo;

            System.out.println(url);
            //运行结果如下：
            //group1
            //M00/00/00/wKgQgV366PmAax3CAAPVx8nuHtk805.png

        }
    }

}
