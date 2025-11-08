package org.demo.car.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.exception.BusinessException;
import org.demo.car.common.result.PageResult;
import org.demo.car.dto.request.BrandRequest;
import org.demo.car.entity.Brand;
import org.demo.car.mapper.BrandMapper;
import org.demo.car.service.BrandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 品牌服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandMapper brandMapper;

    @Override
    public List<Brand> getAllActiveBrands() {
        log.debug("获取所有上架品牌");
        return brandMapper.findAllActive();
    }

    @Override
    public Brand getBrandById(Long id) {
        log.debug("获取品牌详情: brandId={}", id);
        Brand brand = brandMapper.findById(id);
        if (brand == null) {
            log.warn("品牌不存在: brandId={}", id);
            throw new BusinessException("品牌不存在");
        }
        return brand;
    }

    @Override
    public PageResult<Brand> getBrandPage(String name, Integer status,
                                         Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum <= 0) pageNum = 1;
        if (pageSize == null || pageSize <= 0) pageSize = 10;
        
        log.debug("分页查询品牌列表: name={}, status={}, pageNum={}, pageSize={}", 
                name, status, pageNum, pageSize);
        
        int offset = (pageNum - 1) * pageSize;
        List<Brand> list = brandMapper.findByPage(name, status, offset, pageSize);
        long total = brandMapper.countByPage(name, status);
        
        return new PageResult<>(list, total, pageNum, pageSize);
    }

    @Override
    @Transactional
    public Long createBrand(BrandRequest request) {
        log.info("创建品牌: name={}, dealerName={}", 
                request.getName(), request.getDealerName());
        
        Brand brand = Brand.builder()
                .name(request.getName())
                .logo(request.getLogo())
                .dealerName(request.getDealerName())
                .dealerAddress(request.getDealerAddress())
                .dealerContactName(request.getDealerContactName())
                .dealerContactPhone(request.getDealerContactPhone())
                .status(request.getStatus() != null ? request.getStatus() : 1)
                .build();
        
        brandMapper.insert(brand);
        log.info("品牌创建成功: brandId={}", brand.getId());
        return brand.getId();
    }

    @Override
    @Transactional
    public void updateBrand(Long id, BrandRequest request) {
        log.info("更新品牌: brandId={}", id);
        
        Brand brand = brandMapper.findById(id);
        if (brand == null) {
            log.error("品牌不存在: brandId={}", id);
            throw new BusinessException("品牌不存在");
        }
        
        // 只更新非空字段
        if (request.getName() != null) {
            brand.setName(request.getName());
        }
        if (request.getLogo() != null) {
            brand.setLogo(request.getLogo());
        }
        if (request.getDealerName() != null) {
            brand.setDealerName(request.getDealerName());
        }
        if (request.getDealerAddress() != null) {
            brand.setDealerAddress(request.getDealerAddress());
        }
        if (request.getDealerContactName() != null) {
            brand.setDealerContactName(request.getDealerContactName());
        }
        if (request.getDealerContactPhone() != null) {
            brand.setDealerContactPhone(request.getDealerContactPhone());
        }
        // status 字段通过专门的 updateBrandStatus 方法更新，这里不处理
        
        brandMapper.update(brand);
        log.info("品牌更新成功: brandId={}", id);
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {
        log.info("删除品牌: brandId={}", id);
        
        Brand brand = brandMapper.findById(id);
        if (brand == null) {
            log.error("品牌不存在: brandId={}", id);
            throw new BusinessException("品牌不存在");
        }
        
        // TODO: 检查是否有关联的车辆
        
        brandMapper.deleteById(id);
        log.info("品牌删除成功: brandId={}", id);
    }

    @Override
    @Transactional
    public void updateBrandStatus(Long id, Integer status) {
        log.info("更新品牌状态: brandId={}, status={}", id, status);
        
        Brand brand = brandMapper.findById(id);
        if (brand == null) {
            log.error("品牌不存在: brandId={}", id);
            throw new BusinessException("品牌不存在");
        }
        
        brandMapper.updateStatus(id, status);
        log.info("品牌状态更新成功: brandId={}", id);
    }
}

