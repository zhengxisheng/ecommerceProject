package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by fanlinglong on 2018/1/14.
 */
@Service("iFileService")
@Slf4j
public class FileServiceImpl implements IFileService {

    @Override
    public String upload(MultipartFile file, String path) {
        //获取文件名字
        String fileName = file.getOriginalFilename();
        //获取文件扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        //服务器存储文件名字
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        log.info("开始上传文件,文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);
        File fileDir = new File(path);
        if (!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);
        try {
            //上传文件
            file.transferTo(targetFile);
            //上传到FTP服务器
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //删除文件
            targetFile.delete();
        }catch (IOException e) {
            log.error("文件上传异常:",e);
            return null;
        }
        return targetFile.getName();
    }
}
