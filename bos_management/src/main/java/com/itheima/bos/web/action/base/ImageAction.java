package com.itheima.bos.web.action.base;

import com.itheima.bos.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
public class ImageAction extends BaseAction<Object>{

    private File imgFile;
    private String imgFileFileName;
    private String imgFileContentType;

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }

    public void setImgFileFileName(String imgFileFileName) {
        this.imgFileFileName = imgFileFileName;
    }

    public void setImgFileContentType(String imgFileContentType) {
        this.imgFileContentType = imgFileContentType;
    }

    @Action(value = "image_upload",results = {@Result(type="json")})
    public String imageUpload(){

        String saveUrl=null;
        String savePath=null;
        try {
            //文件保存目录路径
            savePath = ServletActionContext.getServletContext().getRealPath("/upload/")+"/";

            //文件保存目录URL
            saveUrl= ServletActionContext.getRequest().getContextPath() + "/upload/";

            //定义允许上传的文件扩展名
            HashMap<String, String> extMap = new HashMap<String, String>();
            extMap.put("image", "gif,jpg,jpeg,png,bmp");
            extMap.put("flash", "swf,flv");
            extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
            extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

            HttpServletResponse response = ServletActionContext.getResponse();
            PrintWriter out = ServletActionContext.getResponse().getWriter();
            HttpServletRequest request= ServletActionContext.getRequest();
            //最大文件大小
            long maxSize = 1000000;

            response.setContentType("text/html; charset=UTF-8");


            //检查目录
            File uploadDir = new File(savePath);
            if(!uploadDir.isDirectory()){
                out.println("上传目录不存在。");
                return INPUT;
            }
            //检查目录写权限
            if(!uploadDir.canWrite()){
                out.println("上传目录没有写权限。");
                return INPUT;
            }

            String dirName = request.getParameter("dir");
            if (dirName == null) {
                dirName = "image";
            }
            if(!extMap.containsKey(dirName)){
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
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//            String ymd = sdf.format(new Date());
//            savePath += ymd + "/";
//            saveUrl += ymd + "/";
//            File dirFile = new File(savePath);
//            if (!dirFile.exists()) {
//                dirFile.mkdirs();
//            }

            //生成随机的图片名
            UUID uuid = UUID.randomUUID();
            //获取文件的后缀
            String ext = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
            //拼成随机的文件名
            String newImageName = uuid + ext;
            //保存图片
            FileUtils.copyFile(imgFile,new File(savePath,newImageName));

            //上传成功
            Map<String,Object> result = new HashMap<String, Object>();
            result.put("error",0);
            result.put("url",saveUrl+newImageName);
            //将result序列化为json
            //将result压入值栈中
            ActionContext.getContext().getValueStack().push(result);

        } catch (IOException e) {
            e.printStackTrace();
            //上传成功
            Map<String,Object> result = new HashMap<String, Object>();
            result.put("error",1);
            result.put("message","图片上传失败了！");
            //将result序列化为json
            //将result压入值栈中
            ActionContext.getContext().getValueStack().push(result);

        }
        return SUCCESS;

    }

    @Action(value = "image_manager",results = {@Result(type="json")})
    public String image_manage(){
        //根目录路径，可以指定绝对路径，比如 /var/www/attached/
        String rootPath = ServletActionContext.getServletContext().getRealPath("/") + "upload/";
        //根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/

        String rootUrl  = ServletActionContext.getRequest().getContextPath() + "/upload/";
        //图片扩展名
        String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};

        String dirName = ServletActionContext.getRequest().getParameter("dir");
        if (dirName != null) {
            if(!Arrays.<String>asList(new String[]{"image", "flash", "media", "file"}).contains(dirName)){
                try {
                    ServletActionContext.getResponse().getWriter().write("Invalid Directory name.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return INPUT;
            }
            rootPath += dirName + "/";
            rootUrl += dirName + "/";
            File saveDirFile = new File(rootPath);
            if (!saveDirFile.exists()) {
                saveDirFile.mkdirs();
            }
        }
        //根据path参数，设置各路径和URL
        String path = ServletActionContext.getRequest().getParameter("path") != null ? ServletActionContext.getRequest().getParameter("path") : "";
        String currentPath = rootPath + path;
        String currentUrl = rootUrl + path;
        String currentDirPath = path;
        String moveupDirPath = "";
        if (!"".equals(path)) {
            String str = currentDirPath.substring(0, currentDirPath.length() - 1);
            moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
        }

        //排序形式，name or size or type
        String order = ServletActionContext.getRequest().getParameter("order") != null ? ServletActionContext.getRequest().getParameter("order").toLowerCase() : "name";

        //目录不存在或不是目录
        File currentPathFile = new File(currentPath);
        if(!currentPathFile.isDirectory()){

            return INPUT;
        }
        //遍历目录取的文件信息
        List<Hashtable> fileList = new ArrayList<Hashtable>();
        if(currentPathFile.listFiles() != null) {
            for (File file : currentPathFile.listFiles()) {
                Hashtable<String, Object> hash = new Hashtable<String, Object>();
                String fileName = file.getName();
                if(file.isDirectory()) {
                    hash.put("is_dir", true);
                    hash.put("has_file", (file.listFiles() != null));
                    hash.put("filesize", 0L);
                    hash.put("is_photo", false);
                    hash.put("filetype", "");
                } else if(file.isFile()){
                    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                    hash.put("is_dir", false);
                    hash.put("has_file", false);
                    hash.put("filesize", file.length());
                    hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
                    hash.put("filetype", fileExt);
                }
                hash.put("filename", fileName);
                hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
                fileList.add(hash);
            }

            Map<String,Object> result = new HashMap<String,Object>();
            result.put("moveup_dir_path", moveupDirPath);
            result.put("current_dir_path", currentDirPath);
            result.put("current_url", currentUrl);
            result.put("total_count", fileList.size());
            result.put("file_list", fileList);

            //将result写入值栈
            ActionContext.getContext().getValueStack().push(result);
        }



        return SUCCESS;
    }

}
