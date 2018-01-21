package com.itheima.fore.action;

import com.itheima.bos.domain.comment.PageBean;
import com.itheima.bos.domain.constant.Constants;
import com.itheima.bos.domain.take_delivery.Promotion;
import com.itheima.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.print.attribute.standard.Media;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
@Scope("prototype")
@ParentPackage("json-default")
@Namespace("/")
public class PromotionAction extends BaseAction<Promotion>{

    /**
     * 宣传活动分页查询
     * @return
     */
    @Action(value = "promotion_queryPage",results = {@Result(name = "success",type = "json")})
    public String promotionQueryPage(){
        //请求服务
        PageBean<Promotion> pageBean = WebClient.create("http://localhost:8080/bos_management/services/promotionService/promotion/promotionQueryPage?page=" + page + "&rows=" + rows).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).get(PageBean.class);
        //将pageBean写入值栈
        ActionContext.getContext().getValueStack().push(pageBean);
        return SUCCESS;
    }

    /**
     * 宣传活动的详情页面
     * @return
     */
    @Action(value = "promotion_showDetail")
    public String promotionShowDetail(){
        //判断id对应的html页面是否存在
        String htmlPath = ServletActionContext.getServletContext().getRealPath("/freemarker");
        File idHtml = new File(htmlPath, model.getId() + ".html");
        if(idHtml.exists()){
            //html文件存在
            //直接将文件返回
            ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
            try {
                FileUtils.copyFile(idHtml,ServletActionContext.getResponse().getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            //html文件不存在
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
            try {
                configuration.setDirectoryForTemplateLoading(new File(ServletActionContext.getServletContext().getRealPath("/WEB-INF/freemarker_templates")));
                //获取模板对象
                Template template = configuration.getTemplate("promotion_detail.ftl");
                //获取动态数据
                Promotion promotion = WebClient.create(Constants.BOS_MANAGEMENT_URL + "/services/promotionService/promotion/promotion_detail/" + model.getId()).accept(MediaType.APPLICATION_JSON_TYPE).get(Promotion.class);
                //经promotion放入map中
                Map<String ,Object> templateMap = new HashMap<String, Object>();
                templateMap.put("promotion",promotion);
                //合并输出
                template.process(templateMap,new OutputStreamWriter(new FileOutputStream(idHtml),"UTF-8"));
                ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
                FileUtils.copyFile(idHtml,ServletActionContext.getResponse().getOutputStream());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return NONE;
    }
}
