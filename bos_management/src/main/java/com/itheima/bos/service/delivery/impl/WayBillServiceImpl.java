package com.itheima.bos.service.delivery.impl;

import com.itheima.bos.dao.delivery.IWayBillRepository;
import com.itheima.bos.domain.take_delivery.WayBill;
import com.itheima.bos.index.IWayBillIndexRepository;
import com.itheima.bos.service.delivery.IWayBillService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;


import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
@Transactional
public class WayBillServiceImpl implements IWayBillService {

    //注入wayBillRepository
    @Autowired
    private IWayBillRepository wayBillRepository;

    //注入waybillindexRepository
    @Autowired
    private IWayBillIndexRepository wayBillIndexRepository;

    @Override
    public void save(WayBill wayBill) {

        //判断运单是否存在
        WayBill persistWayBill = wayBillRepository.findByWayBillNum(wayBill.getWayBillNum());
        if (persistWayBill == null) {
            //运单不存在，执行保存操作
            wayBillRepository.save(wayBill);
            //同时保存索引
            wayBillIndexRepository.save(wayBill);
        } else {
            //运单存在，执行包含id的修改操作
            //得到运单的id
            Integer id = persistWayBill.getId();

            try {
                if (persistWayBill.getSignStatus()==1) {
                    //将wayBillcopy到persistwayBill中
                    BeanUtils.copyProperties(persistWayBill, wayBill);
                    persistWayBill.setId(id);
                    persistWayBill.setSignStatus(1);
                    //保存索引
                    wayBillIndexRepository.save(persistWayBill);
                } else {
                    throw new RuntimeException("运单已经发出，无法保存修改！");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    @Override
    public Page<WayBill> findQueryPage(WayBill wayBill, Pageable pageable) {
        //运单号
        String wayBillNum = wayBill.getWayBillNum();
        //发货地址
        String sendAddress = wayBill.getSendAddress();
        //收货地址
        String recAddress = wayBill.getRecAddress();
        //快递产品类型
        String sendProNum = wayBill.getSendProNum();
        //运单状态
        Integer signStatus = wayBill.getSignStatus();

        //判断waybill中是否存在条件，若都没有条件，即为无条件查询
        if (StringUtils.isBlank(wayBillNum)
                && StringUtils.isBlank(sendAddress)
                && StringUtils.isBlank(recAddress)
                && StringUtils.isBlank(sendProNum)
                && (signStatus == null || signStatus == 0)) {
            //说明没有条件，为无条件查询
            return wayBillRepository.findAll(pageable);

        } else {
            //must  条件必须成立  and
            //must not 条件必须不成立 not
            //should 条件可以成立 or

            //多条件组合查询对象
            BoolQueryBuilder boolQuery = new BoolQueryBuilder();
            //向组合查询对象中添加条件
            //如果运单号不为空
            if (StringUtils.isNotBlank(wayBillNum)) {
                //运单号查询
                //采用分词等值查询
                QueryBuilder termQueryBuilder = new TermQueryBuilder("wayBillNum", wayBillNum);
                //添加条件
                boolQuery.must(termQueryBuilder);
            }
            //如果发货地址不为空
            if (StringUtils.isNotBlank(sendAddress)) {

                //1.条件在一个词条内的分词模糊查询
                QueryBuilder wildcardQueryBuilder = new WildcardQueryBuilder("sendAddress", "*" + sendAddress + "*");

                //2.条件跨越多词条的多词条组合查询
                QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(sendAddress).field("sendAddress").defaultOperator(QueryStringQueryBuilder.Operator.AND);

                BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
                queryBuilder.should(wildcardQueryBuilder);
                queryBuilder.should(queryStringQueryBuilder);

                //将合并的条件加入boolQurey中
                boolQuery.must(queryBuilder);

            }
            //如果收货地址不为空
            if (StringUtils.isNotBlank(recAddress)) {

                //1.分词模糊查询
                QueryBuilder wildcardQueryBuilder = new WildcardQueryBuilder("recAddress", "*" + recAddress + "*");
                boolQuery.must(wildcardQueryBuilder);

                //2.条件跨越多词条的多词条组合查询
                QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(recAddress).field("recAddress").defaultOperator(QueryStringQueryBuilder.Operator.AND);

                BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
                queryBuilder.should(wildcardQueryBuilder);
                queryBuilder.should(queryStringQueryBuilder);

                //将合并的条件加入boolQurey中
                boolQuery.must(queryBuilder);
            }

            //如果快递产品类型不为空 等值查询
            if (StringUtils.isNotBlank(sendProNum)) {
                //快递产品类型查询
                //采用分词等值查询
                QueryBuilder termQueryBuilder = new TermQueryBuilder("sendProNum", sendProNum);
                //添加条件
                boolQuery.must(termQueryBuilder);
            }
            //如果运单状态不为零且不为空
            if (signStatus != null && signStatus != 0) {
                QueryBuilder termQueryBuilder = new TermQueryBuilder("signStatus", signStatus);
                //添加条件
                boolQuery.must(termQueryBuilder);
            }

            NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(boolQuery);
            nativeSearchQuery.setPageable(pageable);
            return wayBillIndexRepository.search(nativeSearchQuery);

        }


    }

    @Override
    public WayBill findByWayBillNum(String wayBillNum) {
        return wayBillRepository.findByWayBillNum(wayBillNum);
    }

    @Override
    public void syncIndex() {
        List<WayBill> wayBills = wayBillRepository.findAll();
        wayBillIndexRepository.save(wayBills);
    }

}
