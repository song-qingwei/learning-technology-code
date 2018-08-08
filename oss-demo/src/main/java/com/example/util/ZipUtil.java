package com.example.util;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.Enumeration;

/**
 * @author SongQingWei
 * @date 2018年07月30 14:34
 */
public class ZipUtil {


    private static final int BUFFERED_SIZE = 1024;

    /**
     * 压缩文件
     *
     * @param zipFileName
     *            保存的压缩包文件路径
     * @param filePath
     *            需要压缩的文件夹或者文件路径
     * @param isDelete
     *            是否删除源文件
     * @throws Exception
     */
    public static void zip(String zipFileName, String filePath, boolean isDelete) throws Exception {
        zip(zipFileName, new File(filePath), isDelete);
    }

    /**
     * 压缩文件
     *
     * @param zipFileName
     *            保存的压缩包文件路径
     * @param inputFile
     *            需要压缩的文件夹或者文件
     * @param isDelete
     *            是否删除源文件
     * @throws Exception
     */
    public static void zip(String zipFileName, File inputFile, boolean isDelete) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        if (!inputFile.exists()) {
            throw new FileNotFoundException("在指定路径未找到需要压缩的文件！");
        }
        zip(out, inputFile, "", isDelete);
        out.close();
    }

    /**
     * 递归压缩方法
     *
     * @param out
     *            压缩包输出流
     * @param inputFile
     *            需要压缩的文件
     * @param base
     *            压缩的路径
     * @param isDelete
     *            是否删除源文件
     * @throws Exception
     */
    private static void zip(ZipOutputStream out, File inputFile, String base, boolean isDelete) throws Exception {
        if (inputFile.isDirectory()) { // 如果是目录
            File[] inputFiles = inputFile.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < inputFiles.length; i++) {
                zip(out, inputFiles[i], base + inputFiles[i].getName(), isDelete);
            }
        } else { // 如果是文件
            if (base.length() > 0) {
                out.putNextEntry(new ZipEntry(base));
            } else {
                out.putNextEntry(new ZipEntry(inputFile.getName()));
            }
            FileInputStream in = new FileInputStream(inputFile);
            try {
                int len;
                byte[] buff = new byte[BUFFERED_SIZE];
                while ((len = in.read(buff)) != -1) {
                    out.write(buff, 0, len);
                }
            } catch (IOException e) {
                throw e;
            } finally {
                in.close();
            }
        }
        if (isDelete) {
            inputFile.delete();
        }
    }

    /**
     * 解压缩
     *
     * @param zipFilePath
     *            压缩包路径
     * @param fileSavePath
     *            解压路径
     * @param isDelete
     *            是否删除源文件
     * @throws Exception
     */
    public static void unZip(String zipFilePath, String fileSavePath, boolean isDelete) throws Exception {
        try {
            (new File(fileSavePath)).mkdirs();
            File f = new File(zipFilePath);
            if ((!f.exists()) && (f.length() <= 0)) {
                throw new Exception("要解压的文件不存在!");
            }
            ZipFile zipFile = new ZipFile(f);
            String strPath, gbkPath, strtemp;
            File tempFile = new File(fileSavePath);// 从当前目录开始
            strPath = tempFile.getAbsolutePath();// 输出的绝对位置
            Enumeration<ZipEntry> e = zipFile.getEntries();
            while (e.hasMoreElements()) {
                org.apache.tools.zip.ZipEntry zipEnt = e.nextElement();
                gbkPath = zipEnt.getName();
                if (zipEnt.isDirectory()) {
                    strtemp = strPath + File.separator + gbkPath;
                    File dir = new File(strtemp);
                    dir.mkdirs();
                    continue;
                } else {
                    // 读写文件
                    InputStream is = zipFile.getInputStream(zipEnt);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    gbkPath = zipEnt.getName();
                    strtemp = strPath + File.separator + gbkPath;
                    // 建目录
                    String strsubdir = gbkPath;
                    for (int i = 0; i < strsubdir.length(); i++) {
                        if (strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {
                            String temp = strPath + File.separator + strsubdir.substring(0, i);
                            File subdir = new File(temp);
                            if (!subdir.exists()) {
                                subdir.mkdir();
                            }
                        }
                    }
                    FileOutputStream fos = new FileOutputStream(strtemp);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int len;
                    byte[] buff = new byte[BUFFERED_SIZE];
                    while ((len = bis.read(buff)) != -1) {
                        bos.write(buff, 0, len);
                    }
                    bis.close();
                    is.close();
                    bos.close();
                    fos.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        if (isDelete) {
            new File(zipFilePath).delete();
        }
    }

    public static void main(String[] args) {
        ZipUtil cpr = new ZipUtil();
        try {
//				cpr.zip("C:\\Users\\SongQingWei\\Desktop\\84f5db0e-8293-43c1-8c5a-86f97f8f66c8.zip", "C:\\Users\\SongQingWei\\Desktop\\84f5db0e-8293-43c1-8c5a-86f97f8f66c8", false);
            ZipUtil.unZip("C:\\Users\\SongQingWei\\Desktop\\05233823-715e-4b2b-881a-843708697162.zip", "C:\\Users\\SongQingWei\\Desktop\\05233823-715e-4b2b-881a-843708697162", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(">>>");
    }

}
