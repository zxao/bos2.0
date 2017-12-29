package com.itheima.bos.service.base;

import com.itheima.bos.domain.base.FixedArea;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IFixedAreaService {
    /**
     * 定区的保存操作
     * @param model
     */
    void save(FixedArea model);

    /**
     * 定区的分页条件查询操作
     * @param page
     * @param rows
     * @param model
     * @return
     */
    Page<FixedArea> fixedAreaQueryPage(Integer page, Integer rows, FixedArea model);

    /**
     * 定区的查询
     * @return
     */
    List<FixedArea> fixedAreaFindAll();
}
