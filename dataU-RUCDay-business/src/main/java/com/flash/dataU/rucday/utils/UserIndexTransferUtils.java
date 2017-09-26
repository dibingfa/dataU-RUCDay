package com.flash.dataU.rucday.utils;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/26.
 */
public class UserIndexTransferUtils {

    /**
     * 解析用户最后一条阅读消息的拼接字符串
     */
    public static Map<String, Integer> parseUserLastReadMsgIndex(String lastReadMsgIndexStr) {
        if (StringUtils.isEmpty(lastReadMsgIndexStr)) {
            return new HashMap<String, Integer>(0);
        }
        String[] groupAndIndexs = lastReadMsgIndexStr.split(",");
        Map<String, Integer> lastReadMsgIndexMap = new HashMap<String, Integer>(groupAndIndexs.length);
        for (String groupAndIndex:groupAndIndexs) {
            String[] split = groupAndIndex.split(":");
            String groupGuid = split[0];
            int index = Integer.parseInt(split[1]);
            lastReadMsgIndexMap.put(groupGuid, index);
        }
        return lastReadMsgIndexMap;
    }

    /**
     * 生成用户最后一条阅读消息的拼接字符串
     */
    public static String generateUserLastReadMsgIndex(Map<String, Integer> lastReadMsgIndexMap) {
        String[] lastReadMsgIndexs = new String[lastReadMsgIndexMap.size()];
        int i = 0;
        for (Map.Entry<String, Integer> entry:lastReadMsgIndexMap.entrySet()) {
            lastReadMsgIndexs[i] = entry.getKey() + ":" + entry.getValue();
            i += 1;
        }
        return String.join(",", lastReadMsgIndexs);
    }

}
