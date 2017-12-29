package com.itheima.bos.service.base;

import com.itheima.bos.domain.base.Area;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAreaService {
    /**
     * 文件导入
     * @param areas
     */
    void areaBatchImport(List<Area> areas);

    /**
     * 分页条件查询
     * @param page
     * @param rows
     * @param area
     * @return
     */
    Page<Area> queryPage(Integer page, Integer rows, Area area);

    /**
     * 区域数据的添加
     * @param area
     */
    void save(Area area);

    /**
     * 区域数据的删除
     * @param ids
     */
    void areaDel(String ids);

    /**
     * 区域数据的查询
     * @return
     */
    List<Area> areaFindAll();

    List<Area> areaQueryByCity();
}
