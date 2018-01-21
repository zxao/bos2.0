package com.itheima.bos.web.action.base;

import com.itheima.bos.domain.take_delivery.Promotion;
import com.itheima.bos.service.base.IPromotionService;
import com.itheima.bos.utils.BaseAction;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.UUID;

@Controller
@Scope("prototype")
@ParentPackage("json-default")
@Namespace("/")
public class PromotionAction extends BaseAction<Promotion>{

    //注入promotionService
    @Autowired
    private IPromotionService promotionService;

    private File titleImgFile;
    private String titleImgFileFileName;

    public void setTitleImgFile(File titleImgFile) {
        this.titleImgFile = titleImgFile;
    }

    public void setTitleImgFileFileName(String titleImgFileFileName) {
        this.titleImgFileFileName = titleImgFileFileName;
    }

    @Action(value = "promotion_save",results = {@Result(type="redirect",location = "./pages/take_delivery/promotion_add.html")})
    public String promotionSave() {
        String saveUrl = null;
        String savePath = null;
        try {
            //文件保存目录路径
            savePath = ServletActionContext.getServletContext().getRealPath("/upload/") + "/";

            //文件保存目录URL
            saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";

            //定义允许上传的文件扩展名
            HashMap<String, String> extMap = new HashMap<String, String>();
            extMap.put("image", "gif,jpg,jpeg,png,bmp");
            extMap.put("flash", "swf,flv");
            extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
            extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

            HttpServletResponse response = ServletActionContext.getResponse();
            PrintWriter out = ServletActionContext.getResponse().getWriter();
            HttpServletRequest request = ServletActionContext.getRequest();
            //最大文件大小
            long maxSize = 1000000;

            response.setContentType("text/html; charset=UTF-8");


            //检查目录
            File uploadDir = new File(savePath);
            if (!uploadDir.isDirectory()) {
                out.println("上传目录不存在。");
                return INPUT;
            }
            //检查目录写权限
            if (!uploadDir.canWrite()) {
                out.println("上传目录没有写权限。");
                return INPUT;
            }

            String dirName = request.getParameter("dir");
            if (dirName == null) {
                dirName = "image";
            }
            if (!extMap.containsKey(dirName)) {
                out.println("目录名不正确。");
                return INPUT;
            }
            //创建文件夹
            savePath += dirName + "/";
            saveUrl += dirName + "/";
            File saveDirFile = new File(savePath);
            if (!saveDirFile.exists()) {
                saveDirFile.mkdirs();
            }

            //生成随机的图片名
            UUID uuid = UUID.randomUUID();
            //获取文件的后缀
            String ext = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
            //拼成随机的文件名
            String newImageName = uuid + ext;
            //保存图片
            FileUtils.copyFile(titleImgFile, new File(savePath, newImageName));

            //保存图片相对路径到数据库
            model.setTitleImg(saveUrl+newImageName);

            promotionService.promotionSave(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }


    @Action(value = "promotion_queryPage",results = {@Result(type = "json")})
    public String promotionQueryPage(){

        //创建分页查询对象
        Pageable pageable = new PageRequest(page-1,rows);
        //调用service层
        Page<Promotion> pageData = promotionService.promotionQueryPage(pageable);
        pushPageDataToValueStack(pageData);

        return SUCCESS;
    }
}
