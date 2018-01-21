package com.itheima.bos.service.delivery.impl;

import com.itheima.bos.dao.base.IAreaRepository;
import com.itheima.bos.dao.base.IFixedAreaRepository;
import com.itheima.bos.dao.delivery.IOrderRepository;
import com.itheima.bos.dao.delivery.IWorkBillRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.constant.Constants;
import com.itheima.bos.domain.take_delivery.Order;
import com.itheima.bos.domain.take_delivery.WorkBill;
import com.itheima.bos.service.delivery.IOrderService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {
    //注入orderRepository
    @Autowired
    private IOrderRepository orderRepository;

    //注入fixedAreaRepository
    @Autowired
    private IFixedAreaRepository fixedAreaRepository;

    //注入AreaRepository
    @Autowired
    private IAreaRepository areaRepository;

    //注入jmsQueueTemplate
    @Autowired
    private JmsTemplate jmsQueueTemplate;

    //注入workBillRepository
    @Autowired
    private IWorkBillRepository workBillRepository;

    @Override
    public void orderSave(Order order) {
        //设置订单的编号
        String orderNum = UUID.randomUUID().toString().replace("-", "");
        order.setOrderNum(orderNum);
        //设置下单时间
        order.setOrderTime(new Date());
        order.setStatus("1");
        //封装包含id的area
        Area persistArea = areaRepository.findByProvinceAndCityAndDistrict(order.getSendArea().getProvince(),order.getSendArea().getCity(),order.getSendArea().getDistrict());
        order.setSendArea(persistArea);
        Area recArea = areaRepository.findByProvinceAndCityAndDistrict(order.getRecArea().getProvince(),order.getRecArea().getCity(),order.getRecArea().getDistrict());
        order.setRecArea(recArea);

        //order中关联快递员
        //使用webservice请求crm_management,根据客户地址找到定区
        String address = order.getSendAddress();
        String fixedAreaId = WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/customer/findFixedAreaIdByAddress?address=" + address).type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).get(String.class);
        if(fixedAreaId!=null){
            //查询对应的定区
            FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
            //从定区中找到关联的快递员
            Iterator<Courier> iterator = fixedArea.getCouriers().iterator();
            if(iterator.hasNext()){
                Courier courier = iterator.next();
                //自动分担成功
                System.out.println("根据区域表中地址自动分单成功");
                //将快递员关联到订单
                order.setCourier(courier);


            }
            //设置订单类型
            order.setOrderType("1");
            //保存订单
            orderRepository.save(order);

            //生成工单发送短信
            //使用mq向快递员发送短信
//            jmsTemplate.send("bos_sendMsg", new MessageCreator() {
//                @Override
//                public Message createMessage(Session session) throws JMSException {
//
//                    MapMessage mapMessage = session.createMapMessage();
//                    mapMessage.setString("telephone",model.getTelephone());
//                    mapMessage.setString("msgContent",message);
//                    return mapMessage;
//                }
//            });
            //生成工单发送短信
            try {
                creatWorkBillAndSendMsg(order);

            } catch (JMSException e) {
                e.printStackTrace();
            }


            return;
        }

        //根据省市区找到区域，再根据分区找到定区，从而找到快递员
        //根据省市区找到区域

        //遍历区域中包含的定区
        for(SubArea subArea : persistArea.getSubareas()){
            //判断地址中是否包含关键字
            if(order.getSendAddress().contains(subArea.getKeyWords())||order.getSendAddress().contains(subArea.getAssistKeyWords())){
                Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
                if(iterator.hasNext()){
                    Courier courier = iterator.next();
                    //自动分担成功
                    System.out.println("根据区域表中地址自动分单成功");
                    //将快递员关联到订单
                    order.setCourier(courier);

                }
                //设置订单类型
                order.setOrderType("1");
                //保存订单
                orderRepository.save(order);

                //生成工单发送短信
                try {
                    creatWorkBillAndSendMsg(order);
                } catch (JMSException e) {
                    e.printStackTrace();
                }

                return;

            }
        }

        //进行人工分单
        System.out.println("进入人工分单");
        order.setOrderType("2");
        //保存订单
        orderRepository.save(order);

    }

    @Override
    public Order findByOrderNum(String orderNum) {
        return orderRepository.findByOrderNum(orderNum);
    }

    private void creatWorkBillAndSendMsg(Order order) throws JMSException {
        //生成工单
        WorkBill workBill = new WorkBill();
        workBill.setType("新");
        workBill.setPickstate("新单");
        workBill.setBuildtime(new Date());
        workBill.setRemark(order.getRemark());
        workBill.setSmsNumber(RandomStringUtils.randomNumeric(4));
        workBill.setOrder(order);
        workBill.setCourier(order.getCourier());
        workBillRepository.save(workBill);
        //发送短信
        sendMsg(order);
        //修改工单
        workBill.setPickstate("已通知");
    }

    private void sendMsg(Order order) throws JMSException {
        //发送短信的电话以及内容
        final String telephone = order.getCourier().getTelephone();
        final String msgContent = "快递员："+order.getCourier().getName()+",请快速到"+order.getSendAddress()+"取件"+",客户联系电话为"+order.getSendMobile()+",客户信息备注为："+order.getSendMobileMsg()+"[速运快递]";
        jmsQueueTemplate.send("bos_sendMsg", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone",telephone);
                mapMessage.setString("msgContent",msgContent);
                return mapMessage;
            }
        });
    }


}
