package com.itheima.bos.service.base;

import com.itheima.bos.domain.base.Standard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IStandardService {
    void save(Standard standard);

    Page<Standard> findPage(Pageable pageable);

    List<Standard> findAll();
}
