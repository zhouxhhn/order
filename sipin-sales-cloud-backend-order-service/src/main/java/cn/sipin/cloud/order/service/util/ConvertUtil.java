package cn.sipin.cloud.order.service.util;

import cn.sipin.sales.cloud.order.common.PageResponse;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertUtil {

    public static <T> List<T> convertList(Class<T> clazz, List list) throws Exception {
        if (list != null && list.size() > 0) {
            List<T> resultList = new ArrayList<T>();
            for (Object obj : list) {
                T destObj = clazz.newInstance();
                BeanUtils.copyProperties(obj, destObj);
                resultList.add(destObj);
            }
            return resultList;
        }
        return null;
    }

    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if(obj == null)
            return null;

        Map<String, Object> map = new HashMap<String, Object>();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter!=null ? getter.invoke(obj) : null;
            if (value == null || StringUtils.isEmpty(value.toString()))
                continue;

            map.put(key, value);
        }

        return map;
    }

    /**
     *
     * @param map
     * @param beanClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) throws Exception {
        if (map == null)
            return null;

        T obj = beanClass.newInstance();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            Method setter = property.getWriteMethod();
            if (setter != null) {
                setter.invoke(obj, map.get(property.getName()));
            }
        }

        return obj;
    }

    public static <T> T objectToAnotherObject(Object obj, Class<T> clazz, String prefix) {
        try {
            if(obj == null)
                return null;

            Map<String, Method> map = new HashMap<String, Method>();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                map.put(key, getter);
            }


            T obj2 = clazz.newInstance();
            BeanInfo beanInfo2 = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors2 = beanInfo2.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors2) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = map.get(prefix + key);
                if (getter != null) {
                    property.getWriteMethod().invoke(obj2, getter.invoke(obj));
                }

            }

            return obj2;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @Title: getSignatureParams
     * @Description: 根据Map参数,排序后,进行参数拼装
     * @param @param paramsHash
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public static String getSignatureParams(Map<String, String> paramsHash){
        StringBuffer stringBuffer = new StringBuffer();
        for (String key : paramsHash.keySet()) {
            stringBuffer.append(key + "=" + paramsHash.get(key) + "&");
        }

        String params = stringBuffer.substring(0, stringBuffer.length() - 1);
        return params;
    }

    public static <T> PageResponse<T> getPageResponse(Page pageResult, List<T> list) {
        if (pageResult != null) {
            PageResponse<T> resp = new PageResponse<T>();
            resp.setCurrent(pageResult.getCurrent());
            resp.setPages(pageResult.getPages());
            resp.setSize(pageResult.getSize());
            resp.setTotal(pageResult.getTotal());
            resp.setRecords(list);
            return resp;
        }
        return null;
    }

    public static <T> PageResponse<T> getPageResponse(Page pageResult, Class<T> clazz) throws Exception {
        if (pageResult != null) {
            PageResponse<T> resp = new PageResponse<T>();
            resp.setCurrent(pageResult.getCurrent());
            resp.setPages(pageResult.getPages());
            resp.setSize(pageResult.getSize());
            resp.setTotal(pageResult.getTotal());
            if (pageResult.getRecords() != null) {
                List<T> list = convertList(clazz, pageResult.getRecords());
                resp.setRecords(list);
            }
            return resp;
        }
        return null;
    }

    /*public static <T> PageResponse<T> getPageResponse(PageInfo<T> pageInfo) {
        if (pageInfo != null) {
            PageResponse<T> resp = new PageResponse<T>();
            resp.setCurrent(pageInfo.getPageNum());
            resp.setPages(pageInfo.getPages());
            resp.setSize(pageInfo.getSize());
            resp.setTotal(new Long(pageInfo.getTotal()).intValue());
            resp.setRecords(pageInfo.getList());
            return resp;
        }
        return null;
    }*/
}
