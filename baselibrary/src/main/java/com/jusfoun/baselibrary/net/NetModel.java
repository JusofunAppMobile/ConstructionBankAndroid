package com.jusfoun.baselibrary.net;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.jusfoun.baselibrary.base.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * model基类
 *
 * @时间 2017/6/29
 * @作者 LiuGuangDan
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class NetModel extends BaseModel implements Serializable {

    private static final long serialVersionUID = 5268625605268545266L;


    public Object data;

    public Object list;

    public int totalCount;

    public Object datelist;
    /**
     * 判断是否为成功状态
     *
     * @return
     */
    public boolean success() {
        return result == 0;
    }

    public <T> T dataToObject(Class<T> clazz) {
        if (data == null) return null;

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(
                        new TypeToken<TreeMap<String, Object>>() {
                        }.getType(),
                        new JsonDeserializer() {
                            @Override
                            public TreeMap<String, Object> deserialize(
                                    JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {

                                TreeMap<String, Object> treeMap = new TreeMap<>();
                                JsonObject jsonObject = json.getAsJsonObject();
                                Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                                for (Map.Entry<String, JsonElement> entry : entrySet) {
                                    treeMap.put(entry.getKey(), entry.getValue());
                                }
                                return treeMap;
                            }
                        }).create();

        return gson.fromJson(gson.toJson(data), clazz);
    }

    public <T> T dataToObject(String key, Class<T> clazz) {
        if (data == null) return null;
        Gson gson = new Gson();
        try {
            JSONObject obj = new JSONObject(gson.toJson(data));
            return new Gson().fromJson(obj.getString(key), clazz);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> dataToList(String key, Class<T> clazz) {
        try {
            Gson gson = new Gson();
            JSONObject obj = new JSONObject(gson.toJson(data));
            JSONArray array = obj.getJSONArray(key);
            if (array.length() > 0) {
                List<T> list = new ArrayList<>();
                for (int i = 0; i < array.length(); i++)
                    list.add(gson.fromJson(array.getString(i), clazz));
                return list;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public <T> T fromJSONObject(JSONObject obj, Class<T> clazz) {
        return new Gson().fromJson(obj.toString(), clazz);
    }

    public JSONObject getDataJSONObject() {
        try {
            return new JSONObject(new Gson().toJson(this)).getJSONObject("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T dataListToObject(Class<T> clazz) {
        if (datelist == null)
            return null;

        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(
                            new TypeToken<TreeMap<String, Object>>() {
                            }.getType(),
                            new JsonDeserializer() {
                                @Override
                                public TreeMap<String, Object> deserialize(
                                        JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {

                                    TreeMap<String, Object> treeMap = new TreeMap<>();
                                    JsonObject jsonObject = json.getAsJsonObject();
                                    Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                                    for (Map.Entry<String, JsonElement> entry : entrySet) {
                                        treeMap.put(entry.getKey(), entry.getValue());
                                    }
                                    return treeMap;
                                }
                            }).create();

            Log.e("tag","ExceptionExceptionException1="+gson.toJson(datelist));

            return gson.fromJson(gson.toJson(datelist), clazz);
        }catch (Exception e){
            Log.e("tag","ExceptionExceptionException="+e);

        }
        return null;
    }
}
