package net.xdclass.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadUserHeadImg( MultipartFile file);
}
