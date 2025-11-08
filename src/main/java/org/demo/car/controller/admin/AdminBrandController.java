package org.demo.car.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.car.common.result.PageResult;
import org.demo.car.common.result.Result;
import org.demo.car.dto.request.BrandRequest;
import org.demo.car.entity.Brand;
import org.demo.car.service.BrandService;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端品牌控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/brand")
@RequiredArgsConstructor
public class AdminBrandController {

    private final BrandService brandService;

    /**
     * 分页查询品牌列表
     */
    @GetMapping("/list")
    public Result<PageResult<Brand>> getBrandPage(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        log.debug("分页查询品牌列表: name={}, status={}", name, status);
        PageResult<Brand> result = brandService.getBrandPage(name, status, pageNum, pageSize);
        return Result.success(result);
    }

    /**
     * 获取品牌详情
     */
    @GetMapping("/{id}")
    public Result<Brand> getBrandDetail(@PathVariable Long id) {
        log.debug("获取品牌详情: brandId={}", id);
        Brand brand = brandService.getBrandById(id);
        return Result.success(brand);
    }

    /**
     * 创建品牌
     */
    @PostMapping
    @org.demo.car.security.RequirePermission("brand:create")
    public Result<Long> createBrand(@Valid @RequestBody BrandRequest request) {
        log.info("创建品牌: name={}", request.getName());
        Long id = brandService.createBrand(request);
        return Result.success(id);
    }

    /**
     * 更新品牌
     */
    @PutMapping("/{id}")
    @org.demo.car.security.RequirePermission("brand:update")
    public Result<Void> updateBrand(@PathVariable Long id, 
                                    @Valid @RequestBody BrandRequest request) {
        log.info("更新品牌: brandId={}", id);
        brandService.updateBrand(id, request);
        return Result.success();
    }

    /**
     * 删除品牌
     */
    @DeleteMapping("/{id}")
    @org.demo.car.security.RequirePermission("brand:delete")
    public Result<Void> deleteBrand(@PathVariable Long id) {
        log.info("删除品牌: brandId={}", id);
        brandService.deleteBrand(id);
        return Result.success();
    }

    /**
     * 上下架品牌
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateBrandStatus(@PathVariable Long id, 
                                          @RequestParam Integer status) {
        log.info("更新品牌状态: brandId={}, status={}", id, status);
        brandService.updateBrandStatus(id, status);
        return Result.success();
    }
}

