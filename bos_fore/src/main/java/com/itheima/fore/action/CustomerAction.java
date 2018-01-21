package com.itheima.fore.action;

import cn.itcast.crm.domain.Customer;
import com.itheima.bos.domain.constant.Constants;
import com.itheima.utils.BaseAction;
import com.itheima.utils.MailUtils;
import com.itheima.utils.SmsUtils;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.print.attribute.standard.Media;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

@Controller
@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer>{

    /**
     * 注册时发送短信功能
     * @return
     */

    //注入jmsTemplate
    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;


    @Action(value = "customer_sendSms")
    public String sendSms(){
        //接收参数telephone
        //生成短信验证码
        String checkCode = RandomStringUtils.randomNumeric(4);

        //经验证码保存到session中
        ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(),checkCode);

        //编辑短信内容
        final String message = "尊敬的用户你好，您本次的验证码为"+checkCode;
        //调用sms服务器发送短信
        //try {
//            String result = "000zxao";
//            //String result = SmsUtils.sendSmsByHTTP(model.getTelephone(), message);
//            //判断是否发送成功
//            if(result.startsWith("000")){
//                //发送成功
//                //System.out.println(result);
                System.out.println(checkCode);
//                return NONE;
//            }else{
//                //发送失败
//                throw new RuntimeException("短信发送失败，信息码为"+result);
//            }
        //} catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
        //}
        //调用activeMQ发送短信
        jmsTemplate.send("bos_sendMsg", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone",model.getTelephone());
                mapMessage.setString("msgContent",message);
                return mapMessage;
            }
        });

        return NONE;


    }

    //属性驱动接收参数
    private String checkCode;

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    //注入redisTemplate
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Action(value = "customer_regist",results = {@Result(type="redirect",location = "signup-success.html"),@Result(name="input",type="redirect",location = "signup.html")})
    public String regist() {
        //校验验证码
        //获取session中的验证码
        String checkCodeSession = (String) ServletActionContext.getRequest().getSession().getAttribute(model.getTelephone());
        if(checkCodeSession!=null && checkCodeSession.equals(checkCode)){
            //通过验证
            System.out.println("验证码正确！");
            //调用activeMQ发送邮件

            //发送邮件
            //生成短信校验码
            String emailCode = RandomStringUtils.randomNumeric(32);
            //将生成的激活码存入redis数据库,设置生存时间为24小时
            redisTemplate.opsForValue().set(model.getTelephone(),emailCode,24, TimeUnit.HOURS);
            //编写邮件内容String
            final String emailContent="尊敬的客户您好，请在24小时之内完成账户的绑定，绑定账户连接为：<a href="+ MailUtils.activeUrl+"?telephone="+model.getTelephone()+"&emailCode="+emailCode+">请点击这里完成账户绑定</a>";
            //发送邮件
            //MailUtils.sendMail("这是速运快递的一封激活邮件",emailContent,model.getEmail());
            //调用activeMQ发送邮件
            jmsTemplate.send("bos_sendEmail", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    MapMessage mapMessage = session.createMapMessage();
                    mapMessage.setString("description","这是速运快递的一封激活邮件");
                    mapMessage.setString("emailContent",emailContent);
                    mapMessage.setString("email",model.getEmail());
                    return mapMessage;
                }
            });
            //保存客户信息
            WebClient.create("http://localhost:9001/crm_management/services/customerService/regist").type(MediaType.APPLICATION_JSON_TYPE).post(model);
            System.out.println("客户注册成功！");
            return SUCCESS;
        }else{
            //未通过验证
            System.out.println("验证码错误！");
            return INPUT;
        }
    }

    private String emailCode;

    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }

    @Action(value = "customer_activeMail")
    public String emailActive(){
        //取出redis中的emailCode
        String emailCodeRedis = redisTemplate.opsForValue().get(model.getTelephone());
        //接收参数
        if(this.emailCode !=null && emailCode.equals(emailCodeRedis)){
            //验证成功
            System.out.println("邮箱验证成功");
            ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
            ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
            Customer customer = WebClient.create("http://localhost:9001/crm_management/services/customerService/customer/findByTelephone/" + model.getTelephone()).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).get(Customer.class);
            if(customer.getType()==null || customer.getType()!=1){
                //修改type
                WebClient.create("http://localhost:9001/crm_management/services/customerService/customer/updateTypeByTelephone/" + customer.getTelephone()).accept(MediaType.APPLICATION_JSON_TYPE).put(null);

                try {
                    ServletActionContext.getResponse().getWriter().write("绑定邮箱成功！");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                //已经绑定过邮箱了
                try {
                    ServletActionContext.getResponse().getWriter().write("您已经绑定过邮箱了，无需重复绑定");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }else{
            //验证失败
            System.out.println("邮箱验证失败");
            //产出redis中指定的激活码
            redisTemplate.delete(model.getTelephone());
        }
        return NONE;
    }

    @Action(value = "customer_login",results = {@Result(name="success",type="redirect",location ="index.html#/myhome" ),@Result(name="error",type = "redirect",location = "login.html")})
    public String customerLogin(){

        Customer customer = WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/customer/customerLogin?telephone=" + model.getTelephone() + "&password=" + model.getPassword()).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).get(Customer.class);
        //判断customer是否存在
        if(customer==null){
            //不存在，即登录失败
            return ERROR;
        }else{
            //存在，即登录成功
            //将用户存入session中
            ServletActionContext.getRequest().getSession().setAttribute("customer",customer);
            return SUCCESS;
        }


    }



}
