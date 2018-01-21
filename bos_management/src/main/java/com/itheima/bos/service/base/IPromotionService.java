package com.itheima.bos.service.base;

import com.itheima.bos.domain.comment.PageBean;
import com.itheima.bos.domain.take_delivery.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.ws.rs.*;
import java.util.Date;

public interface IPromotionService {



    void promotionSave(Promotion model);

    Page<Promotion> promotionQueryPage(Pageable pageable);

    @Path("/promotion/promotionQueryPage")
    @GET
    @Consumes({"application/xml","application/json"})
    @Produces({"application/xml","application/json"})
    PageBean<Promotion> promotionQueryPage(@QueryParam("page") Integer page,@QueryParam("rows") Integer rows);

    @Path("promotion/promotion_detail/{id}")
    @GET
    @Consumes({"application/xml","application/json"})
    @Produces({"application/xml","application/json"})
    public Promotion showPromotionDetail(@PathParam("id") Integer id);

    void updateStatus(Date date);
}
