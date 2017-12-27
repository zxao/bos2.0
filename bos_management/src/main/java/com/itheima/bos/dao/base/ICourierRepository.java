package com.itheima.bos.dao.base;

import com.itheima.bos.domain.base.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface ICourierRepository extends JpaRepository<Courier,Integer> ,JpaSpecificationExecutor<Courier> {
    @Query(value = "update Courier set deltag='1' where id=?")
    @Modifying
    public void updateDeltag(Integer id);

    @Query(value = "update Courier set deltag='' where id=?")
    @Modifying
    public void updateDeltagForRestore(Integer id);
}
