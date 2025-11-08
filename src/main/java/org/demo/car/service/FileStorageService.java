package org.demo.car.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储服务接口
 */
public interface FileStorageService {
    
    /**
     * 上传文件
     * @param file 文件
     * @param path 存储路径
     * @return 文件访问URL
     */
    String upload(MultipartFile file, String path);
    
    /**
     * 删除文件
     * @param fileUrl 文件URL
     */
    void delete(String fileUrl);
    
    /**
     * 获取访问URL
     * @param fileKey 文件键
     * @return 访问URL
     */
    String getAccessUrl(String fileKey);
}

