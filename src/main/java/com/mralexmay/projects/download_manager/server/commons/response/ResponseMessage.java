package com.mralexmay.projects.download_manager.server.commons.response;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ResponseMessage {
    private Map<String, Object> response = new HashMap<>();


    public ResponseMessage() {
    }

    public ResponseMessage(Object value) {
        addParam("message", value);
    }

    public ResponseMessage(String key, Object param) {
        addParam(key, param);
    }


    public ResponseMessage addParam(String key, Object param) {
        response.put(key, param);
        return this;
    }

    public String asJson() {
        StringBuilder sb = new StringBuilder();
        Gson gson = new Gson();

        sb.append("{");
        for (var param : response.keySet()) {
            sb
                    .append("\"")
                    .append(param)
                    .append("\"")
                    .append(":")
                    .append(gson.toJson(response.get(param)))
                    .append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.append("}");

        return sb.toString();
    }
}
