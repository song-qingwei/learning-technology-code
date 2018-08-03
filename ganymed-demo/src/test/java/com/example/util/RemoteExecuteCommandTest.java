package com.example.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author SongQingWei
 * @date 2018年07月30 10:25
 */
public class RemoteExecuteCommandTest {

    private static Logger log = LoggerFactory.getLogger(RemoteExecuteCommandTest.class);

    public static void main(String[] args) throws IOException {

        // 待执行的命令
        String cmd = "cd /share/dadimedia/dcp/user && ll |grep Input";
        // 执行命令后返回值的校验
        String checkField = "root";

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10,
                200,
                15000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(500)
        );
        File file = ResourceUtils.getFile("classpath:theater_success.txt");
        List<String> readLines = FileUtils.readLines(file, Charset.forName("UTF-8"));
        for (String information : readLines) {
            if (StringUtils.isNotBlank(information)) {
                String[] split = information.split(",");
                String ip = split[0];
                String theaterCode = split[1];
                String userName = split[2];
                String userPwd = split[3];
                String theaterName = split[4];
                executor.execute(new RemoteExecuteCommandTest().new MyCase(ip, userName, userPwd, cmd, checkField));
            }
        }
        executor.shutdown();
    }

    public class MyCase extends Thread {

        private String ip;
        private String userName;
        private String userPwd;
        private String cmd;
        private String checkField;

        public MyCase(String ip, String userName, String userPwd, String cmd, String checkField) {
            this.ip = ip;
            this.userName = userName;
            this.userPwd = userPwd;
            this.cmd = cmd;
            this.checkField = checkField;
        }

        @Override
        public void run() {
            RemoteExecuteCommand command = new RemoteExecuteCommand(ip, userName, userPwd);
            try {
                String execute = command.execute(cmd);
                System.out.println(execute);
                if (StringUtils.isNotBlank(execute) && execute.contains(checkField)) {
                    log.info("IP:{}执行成功", ip);
                } else {
                    log.warn("IP:{}执行失败", ip);
                }
            } catch (IOException e) {
                log.error("执行命令错误：{}", e.getMessage());
            }
        }
    }
}