package com.itheima.bos.utils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;

import javax.jws.soap.SOAPBinding;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {
    protected T model;
    @Override
    public T getModel() {
        return model;
    }

    public BaseAction() {
        //通过子类对象获取父类的字节码文件
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        //获取类型第一个泛型参数
        Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        //获取泛型的实例
        try {
            model = modelClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.out.println("模型构造失败！");
        }

    }
    //接收分页查询参数
    protected Integer page;
    protected Integer rows;

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    //将分页查询的结果压入值栈中
    public void pushPageDataToValueStack(Page<T> pageData) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",pageData.getTotalElements());
        map.put("rows",pageData.getContent());
        //将map压入值栈
        ActionContext.getContext().getValueStack().push(map);

    }
}
