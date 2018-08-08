package com.example.util;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.aliyuncs.exceptions.ClientException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author SongQingWei
 * @date 2018年07月30 14:04
 */
public class AliyunOSSClientUtil {


    // 阿里云API的外网域名
    private static final String OSS_ENDPOINT_OUTER = "http://oss-cn-beijing.aliyuncs.com";
    // 阿里云API的内网域名
    private static final String OSS_ENDPOINT_INNER = "http://oss-cn-beijing-internal.aliyuncs.com";
    // 当前使用域名
    private static final String OSS_ENDPOINT = OSS_ENDPOINT_INNER;
    // 阿里云API的密钥Access Key ID
    private static final String ACCESS_KEY_ID = "LTAI2znY4y7LgT1a";
    // 阿里云API的密钥Access Key Secret
    private static final String ACCESS_KEY_SECRET = "pAKIWdSN0R1MnXkEGVTiP4cBc5x4bO";
    // 阿里云API的bucket名称
    private static final String BACKET_NAME = "guanggao-chengxin";
    // 阿里云API的文件夹名称
    private static final String FOLDER = "dcp/6a4c73ae-5083-40fb-9f85-afccc9e50202";
    // 回调服务器地址
    private static final String CALLBACKURL = "http://oss-demo.aliyuncs.com:23450";
    // 每个Part的大小，最小为5MB
    private static final long PART_SIZE = 5 * 1024 * 1024L;
    // 上传Part的并发线程数。
    private static final int CONCURRENCIES = 12;

    public static void main(String[] args) {
        uploadImageTest();
    }

    public static void uploadImageTest() {
        OSSClient ossClient = initClient();
		/*File file = new File("D:\\dadimedia\\dadiftp\\dcp_bak\\1763c41c-809f-4bd5-92d9-c969da5cbd11");
		String key = "dcp/1763c41c-809f-4bd5-92d9-c969da5cbd11";
		if (file.exists()) {
			File[] files = file.listFiles();
			List<File> lists = new ArrayList<File>();
			for (File f : files) {
				lists.add(f);
			}
			uploadMultiFile(ossClient, key, lists);
		}*/
        //uploadSmallFile(ossClient, f1, uuid, BACKET_NAME);
        //uploadSingleFile(ossClient, uuid, f1, BACKET_NAME);
        //downloadObject(ossClient, "dcp/84f5db0e-8293-43c1-8c5a-86f97f8f66c8", "C:\\Users\\SongQingWei\\Desktop\\84f5db0e-8293-43c1-8c5a-86f97f8f66c8");
        //delObject(ossClient, uuid);
		/*List<String> files = readFiles(ossClient, "dcp/6a4c73ae-5083-40fb-9f85-afccc9e50202");
		for (String string : files) {
			System.out.println(string);
		}*/
		/*List<String> keys = Arrays.asList("dcp/1763c41c-809f-4bd5-92d9-c969da5cbd11/ASSETMAP",
				"dcp/1763c41c-809f-4bd5-92d9-c969da5cbd11/Audio_dajiang15s_JPEG_239.mxf",
				"dcp/1763c41c-809f-4bd5-92d9-c969da5cbd11/CPL_e2784b29-2cdf-4318-b931-ae7d22566fb5.cpl.xml",
				"dcp/1763c41c-809f-4bd5-92d9-c969da5cbd11/PKL_1763c41c-809f-4bd5-92d9-c969da5cbd11.pkl.xml",
				"dcp/1763c41c-809f-4bd5-92d9-c969da5cbd11/Video_dajiang15s_JPEG_239.mxf",
				"dcp/1763c41c-809f-4bd5-92d9-c969da5cbd11/VOLINDEX");
		delObjects(ossClient, keys);*/
        System.out.println(readFiles(ossClient, "test/dcp/1763c41c-809f-4bd5-92d9-c969da5cbd11"));
        //downloadObject(ossClient, "test/dcp/1763c41c-809f-4bd5-92d9-c969da5cbd11/ASSETMAP", "C:\\Users\\SongQingWei\\Desktop\\1763c41c-809f-4bd5-92d9-c969da5cbd11\\ASSETMAP");
        closeClient(ossClient);
    }

    /**
     * 获取阿里云OSS客户端对象
     *
     * @return
     */
    public static OSSClient initClient() {
        ClientConfiguration config = new ClientConfiguration();
        OSSClient client = new OSSClient(OSS_ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET, config);
        return client;
    }

    /**
     * 关闭
     *
     * @param client
     */
    public static void closeClient(OSSClient client) {
        if (client != null) {
            client.shutdown();
        }
    }

    /**
     * 获取存储空间下指定文件夹下的文件
     *
     * @param client client
     * @param key    文件夹
     * @return
     */
    public static List<String> readFiles(OSSClient client, String key) {
        // 构造ListObjectsRequest请求。
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(BACKET_NAME);
        // 设置prefix参数来获取fun目录下的所有文件。
        if (key.endsWith("/")) {
            listObjectsRequest.setPrefix(key);
        } else {
            listObjectsRequest.setPrefix(key + "/");
        }
        // 递归列出fun目录下的所有文件。
        ObjectListing listing = client.listObjects(listObjectsRequest);
        // 遍历所有文件。
        List<String> listFiles = new ArrayList<String>();
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            System.out.println(objectSummary.getSize());
            listFiles.add(objectSummary.getKey());
        }
        return listFiles;
    }

    /**
     * 判断文件是否存在
     *
     * @param client client
     * @param key    存储空间下问价全路径 如:dcp/a.jpg
     * @return
     */
    public static boolean checkFileExist(OSSClient client, String key) {
        return client.doesObjectExist(BACKET_NAME, key);
    }

    /**
     * 下载文件
     *
     * @param client       client
     * @param key          Bucket下的文件的路径名+文件名 如："upload/1.jpg"
     * @param downloadFile 下载后保存本地的路径+文件名
     * @return boolean
     */
    public static boolean downloadObject(OSSClient client, String key, String downloadFile) {
        boolean flag = false;
        try {
            DownloadFileRequest downloadFileRequest = new DownloadFileRequest(BACKET_NAME, key);
            // 设置本地文件
            downloadFileRequest.setDownloadFile(downloadFile);
            // 设置并发下载数，默认1
            downloadFileRequest.setTaskNum(5);
            // 设置分片大小，默认100KB
            downloadFileRequest.setPartSize(1024 * 1024 * 1);
            // 开启断点续传，默认关闭
            downloadFileRequest.setEnableCheckpoint(true);
            DownloadFileResult downloadResult = client.downloadFile(downloadFileRequest);
            ObjectMetadata objectMetadata = downloadResult.getObjectMetadata();
            System.out.println(objectMetadata.getETag());
            System.out.println(objectMetadata.getLastModified());
            System.out.println(objectMetadata.getUserMetadata().get("meta"));
            flag = true;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean downloadObject1(OSSClient client, String key, String downloadFile) {
        boolean flag = false;
        ObjectMetadata object = client.getObject(new GetObjectRequest(BACKET_NAME, key), new File(downloadFile));
        if (object != null) {
            flag = true;
        }
        return flag;
    }

    /**
     * 根据key删除OSS服务器上的文件
     *
     * @param client     OSS连接
     * @param key        Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     */
    public static void delObject(OSSClient client, String key) {
        client.deleteObject(BACKET_NAME, key);
    }

    /**
     * 批量删除问价
     *
     * @param client
     * @param keys
     * @return
     */
    public static boolean delObjects(OSSClient client, List<String> keys) {
        // quiet 返回模式。true表示简单模式，false表示详细模式。默认为详细模式
        DeleteObjectsResult deleteObjectsResult = client.deleteObjects(new DeleteObjectsRequest(BACKET_NAME).withKeys(keys));
        // getDeletedObjects() 删除结果。详细模式下为删除成功的文件列表，简单模式下为删除失败的文件列表。
        List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
        if (keys.size() == deletedObjects.size()) {
            return true;
        }
        return false;
    }

    /**
     * 小文件上传
     *
     * @param ossClient  client
     * @param file       file
     * @param key        格式：dcp/
     * @param bucketName 存储空间
     */
    public static void uploadSmallFile(OSSClient ossClient, File file, String key, String bucketName) {
        ossClient.putObject(bucketName, key + file.getName(), file);
    }

    /**
     * 文件上传
     *
     * @param key
     * @param uploadFile
     * @return boolean
     */
    public static boolean uploadSingleFile(OSSClient client, String key, File uploadFile) {
        boolean flag = false;
        if (StringUtils.isBlank(key)) {
            System.err.println("无法上传文件,key为空");
            return false;
        }
        System.out.println("开始上传..." + uploadFile.getName());
        String fileParentName;
        if (key.endsWith("/")) {
            fileParentName = key + uploadFile.getName();
        } else {
            fileParentName = key + "/" + uploadFile.getName();
        }
        try {
            long startTime = System.currentTimeMillis();
            uploadBigFile(client, uploadFile, fileParentName, BACKET_NAME);
            long endTime = System.currentTimeMillis();
            long spendTime = (endTime - startTime) / 1000;
            System.out.println("上传花费时间约：" + spendTime + " s");
            flag = true;
        } catch (OSSException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 批量上传
     *
     * @param client
     * @param key
     * @param fileList
     * @return
     */
    public static boolean uploadMultiFile(OSSClient client, String key, List<File> fileList) {
        boolean flag = false;
        if (StringUtils.isBlank(key)) {
            System.err.println("无法上传文件,key为空");
            return false;
        }
        Integer uploadFileSize = fileList.size();
        int index = 0;
        long startTime = System.currentTimeMillis();
        for (File uploadFile : fileList) {
            if (!uploadFile.exists()) {
                System.err.println("无法找到文件：" + uploadFile.getPath());
                continue;
            }
            boolean uploadSingleFile = uploadSingleFile(client, key, uploadFile);
            if (uploadSingleFile) {
                index++;
            }
        }
        long endTime = System.currentTimeMillis();
        long totalSpendTime = (endTime - startTime) / 1000;
        System.out.println("共耗时:" + totalSpendTime + " s");
        if (uploadFileSize.intValue() == index) {
            flag = true;
        }
        return flag;
    }

    // 通过Multipart的方式上传一个大文件
    // 要上传文件的大小必须大于一个Part允许的最小大小，即5MB。
    public static void uploadBigFile(OSSClient client, File uploadFile, String key, String bucketName)
            throws OSSException, ClientException, InterruptedException {

        int partCount = calPartCount(uploadFile);

        String uploadId = initMultipartUpload(client, bucketName, key);

        ExecutorService pool = Executors.newFixedThreadPool(CONCURRENCIES);

        List<PartETag> eTags = Collections.synchronizedList(new ArrayList<PartETag>());

        for (int i = 0; i < partCount; i++) {
            // System.out.println("正在上传第" + i + "部分");
            long start = PART_SIZE * i;
            long curPartSize = PART_SIZE < uploadFile.length() - start ? PART_SIZE : uploadFile.length() - start;

            pool.execute(new UploadPartThread(client, bucketName, key, uploadFile, uploadId, i + 1, PART_SIZE * i,
                    curPartSize, eTags));
        }

        pool.shutdown();
        while (!pool.isTerminated()) {
            pool.awaitTermination(5, TimeUnit.SECONDS);
        }

        if (eTags.size() != partCount) {
            throw new IllegalStateException("Multipart上传失败，有Part未上传成功。");
        }

        completeMultipartUpload(client, bucketName, key, uploadId, eTags);
    }

    /**
     * 根据文件的大小和每个Part的大小计算需要划分的Part个数。
     *
     * @param file file
     * @return Part个数
     */
    private static int calPartCount(File file) {
        int partCount = (int) (file.length() / PART_SIZE);
        if (file.length() % PART_SIZE != 0) {
            partCount++;
        }
        return partCount;
    }

    /**
     * 初始化一个Multi-part upload请求。
     *
     * @param client     client
     * @param bucketName 存储空间
     * @param key        存储空间下的文件夹名
     * @return string
     * @throws OSSException    e
     * @throws ClientException e
     */
    private static String initMultipartUpload(OSSClient client, String bucketName, String key)
            throws OSSException, ClientException {
        InitiateMultipartUploadRequest initUploadRequest = new InitiateMultipartUploadRequest(bucketName, key);
        InitiateMultipartUploadResult initResult = client.initiateMultipartUpload(initUploadRequest);
        String uploadId = initResult.getUploadId();
        return uploadId;
    }

    /**
     * 完成一个multi-part请求。
     *
     * @param client
     * @param bucketName
     * @param key
     * @param uploadId
     * @param eTags
     * @throws OSSException
     * @throws ClientException
     */
    private static void completeMultipartUpload(OSSClient client, String bucketName, String key, String uploadId,
                                                List<PartETag> eTags) throws OSSException, ClientException {
        // 为part按partnumber排序
        Collections.sort(eTags, new Comparator<PartETag>() {

            @Override
            public int compare(PartETag arg0, PartETag arg1) {
                PartETag part1 = arg0;
                PartETag part2 = arg1;

                return part1.getPartNumber() - part2.getPartNumber();
            }
        });

        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName,
                key, uploadId, eTags);

        client.completeMultipartUpload(completeMultipartUploadRequest);
    }

    private static class UploadPartThread implements Runnable {
        private File uploadFile;
        private String bucket;
        private String object;
        private long start;
        private long size;
        private List<PartETag> eTags;
        private int partId;
        private OSSClient client;
        private String uploadId;

        UploadPartThread(OSSClient client, String bucket, String object, File uploadFile, String uploadId, int partId,
                         long start, long partSize, List<PartETag> eTags) {
            this.uploadFile = uploadFile;
            this.bucket = bucket;
            this.object = object;
            this.start = start;
            this.size = partSize;
            this.eTags = eTags;
            this.partId = partId;
            this.client = client;
            this.uploadId = uploadId;
        }

        @Override
        public void run() {

            InputStream in = null;
            try {
                System.out.println("当前第" + partId + "块开始上传");
                in = new FileInputStream(uploadFile);
                in.skip(start);
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucket);
                uploadPartRequest.setKey(object);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setInputStream(in);
                uploadPartRequest.setPartSize(size);
                uploadPartRequest.setPartNumber(partId);
                UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
                eTags.add(uploadPartResult.getPartETag());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
