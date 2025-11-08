package org.demo.car.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.exception.BusinessException;
import org.demo.car.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 本地文件存储服务实现
 */
@Slf4j
@Service
public class LocalFileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.base-url}")
    private String baseUrl;

    @Override
    public String upload(MultipartFile file, String path) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        // 获取文件原始名称
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BusinessException("文件名不能为空");
        }

        // 获取文件扩展名
        String extension = "";
        int lastDotIndex = originalFilename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            extension = originalFilename.substring(lastDotIndex);
        }

        // 生成文件名：UUID + 扩展名
        String filename = UUID.randomUUID().toString() + extension;

        // 构建存储路径：/uploads/images/yyyy/MM/dd/
        LocalDate now = LocalDate.now();
        String datePath = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String relativePath = path + datePath + "/";
        String fullPath = uploadPath + relativePath;

        try {
            // 创建目录
            Path directory = Paths.get(fullPath);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // 保存文件
            Path filePath = directory.resolve(filename);
            file.transferTo(filePath.toFile());

            // 返回访问URL
            String fileUrl = relativePath + filename;
            log.info("文件上传成功: {}", fileUrl);
            return fileUrl;

        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        try {
            Path filePath = Paths.get(uploadPath + fileUrl);
            Files.deleteIfExists(filePath);
            log.info("文件删除成功: {}", fileUrl);
        } catch (IOException e) {
            log.error("文件删除失败: {}", fileUrl, e);
            throw new BusinessException("文件删除失败: " + e.getMessage());
        }
    }

    @Override
    public String getAccessUrl(String fileKey) {
        if (fileKey == null || fileKey.isEmpty()) {
            return null;
        }
        // 如果已经是完整URL，直接返回
        if (fileKey.startsWith("http://") || fileKey.startsWith("https://")) {
            return fileKey;
        }
        return baseUrl + "/files/" + fileKey;
    }
}

