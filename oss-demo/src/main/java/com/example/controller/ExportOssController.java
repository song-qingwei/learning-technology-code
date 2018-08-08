package com.example.controller;

import com.aliyun.oss.OSSClient;
import com.example.enums.DcpTypeEnum;
import com.example.util.AliyunOSSClientUtil;
import com.example.util.DcpTool;
import com.example.util.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author SongQingWei
 * @date 2018年07月30 14:11
 */
@RestController
@Slf4j
public class ExportOssController {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String SUFFIX = " 00:00:00";
    private static final String SAVE_PATH = "D:\\test\\test\\";

    @RequestMapping("/export_oss")
    public String exportOss(@RequestParam(value = "path") String path,
                            @RequestParam(value = "start") String start,
                            @RequestParam(value = "end") String end) throws Exception {
        start = start.trim() + SUFFIX;
        end = end.trim() + SUFFIX;
        log.info("输入路径:{}, 开始时间:{}, 结束时间:{}", path, start, end);
        Date startTime = format.parse(start);
        Date endTime = format.parse(end);
        Long startLong = startTime.getTime();
        Long endLong = endTime.getTime();
        if (endLong < startLong) {
            return "日期范围出错";
        }
        File file = new File(path);
        if (!file.exists()) {
            return path + "路径不存在";
        }
        File[] files = file.listFiles();
        List<String> successList = new ArrayList<>();
        List<String> failedList = new ArrayList<>();
        if (files != null && files.length != 0) {
            List<File> list = new ArrayList<>();
            for (File f : files) {
                long modified = f.lastModified();
                if (modified >= startLong && modified <= endLong) {
                    list.add(f);
                }
            }
            this.uploadOss(list, successList, failedList);
        } else {
            return path + "下不存在dcp数据包";
        }
        return path + "<br>成功UUID：" + successList.toString() + "<br>失败UUID：" + failedList.toString();
    }

    @RequestMapping("/check_dcp")
    public String checkDcpFromOss(@RequestParam(value = "path") String path,
                            @RequestParam(value = "start") String start,
                            @RequestParam(value = "end") String end) throws Exception {
        start = start.trim() + SUFFIX;
        end = end.trim() + SUFFIX;
        log.info("输入路径:{}, 开始时间:{}, 结束时间:{}", path, start, end);
        Date startTime = format.parse(start);
        Date endTime = format.parse(end);
        Long startLong = startTime.getTime();
        Long endLong = endTime.getTime();
        if (endLong < startLong) {
            return "日期范围出错";
        }
        File file = new File(path);
        if (!file.exists()) {
            return path + "路径不存在";
        }
        File[] files = file.listFiles();
        List<String> successList = new ArrayList<>();
        List<String> failedList = new ArrayList<>();
        if (files != null && files.length != 0) {
            List<String> list = new ArrayList<>();
            for (File f : files) {
                long modified = f.lastModified();
                if (modified >= startLong && modified <= endLong) {
                    list.add(f.getName());
                }
            }
            this.downloadOss(list, successList, failedList);
        } else {
            return path + "下不存在dcp数据包";
        }
        return path + "<br>成功UUID：" + successList.toString() + "<br>失败UUID：" + failedList.toString();
    }

    @RequestMapping("/read_file_zip")
    public String readFile(@RequestParam(value = "uuidPath") String uuidPath,
                           @RequestParam(value = "localPath") String localPath) throws Exception {
        File uuidFile = new File(uuidPath);
        if (!uuidFile.exists()) {
            return uuidPath + "路径不存在";
        }
        File localFile = new File(localPath);
        if (!localFile.exists()) {
            return localPath + "路径不存在";
        }
        if (!localPath.endsWith("/")) {
            localPath = localPath + "/";
        }
        List<String> successList = new ArrayList<>();
        List<String> failedList = new ArrayList<>();
        try {
            List<String> list = FileUtils.readLines(uuidFile, "UTF-8");
            for (String uuid : list) {
                if (StringUtils.isNotBlank(uuid)) {
                    List<File> files = new ArrayList<>();
                    File file = new File(localPath + uuid);
                    if (file.exists()) {
                        File f = new File(localPath + uuid + ".zip");
                        if (f.exists()) {
                            f.delete();
                        }
                        files.add(file);
                    }
                    this.uploadOss(files, successList, failedList);
                    /*if (file.exists()) {
                        OSSClient client = AliyunOSSClientUtil.initClient();
                        boolean b = AliyunOSSClientUtil.uploadSingleFile(client, DcpTypeEnum.dcp.getMsg(), file);
                        if (b) {
                            if (!successList.contains(uuid)) {
                                successList.add(uuid);
                            }
                        }
                        AliyunOSSClientUtil.closeClient(client);
                    } else {
                        log.warn(localPath + uuid + ".zip 不存在");
                        if (!failedList.contains(uuid)) {
                            failedList.add(uuid);
                        }
                    }*/
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "成功UUID：" + successList.toString() + "<br>失败UUID：" + failedList.toString();
    }

    @RequestMapping("/only_upload_file_zip")
    public String onlyUploadFile(@RequestParam(value = "uuidPath") String uuidPath,
                           @RequestParam(value = "localPath") String localPath) throws Exception {
        File uuidFile = new File(uuidPath);
        if (!uuidFile.exists()) {
            return uuidPath + "路径不存在";
        }
        File localFile = new File(localPath);
        if (!localFile.exists()) {
            return localPath + "路径不存在";
        }
        if (!localPath.endsWith("/")) {
            localPath = localPath + "/";
        }
        List<String> successList = new ArrayList<>();
        List<String> failedList = new ArrayList<>();
        try {
            List<String> list = FileUtils.readLines(uuidFile, "UTF-8");
            OSSClient client = AliyunOSSClientUtil.initClient();
            for (String uuid : list) {
                if (StringUtils.isNotBlank(uuid)) {
                    File file = new File(localPath + uuid + ".zip");
                    if (file.exists()) {
                        boolean b = AliyunOSSClientUtil.uploadSingleFile(client, DcpTypeEnum.zip.getMsg(), file);
                        if (b) {
                            successList.add(uuid);
                        }
                    } else {
                        failedList.add(uuid);
                    }
                }
            }
            AliyunOSSClientUtil.closeClient(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "成功UUID：" + successList.toString() + "<br>失败UUID：" + failedList.toString();
    }

    private void uploadOss(List<File> files, List<String> successList, List<String> failedList) throws Exception {
        for (File f : files) {
            OSSClient client = AliyunOSSClientUtil.initClient();
            // 过滤掉文件
            if (f.isDirectory()) {
                File[] listFiles = f.listFiles();
                if (listFiles != null && listFiles.length != 0) {
                    List<File> list = new ArrayList<>();
                    for (File file1 : listFiles) {
                        // 过滤掉文件夹，防止嵌套
                        if (file1.isFile()) {
                            list.add(file1);
                        } else {
                            log.warn("{} 出现嵌套", file1.getAbsolutePath());
                        }
                    }
                    AliyunOSSClientUtil.uploadMultiFile(client, DcpTypeEnum.dcp.getMsg() + f.getName(), list);
                    // 生成ZIP
                    ZipUtil.zip(f.getAbsolutePath() + ".zip", f.getAbsolutePath(), false);
                    File file1 = new File(f.getAbsolutePath() + ".zip");
                    AliyunOSSClientUtil.uploadSingleFile(client, DcpTypeEnum.zip.getMsg(), file1);
                    // 删除ZIP
                    file1.delete();
                    if (!successList.contains(f.getName())) {
                        successList.add(f.getName());
                    }
                } else {
                    log.warn(f.getAbsolutePath() + "下无dcp文件");
                    if (!failedList.contains(f.getName())) {
                        failedList.add(f.getName());
                    }
                }
            } else {
                log.warn(f.getAbsolutePath() + "不是文件夹");
                if (!failedList.contains(f.getName())) {
                    failedList.add(f.getName());
                }
            }
            AliyunOSSClientUtil.closeClient(client);
        }
    }

    private void downloadOss(List<String> list, List<String> successList, List<String> failedList) throws Exception {
        for (String uuid : list) {
            OSSClient client = AliyunOSSClientUtil.initClient();
            boolean b1 = false, b2 = false;
            DcpTool dt = new DcpTool();
            if (AliyunOSSClientUtil.checkFileExist(client, DcpTypeEnum.zip.getMsg() + uuid + ".zip")) {
                boolean b = AliyunOSSClientUtil.downloadObject(client, DcpTypeEnum.zip.getMsg() + uuid + ".zip", SAVE_PATH + uuid + ".zip");
                if (b) {
                    ZipUtil.unZip(SAVE_PATH + uuid + ".zip", SAVE_PATH + uuid, false);
                    if (dt.validateDCP(SAVE_PATH + uuid)) {
                        log.info("{} ZIP校验通过", uuid);
                        FileUtils.deleteDirectory(new File(SAVE_PATH + uuid));
                        b1 = true;
                    } else {
                        log.warn("{} ZIP校验失败", uuid);
                        if (!failedList.contains(uuid)) {
                            failedList.add(uuid);
                        }
                    }
                }
                File file = new File(SAVE_PATH + uuid + ".zip");
                if (file.exists()) {
                    file.delete();
                }
            } else {
                log.warn("{} 在OSS上不存在ZIP", uuid);
                if (!failedList.contains(uuid)) {
                    failedList.add(uuid);
                }
            }
            if (AliyunOSSClientUtil.checkFileExist(client, DcpTypeEnum.dcp.getMsg() + uuid + "/ASSETMAP")) {
                File file = new File(SAVE_PATH + uuid);
                if (!file.exists()) {
                    file.mkdirs();
                }
                List<String> readFiles = AliyunOSSClientUtil.readFiles(client, DcpTypeEnum.dcp.getMsg() + uuid);
                int i = 0;
                for (String fileName : readFiles) {
                    boolean b = AliyunOSSClientUtil.downloadObject(client, fileName, SAVE_PATH + uuid + fileName.substring(fileName.lastIndexOf("/")));
                    if (b) {
                        i++;
                    }
                }

                if (i == readFiles.size()) {
                    if (dt.validateDCP(SAVE_PATH + uuid)) {
                        log.info("{} DCP校验通过", uuid);
                        FileUtils.deleteDirectory(new File(SAVE_PATH + uuid));
                        b2 = true;
                    } else {
                        log.warn("{} 校验DCP失败", uuid);
                        if (!failedList.contains(uuid)) {
                            failedList.add(uuid);
                        }
                    }
                }
            }
            if (b1 && b2) {
                if (!successList.contains(uuid)) {
                    successList.add(uuid);
                }
            } else {
                if (!failedList.contains(uuid)) {
                    failedList.add(uuid);
                }
            }
            AliyunOSSClientUtil.closeClient(client);
        }
    }
}
