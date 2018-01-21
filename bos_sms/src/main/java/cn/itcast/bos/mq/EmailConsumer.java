package cn.itcast.bos.mq;

import cn.itcast.bos.utils.MailUtils;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Service("emailConsumer")
public class EmailConsumer implements MessageListener{

    @Override
    public void onMessage(Message message) {
        String email=null;
        try {
            MapMessage mapMessage = (MapMessage) message;
            String description = mapMessage.getString("description");
            String emailContent = mapMessage.getString("emailContent");
            email = mapMessage.getString("email");
            MailUtils.sendMail(description,emailContent,email);
            System.out.println("邮件发送成功");
        } catch (JMSException e) {
            e.printStackTrace();
            throw new RuntimeException("邮件发送失败,邮箱账号为：" + email);
        }
    }
}
