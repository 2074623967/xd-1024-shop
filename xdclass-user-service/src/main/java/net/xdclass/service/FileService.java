package net.xdclass.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * 头像图片上传
     * @param file
     * @return
     */
    String uploadUserHeadImg( MultipartFile file);
}
