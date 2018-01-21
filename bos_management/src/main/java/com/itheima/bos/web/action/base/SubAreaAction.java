package com.itheima.bos.web.action.base;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.IAreaService;
import com.itheima.bos.service.base.ISubAreaService;
import com.itheima.bos.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.io.IOExceptionWithCause;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class SubAreaAction extends BaseAction<SubArea> {

    @Autowired
    private ISubAreaService subAreaService;

    //注入areaService
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

    /**
     * 分区数据文件导入
     * @return
     */
    @Action(value = "subArea_import",results = {@Result(type = "json")})
    public String subAreaImport() {
        //解析Excel文件
        String msg="";
        try{
            Workbook workbook = null;
            //判断文件的后缀
            if(fileFileName.endsWith(".xls")){
                workbook = new HSSFWorkbook(new FileInputStream(file));
            }else if(fileFileName.endsWith(".xlsx")){
                workbook = new XSSFWorkbook(new FileInputStream(file));
            }else {
                ActionContext.getContext().getValueStack().push("导入的文件类型不正确，只允许导入Excel文件");
                return SUCCESS;
            }

            //得到sheet
            Sheet sheet = workbook.getSheetAt(0);
            //使用List存储subArea
            List<SubArea> list = new ArrayList<SubArea>();
            //获得row
            for (int i=1;i<=sheet.getLastRowNum();i++) {
                Row row = sheet.getRow(i);
                //得到单元格
                String id = row.getCell(0).getStringCellValue();
                String province = row.getCell(1).getStringCellValue();
                String city = row.getCell(2).getStringCellValue();
                String district = row.getCell(3).getStringCellValue();
                String keyWords = row.getCell(4).getStringCellValue();
                String startNum = row.getCell(5).getStringCellValue();
                String endNum = row.getCell(6).getStringCellValue();
                Character single = row.getCell(7).getStringCellValue().toCharArray()[0];
                String assistKeyWords = row.getCell(8).getStringCellValue();
                String fixedAreaId = row.getCell(9).getStringCellValue();

                FixedArea fixedArea = new FixedArea();
                fixedArea.setId(fixedAreaId);
                //根据省市区查询area
                Area area = areaService.findByProvinceAndCityAndDistrict(province,city,district);

                SubArea subArea = new SubArea(id,startNum,endNum,single,keyWords,assistKeyWords,area,fixedArea);
                list.add(subArea);
            }

            //调用service层的subAreaImport方法
            subAreaService.subAreaImport(list);

            msg = "数据导入成功！";

        }catch (Exception e) {
            e.printStackTrace();
            msg = "数据导入失败";
        }

        //将msg压入值栈
        ActionContext.getContext().getValueStack().push(msg);

        return SUCCESS;
    }

    /**
     * 分区管理的分页条件查询
     * @return
     */
    @Action(value = "subArea_queryPage",results ={@Result(type = "json")})
    public String subAreaQueryPage() {
        Page<SubArea> pageDate = subAreaService.subAreaQueryPage(page,rows,model);
        this.pushPageDataToValueStack(pageDate);
        return SUCCESS;
    }


    /**
     * 分区的保存
     * @return
     */
    @Action(value = "subArea_Save",results = {@Result(type = "redirect",location = "./pages/base/sub_area.html")})
    public String subAreaSave() {
        subAreaService.save(model);
        return SUCCESS;
    }

    @Action(value = "subArea_export")
    public String subAreaExport() {
        //从数据库将出具查询出来
        List<SubArea> subAreaList = subAreaService.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("第一页");
        //创建表头
        Row rowFist = sheet.createRow(0);
        rowFist.createCell(0).setCellValue("分区编号");
        rowFist.createCell(1).setCellValue("定区编码");
        rowFist.createCell(2).setCellValue("区域编码");
        rowFist.createCell(3).setCellValue("关键字");
        rowFist.createCell(4).setCellValue("起始号");
        rowFist.createCell(5).setCellValue("结束号");
        rowFist.createCell(6).setCellValue("单双号");
        rowFist.createCell(7).setCellValue("位置信息");

        for (int i=0;i<subAreaList.size();i++) {
            Row row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(subAreaList.get(i).getId());
            row.createCell(1).setCellValue(subAreaList.get(i).getFixedArea().getId());
            row.createCell(2).setCellValue(subAreaList.get(i).getArea().getId());
            row.createCell(3).setCellValue(subAreaList.get(i).getKeyWords());
            row.createCell(4).setCellValue(subAreaList.get(i).getStartNum());
            row.createCell(5).setCellValue(subAreaList.get(i).getEndNum());
            row.createCell(6).setCellValue(subAreaList.get(i).getSingle());
            row.createCell(7).setCellValue(subAreaList.get(i).getAssistKeyWords());

        }
        String fileName="qysj.xlsx";

        //设置两个头
        ServletActionContext.getResponse().setContentType(ServletActionContext.getServletContext().getMimeType(fileName));
        ServletActionContext.getResponse().setHeader("content-disposition","attachment;filename="+fileName);
        try {
            workbook.write(ServletActionContext.getResponse().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NONE;
    }

    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    @Action(value = "subArea_batchDel",results = {@Result(type = "redirect",location = "./pages/base/sub_area.html")})
    public String subAreaBatchDel() {
        subAreaService.subAreaBatchDel(ids);
        return SUCCESS;
    }
}
