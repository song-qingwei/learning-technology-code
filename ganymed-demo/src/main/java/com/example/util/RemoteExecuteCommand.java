package com.example.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.lang3.StringUtils;
import org.apache.sshd.common.file.SshFile.Permission;
import org.apache.sshd.common.scp.ScpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.EnumSet;

/**
 * @author SongQingWei
 * @date 2018年07月30 09:29
 */
public class RemoteExecuteCommand {

    private static Logger log = LoggerFactory.getLogger(RemoteExecuteCommand.class);
    private static final String  DEFAULT_CHART="UTF-8";
    private Connection connection;
    private String ip;
    private static final Integer PORT = 22;
    private String userName;
    private String userPwd;

    public RemoteExecuteCommand(String ip, String userName, String userPwd) {
        this.ip = ip;
        this.userName = userName;
        this.userPwd = userPwd;
    }

    /**
     * 登录
     * @return boolean
     */
    private boolean login() {
        boolean flag = false;
        try {
            connection = new Connection(ip, PORT);
            // 创建连接
            connection.connect();
            // 认证
            flag = connection.authenticateWithPassword(userName, userPwd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 远程执行shell脚本或者命令
     * @param cmd 命令
     * @return 控制台内容
     */
    public String execute(String cmd) throws IOException {
        String result = "";
        if (login()) {
            // 打开一个会话
            Session session = connection.openSession();
            // 执行命令
            session.execCommand(cmd);
            // 如果为得到标准输出为空，说明脚本执行出错了
            result = processStdout(session.getStdout());
            if (StringUtils.isBlank(result)) {
                result = processStdout(session.getStderr());
            }
            connection.close();
            session.close();
        } else {
            log.warn("IP:{}登录失败", ip);
        }
        return result;
    }

    /**
     * 上传文件
     * @param localFilePath 本地文件路径 如：D:/test/1.jpg
     * @param saveFilePath 远程服务器保存路径 /root/
     * @throws IOException e
     */
    public void transfer(String localFilePath, String saveFilePath) throws IOException {
        File file = new File(localFilePath);
        if (!file.exists()) {
            throw new FileNotFoundException("本地文件不存在");
        }
        if (login()) {
            SCPClient client = new SCPClient(connection);
            String mode = ScpHelper.toOctalPerms(
                    EnumSet.of(
                            Permission.OthersRead, Permission.OthersWrite, Permission.OthersExecute,
                            Permission.GroupRead, Permission.GroupWrite, Permission.GroupExecute,
                            Permission.UserRead, Permission.UserWrite, Permission.UserExecute
                    )
            );

            client.put(localFilePath, file.length(), saveFilePath, mode);
            connection.close();
            log.info("IP:{}上传文件>>>OK");
        } else {
            log.warn("IP:{}登录失败", ip);
        }
    }

    /**
     * 解析脚本执行返回的结果集
     * @param in 输入流对象
     * @return 以纯文本的格式返回
     */
    private String processStdout(InputStream in) throws IOException {
        InputStream stdout = new StreamGobbler(in);
        StringBuilder builder = new StringBuilder();
        BufferedReader br = null;
        InputStreamReader streamReader = null;
        try {
            streamReader = new InputStreamReader(stdout, DEFAULT_CHART);
            br = new BufferedReader(streamReader);
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stdout.close();
            if (br != null) {
                br.close();
            }
            if (streamReader != null) {
                streamReader.close();
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) throws IOException {
        RemoteExecuteCommand command = new RemoteExecuteCommand("172.26.14.7", "root", "BJoriTms2018MG&Mike");
        String execute = command.execute("ll /share/dadimedia/dcp/user/ |grep Input");
        System.out.println(execute);
    }
}
