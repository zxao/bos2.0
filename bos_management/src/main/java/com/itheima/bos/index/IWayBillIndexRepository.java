package com.itheima.bos.index;

import com.itheima.bos.domain.take_delivery.WayBill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IWayBillIndexRepository extends ElasticsearchRepository<WayBill,Integer> {

}
