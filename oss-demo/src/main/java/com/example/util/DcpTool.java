package com.example.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author SongQingWei
 * @date 2018年07月30 15:45
 */
@Slf4j
public class DcpTool {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
		/*String path="E:\\0128jinpengweidao20_JPEG_239\\ASSETMAP";
		File f=new File(path);*/
        DcpTool dt=new DcpTool();
        System.out.println(dt.validateDCP("C:\\Users\\SongQingWei\\Desktop\\84f5db0e-8293-43c1-8c5a-86f97f8f66c8"));
    }
    /**
     * 验证dcp包的完整性
     * @param dcpPath dcp包路径
     * @return
     */
    public boolean validateDCP(String dcpPath){
        boolean result = false;
        File f=new File(dcpPath);
        if(!f.exists()){
            return false;
        }
        File ASSETMAPFILE =findAssetmapFile(dcpPath);
        try {
            if(ASSETMAPFILE!=null && ASSETMAPFILE.exists()){
                Map<String,String> dcpIdAndNameMap=getDcpFileIdAndNameFromAssetmap(ASSETMAPFILE);
                String pklName  = getPklFileNameFromAssetmap(ASSETMAPFILE);
                String pklFilePath=dcpPath+File.separator+pklName;
                Map<String,String> dcpIdAndHashMap=getDcpFileIdAndHash(pklFilePath);
                if(!dcpIdAndHashMap.isEmpty()){
                    Set<String> dcpIdKeySet=dcpIdAndHashMap.keySet();
                    int size=dcpIdKeySet.size();
                    int index=0;
                    for(String dcpId:dcpIdKeySet){
                        if(!dcpIdAndNameMap.containsKey(dcpId)){
                            break;
                        }
                        if(!dcpIdAndHashMap.containsKey(dcpId)){
                            break;
                        }
                        String fileName=dcpIdAndNameMap.get(dcpId);
                        String pklHashValue=dcpIdAndHashMap.get(dcpId);
                        String hash=getFileMD(dcpPath+File.separator+fileName);
                        if(pklHashValue!=null && hash!=null && pklHashValue.equals(hash)){
                            index++;
                        }else{
                            log.info("文件:"+fileName+"在pkl中哈希值:"+pklHashValue);
                            log.info("文件:"+fileName+",重新计算哈希值:"+hash);
                            result=false;
                            break;
                        }
                    }
                    if(size==index){
                        result=true;
                    }
                    log.info("文件:"+dcpPath+" hash校验结果:"+result);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public String getPklFileNameFromAssetmap(File ASSETMAPFile) throws SAXException {
        String pklFileName="";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.parse(ASSETMAPFile);
            Element root = doc.getDocumentElement();
            NodeList collegeNodes = root.getChildNodes();
            if (collegeNodes == null) {
                return null;
            }
            for (int i = 0; i < collegeNodes.getLength(); i++) {
                Node college = collegeNodes.item(i);
                if (college.getNodeType() == Node.ELEMENT_NODE
                        && college.getNodeName().equals("AssetList")) {
                    NodeList ReelCollege = college.getChildNodes();
                    for (int k = 0; k < ReelCollege.getLength(); k++) {
                        Node rn = ReelCollege.item(k);
                        if (rn.getNodeType() == Node.ELEMENT_NODE
                                && rn.getNodeName().equals("Asset")) {
                            NodeList reelList = rn.getChildNodes();
                            boolean isPkl = false;
                            for (int m = 0; m < reelList.getLength(); m++) {
                                Node rb = reelList.item(m);
                                if (rb.getNodeType() == Node.ELEMENT_NODE) {
                                    // log.info("=-=-"+rb.getNodeName());
                                    if (rb.getNodeName().equals("PackingList")) {
                                        isPkl = true;
                                    }
                                    if (isPkl
                                            && rb.getNodeName().equals(
                                            "ChunkList")) {
                                        NodeList ChunkList = rb.getChildNodes();
                                        for (int n = 0; n < ChunkList
                                                .getLength(); n++) {
                                            Node ChunkChild = ChunkList.item(n);
                                            if (ChunkChild.getNodeType() == Node.ELEMENT_NODE
                                                    && ChunkChild.getNodeName()
                                                    .equals("Chunk")) {
                                                NodeList ChunkAll = ChunkChild
                                                        .getChildNodes();
                                                for (int t = 0; t < ChunkAll
                                                        .getLength(); t++) {
                                                    Node pv = ChunkAll.item(t);
                                                    if (pv.getNodeType() == Node.ELEMENT_NODE
                                                            && pv.getNodeName()
                                                            .equals("Path")) {
                                                        pklFileName=pv.getTextContent();
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pklFileName;
    }

    public  File findAssetmapFile(String dcpPath){
        File assetFile=null;
        File singleDcpFile = new File(dcpPath);
        if (!singleDcpFile.exists()) {
            System.err.println("singleDcpFile invalid:" + dcpPath);
            return null;
        }
        FileFilter ff = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                // TODO Auto-generated method stub
                String filename = pathname.getName().toUpperCase();
                if (filename.contains("ASSETMAP") && !filename.contains("DSR")) {
                    return true;
                }
                return false;
            }
        };
        File[] fs = singleDcpFile.listFiles(ff);
        if(fs!=null && fs.length>0){
            assetFile=fs[0];
        }else{
            log.warn("没有找到assetmap文件:"+dcpPath);
        }
        return assetFile;
    }
    public File findAssetFile(String dcpPath){
        File dcpFile = new File(dcpPath);
        if(!dcpFile.exists()){
            log.warn("\u6587\u4ef6:"+dcpPath+"\u4e0d\u5b58\u5728");
        }
        File assetmapFile=null;
        File[] fs=dcpFile.listFiles();
        if(fs!=null && fs.length>0){
            for(File f:fs){
                String name=f.getName().toLowerCase();
                if(name.startsWith("assetmap")){
                    assetmapFile=f;
                    break;
                }
            }
        }
        return assetmapFile;
    }
    public  Map<String,String> getDcpFileIdAndNameFromAssetmap(File ASSETMAPFile) {
        Map<String,String> map=new HashMap<String,String>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.parse(ASSETMAPFile);
            Element root = doc.getDocumentElement();
            NodeList collegeNodes = root.getChildNodes();
            if (collegeNodes == null) {
                return null;
            }
            for (int i = 0; i < collegeNodes.getLength(); i++) {
                Node college = collegeNodes.item(i);
                if (college.getNodeType() == Node.ELEMENT_NODE
                        && college.getNodeName().equals("AssetList")) {
                    NodeList ReelCollege = college.getChildNodes();
                    for (int k = 0; k < ReelCollege.getLength(); k++) {
                        Node rn = ReelCollege.item(k);
                        if (rn.getNodeType() == Node.ELEMENT_NODE
                                && rn.getNodeName().equals("Asset")) {
                            NodeList reelList = rn.getChildNodes();
                            String IdString="";
                            String pklFileName="";
                            for (int m = 0; m < reelList.getLength(); m++) {
                                Node rb = reelList.item(m);
                                if (rb.getNodeType() == Node.ELEMENT_NODE) {
                                    if(rb.getNodeName().equals("Id")){
                                        IdString=rb.getTextContent();
                                    }
                                    if (rb.getNodeName().equals(
                                            "ChunkList")) {
                                        NodeList ChunkList = rb.getChildNodes();
                                        for (int n = 0; n < ChunkList
                                                .getLength(); n++) {
                                            Node ChunkChild = ChunkList.item(n);
                                            if (ChunkChild.getNodeType() == Node.ELEMENT_NODE
                                                    && ChunkChild.getNodeName()
                                                    .equals("Chunk")) {
                                                NodeList ChunkAll = ChunkChild
                                                        .getChildNodes();
                                                for (int t = 0; t < ChunkAll
                                                        .getLength(); t++) {
                                                    Node pv = ChunkAll.item(t);
                                                    if (pv.getNodeType() == Node.ELEMENT_NODE
                                                            && pv.getNodeName()
                                                            .equals("Path")) {
                                                        pklFileName=pv.getTextContent();
                                                    }
                                                }

                                            }
                                        }
                                    }
                                    if(IdString!=null && !IdString.equals("") &&
                                            pklFileName!=null && !pklFileName.equals("")){
                                        map.put(IdString, pklFileName);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }
    public  Map<String,String> getDcpFileIdAndHash(String pklFilePath) {
        Map<String,String> idAndHashMap=new HashMap<String,String>();
        File pklFile = new File(pklFilePath);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            doc = builder.parse(pklFile);
            Element root = doc.getDocumentElement();
            NodeList collegeNodes = root.getChildNodes();
            if (collegeNodes == null) {
                return idAndHashMap;
            }
            for (int i = 0; i < collegeNodes.getLength(); i++) {
                Node college = collegeNodes.item(i);
                if (college.getNodeType() == Node.ELEMENT_NODE
                        && college.getNodeName().equals("AssetList")) {
                    NodeList ReelCollege = college.getChildNodes();
                    for (int k = 0; k < ReelCollege.getLength(); k++) {
                        Node rn = ReelCollege.item(k);
                        String hash="";
                        String idString="";
                        if (rn.getNodeType() == Node.ELEMENT_NODE
                                && rn.getNodeName().equals("Asset")) {
                            NodeList reelList = rn.getChildNodes();
                            for (int m = 0; m < reelList.getLength(); m++) {
                                Node rb = reelList.item(m);
                                if (rb.getNodeType() == Node.ELEMENT_NODE) {
                                    if (rb.getNodeName().equals("Hash")) {
                                        hash=rb.getTextContent();
                                    }
                                    if (rb.getNodeName().equals("Id")) {
                                        idString=rb.getTextContent();
                                    }
                                    if(!hash.equals("") && !idString.equals("")){
                                        if(!idAndHashMap.containsKey(idString)){
                                            idAndHashMap.put(idString, hash);
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return idAndHashMap;
    }

    public String getFileMD(String filePath){
        File f=new File(filePath);
        MessageDigest md = null;
        String result="";
        byte buffer[] = new byte[1024];
        String algorithm = "SHA-1";
        try {
            InputStream fis = new FileInputStream(f);
            md = MessageDigest.getInstance(algorithm);
            for (int numRead = 0; (numRead = fis.read(buffer)) > 0;) {
                // log.info(numRead);
                md.update(buffer, 0, numRead);
            }
            fis.close();
            result= Base64.encode(md.digest());
        }catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    public boolean eq(String hash, InputStream fis) {
        boolean res = false;
        byte buffer[] = new byte[1024];
        String algorithm = "SHA-1";
//		long startTime = System.currentTimeMillis();
        MessageDigest md = null;
//		log.info("开始进行摘要计算");
        try {
            md = MessageDigest.getInstance(algorithm);
            for (int numRead = 0; (numRead = fis.read(buffer)) > 0;) {
                // log.info(numRead);
                md.update(buffer, 0, numRead);
            }
            fis.close();

            String result = Base64.encode(md.digest());
//			log.info("摘要用时"
//					+ (System.currentTimeMillis() - startTime) / 1000 + "秒");
//			log.info("原始的摘要值是" + hash);
//			log.info("计算的摘要值是" + result);
            res = hash.equals(result);
//			if (res) {
//				log.info("摘要验证通过");
//			} else {
//				log.info("摘要验证失败**************************");
//			}
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            buffer = null;
            md = null;
        }

        return res;
    }



}
