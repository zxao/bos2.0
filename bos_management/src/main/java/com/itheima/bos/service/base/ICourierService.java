package com.itheima.bos.service.base;

import com.itheima.bos.domain.base.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ICourierService {
    void save(Courier courier);

    

    void courierDeleteBatch(String ids);

    void courierRestore(String ids);

    Page<Courier> queryPage(Specification<Courier> specification, Pageable pageable);
}
