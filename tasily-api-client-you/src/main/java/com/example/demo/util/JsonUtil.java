package com.example.demo.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JsonUtil {

    //将obj转换成JSONArray
    public JSONArray ObjectToJSONArray(Object obj, String ...filters) {
        SimplePropertyPreFilter spp = new SimplePropertyPreFilter();
        for (String filter : filters) {
            spp.getExcludes().add(filter);
        }
        String jsonString = JSONObject.toJSONString(obj, spp, SerializerFeature.DisableCircularReferenceDetect);
        return JSONArray.parseArray(jsonString);
    }

    //将Object转换成JSONObject
    public JSONObject ObjectToJSONObject(Object obj, String ...filters) {
        SimplePropertyPreFilter spp = new SimplePropertyPreFilter();
        for (String filter : filters) {
            spp.getExcludes().add(filter);
        }
        String jsonString = JSONObject.toJSONString(obj, spp, SerializerFeature.DisableCircularReferenceDetect);
        return JSONObject.parseObject(jsonString);
    }

    //JSONArray转换成JSONObject
    public JSONObject JsonArrayToJsonObject(JSONArray jo) throws Exception{
        JSONObject ret = new JSONObject();
        ret.put("data", jo == null ? "[]".getBytes() : jo.toJSONString().getBytes("UTF-8"));
        return ret;
    }

    //JSONObject转换成JSONArray
    public JSONArray JSONObjectToJSONArray(JSONObject object){
        JSONArray array = new JSONArray();
        String strJson=object.toJSONString();
        array.add(strJson);
        return array;
    }

    //JSONArray转换成List
    public List JSONArrayToList(JSONArray jsonArray){
        List list = new ArrayList();
        jsonArray.forEach(array->{
            list.add(array);
        });
        return list;
    }

    //返回成功状态码
    public JSONObject success(Object content) {
        JSONObject retObj = new JSONObject();
        retObj.put("status", "success");
        if(content != null) retObj.put("content", content);
        return retObj;
    }

    //返回失败状态码
    public JSONObject failure(int code, String info, Object content) {
        JSONObject retObj = new JSONObject();
        retObj.put("status", "error");
        if(code != 0) retObj.put("code", code);
        if(info != null) retObj.put("info", info);
        if(content != null) retObj.put("content", content);
        return retObj;
    }


}
