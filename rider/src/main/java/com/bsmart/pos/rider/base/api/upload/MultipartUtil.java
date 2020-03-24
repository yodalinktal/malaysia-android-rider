package com.bsmart.pos.rider.base.api.upload;


import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultipartUtil {

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public static void putRequestBodyMap(Map map, String key, String value) {
        putRequestBodyMap(map, key, createPartFromString(value));
    }

    @NonNull
    public static RequestBody createPartFromString(String descriptionString) {
        if (descriptionString == null) {
            descriptionString = "";
        }
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    public static void putRequestBodyMap(Map map, String key, RequestBody body) {
        if (!TextUtils.isEmpty(key) && body != null) {
            map.put(key, body);
        }
    }

    public static Map<String, RequestBody> convertMap(Map<String, String> origin) {
        Iterator<String> keys = origin.keySet().iterator();
        Map<String, RequestBody> target = new HashMap<>();
        while (keys.hasNext()) {
            String key = keys.next();
            putRequestBodyMap(target, key, origin.get(key));
        }
        return target;
    }

    public static List<MultipartBody.Part> getUploadFiles(String key, List<String> files) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            try {
                // File file = new File(RealPathUtil.getRealPath(App.ctx, files.get(i)));
                File file = new File(files.get(i));
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("application/otcet-stream"), file);
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData(key, URLEncoder.encode(file.getName(), "UTF-8"), requestFile);
                parts.add(body);
            } catch (Exception e) {
            }
        }
        return parts;
    }
}
