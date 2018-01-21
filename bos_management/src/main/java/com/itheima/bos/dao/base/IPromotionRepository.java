package com.itheima.bos.dao.base;

import com.itheima.bos.domain.take_delivery.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.ws.rs.QueryParam;
import java.util.Date;

public interface IPromotionRepository extends JpaRepository<Promotion,Integer>,JpaSpecificationExecutor<Promotion>{
    Promotion findById(Integer id);

    @Query(value = "update Promotion set status='2' where endDate<? and status='1'")
    @Modifying
    void updateStatus(Date date);
}
