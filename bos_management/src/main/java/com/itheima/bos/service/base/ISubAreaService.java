package com.itheima.bos.service.base;

import com.itheima.bos.domain.base.SubArea;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ISubAreaService {
    /**
     * 分区数据的导入
     * @param list
     */
    void subAreaImport(List<SubArea> list);

    /**
     * 分区管理的分页条件查询
     * @param page
     * @param rows
     * @param model
     * @return
     */
    Page<SubArea> subAreaQueryPage(Integer page, Integer rows, SubArea model);

    /**
     *分区数据的保存
     * @param model
     */
    void save(SubArea model);

    /**
     * 查询所有
     * @return
     */
    List<SubArea> findAll();

    /**
     * 分区的删除
     * @param ids
     */
    void subAreaBatchDel(String ids);
}
