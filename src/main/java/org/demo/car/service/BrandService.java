package org.demo.car.service;

import org.demo.car.common.result.PageResult;
import org.demo.car.dto.request.BrandRequest;
import org.demo.car.entity.Brand;

import java.util.List;

/**
 * 品牌服务接口
 */
public interface BrandService {
    
    /**
     * 获取所有上架品牌
     */
    List<Brand> getAllActiveBrands();
    
    /**
     * 获取品牌详情
     */
    Brand getBrandById(Long id);
    
    /**
     * 分页查询品牌列表
     */
    PageResult<Brand> getBrandPage(String name, Integer status, 
                                   Integer pageNum, Integer pageSize);
    
    /**
     * 创建品牌
     */
    Long createBrand(BrandRequest request);
    
    /**
     * 更新品牌
     */
    void updateBrand(Long id, BrandRequest request);
    
    /**
     * 删除品牌
     */
    void deleteBrand(Long id);
    
    /**
     * 上下架品牌
     */
    void updateBrandStatus(Long id, Integer status);
}

