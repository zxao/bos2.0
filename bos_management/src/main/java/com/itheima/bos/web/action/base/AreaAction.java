package com.itheima.bos.web.action.base;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.service.base.IAreaService;
import com.itheima.bos.utils.PinYin4jUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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
import org.springframework.data.domain.Page;

import org.springframework.stereotype.Controller;



import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class AreaAction extends ActionSupport implements ModelDriven<Area> {
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


    /**
     * 区域管理中文件的导入
     * @return json数据
     */
    @Action(value = "area_batchImport", results = {@Result(type = "json")})
    public String areaBatchImport() {

        String response = "";

        try {

            Workbook workbook = null;
            //判断提交的文件类型
            if (fileFileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(new FileInputStream(file));
            } else if (fileFileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(new FileInputStream(file));
            }

//        //创建一个workBook对象
//        Workbook workbook = new HSSFWorkbook(new FileInputStream(file));
            //获得sheet
            Sheet sheet = workbook.getSheetAt(0);

            //用来存储Area对象
            List<Area> areas = new ArrayList<Area>();

            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                //获得行
                Row row = sheet.getRow(i);
                //获取单元格中的数据
                String id = row.getCell(0).getStringCellValue();
                String province = row.getCell(1).getStringCellValue();
                String city = row.getCell(2).getStringCellValue();
                String district = row.getCell(3).getStringCellValue();
                String postcode = row.getCell(4).getStringCellValue();

                //使用yinpin4j完成citycode与shortcode的添加
                String[] citycodeArr = PinYin4jUtils.stringToPinyin(city);
                String citycode = Arrays.toString(citycodeArr).substring(1, Arrays.toString(citycodeArr).length() - 1).replace(",", "").replace(" ", "");

                String[] shortArr = PinYin4jUtils.getHeadByString(city+district);
                String shortcode = Arrays.toString(shortArr).substring(1, Arrays.toString(shortArr).length() - 1).replace(",", "").replace(" ", "");


                //封装数据到Area对象中
                Area area = new Area(id, province, city, district, postcode,citycode,shortcode);

                //将area添加到list集合中
                areas.add(area);

            }

            //调用service的areaBatchImport（）
            areaService.areaBatchImport(areas);

            response = "上传文件成功！";

        } catch (Exception e) {
            e.printStackTrace();
            response = "上传文件失败！";

        }

        //返回json数据
        ActionContext.getContext().getValueStack().push(response);


        return SUCCESS;
    }

    private Integer page;
    private Integer rows;

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    /**
     * 分页条件查询
     * @return json数据
     */
    @Action(value = "area_pageQuery",results = {@Result(type = "json")})
    public String areaPageQuery() {
        //调用service的queryPage方法得到page对象
        Page<Area> pageData = areaService.queryPage(page,rows,area);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("total",pageData.getTotalElements());
        map.put("rows",pageData.getContent());

        //将map中的数据压入值栈中
        ActionContext.getContext().getValueStack().push(map);
        return SUCCESS;
    }

    /**
     * 区域数据的添加
     * @return
     */
    @Action(value = "area_save",results = {@Result(type = "redirect",location = "./pages/base/area.html")})
    public String areaSave() {
        //调用service的save()
        areaService.save(area);
        return SUCCESS;
    }

    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    /**
     * 区域数据的删除
     * @return
     */
    @Action(value = "area_del",results = {@Result(type = "redirect",location = "./pages/base/area.html")})
    public String areaDel() {
        areaService.areaDel(ids);
        return SUCCESS;
    }

    /**
     * 区域数据的查询
     * @return
     */
    @Action(value = "area_findAll",results = {@Result(type = "json")})
    public String areaFindAll() {
        List<Area> areaList = areaService.areaFindAll();
        ActionContext.getContext().getValueStack().push(areaList);
        return SUCCESS;
    }

    /**
     * 查询省
     * @return
     */
    @Action(value = "area_findProvince",results = {@Result(type = "json")})
    public String areaFindProvince() {
        List<String> provinceList = areaService.areaFindProvince();
        List<Area> areaList = new ArrayList<Area>();
        for (String province : provinceList){
            Area area2 = new Area();
            area2.setProvince(province);
            areaList.add(area2);
        }
        ActionContext.getContext().getValueStack().push(areaList);
        return SUCCESS;
    }

    @Action(value = "area_findCity",results = {@Result(type = "json")})
    public String areaFindCity() {
        //get方式处理中文乱码
        String province = area.getProvince();
        try {
            province = new String(province.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        List<String> cityList = areaService.areaFindCity(province);
        List<Area> areaList = new ArrayList<Area>();
        for (String city : cityList){
            Area area2 = new Area();
            area2.setCity(city);
            areaList.add(area2);
        }
        ActionContext.getContext().getValueStack().push(areaList);
        return SUCCESS;
    }

    private String params;

    public void setParams(String params) {
        this.params = params;
    }

    @Action(value = "area_findDistrictByProvinceAndCity",results = {@Result(type = "json")})
    public String areaFindDistrictByProvinceAndCity() {
        //get方式处理中文乱码
        try {
            params = new String(params.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] split = params.split(",");
        String province = split[0];
        String city = split[split.length-1];

        List<String> districtList = areaService.areaFindDistrictByProvinceAndCity(province,city);
        List<Area> areaList = new ArrayList<Area>();
        for (String district : districtList){
            Area area2 = new Area();
            area2.setDistrict(district);
            areaList.add(area2);
        }
        ActionContext.getContext().getValueStack().push(areaList);
        return SUCCESS;
    }


}
