package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * 统一的信息输出类----进行信息的返回
 */
public class ResultJSONUtils {
    public static void write(HttpServletResponse response, String jsonStr) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.println(jsonStr);

    }


    /**
     * 此方法是在登录时新加的
     * @param response
     * @param map
     * @throws IOException
     */
    public static void writeMap(HttpServletResponse response, HashMap<String,Object> map) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("appliction/json");
        PrintWriter writer = response.getWriter();

        ObjectMapper mapper = new ObjectMapper();
        writer.println(mapper.writeValueAsString(map));
    }
}
