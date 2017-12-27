package com.itheima.bos.web.action.base;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.service.base.IAreaService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class AreaAction extends ActionSupport implements ModelDriven<Area>{
    private Area area = new Area();
    
    @Override
    public Area getModel() {
        return area;
    }
    
    @Autowired
    private IAreaService areaService;
    
    private File file;
    private String fileFileName;

    public void setFile(File file) {
        this.file = file;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    @Action(value = "area_batchImport",results = {@Result(type = "json")})
    public String areaBatchImport() {

        String response = "";

        try{

            Workbook workbook = null;
            //判断提交的文件类型
            if(fileFileName.endsWith(".xls")){
                workbook = new HSSFWorkbook(new FileInputStream(file));
            }else if(fileFileName.endsWith(".xlsx")){
                workbook = new XSSFWorkbook(new FileInputStream(file));
            }

//        //创建一个workBook对象
//        Workbook workbook = new HSSFWorkbook(new FileInputStream(file));
            //获得sheet
            Sheet sheet = workbook.getSheetAt(0);

            for (int i=1;i<sheet.getLastRowNum();i++) {
                //获得行
                Row row = sheet.getRow(i);
                //获取单元格中的数据
                String id = row.getCell(0).getStringCellValue();
                String province = row.getCell(1).getStringCellValue();
                String city = row.getCell(2).getStringCellValue();
                String district = row.getCell(3).getStringCellValue();
                String postcode = row.getCell(4).getStringCellValue();
                //封装数据到Area对象中
                Area area = new Area(id,province,city,district,postcode);

                //调用service的areaBatchImport(area)方法
                areaService.areaBatchImport(area);

            }

            response="上传文件成功！";

        }catch (Exception e){
            e.printStackTrace();
            response="上传文件失败！";

        }

        //返回json数据
        ActionContext.getContext().getValueStack().push(response);


        return SUCCESS;
    }
}
