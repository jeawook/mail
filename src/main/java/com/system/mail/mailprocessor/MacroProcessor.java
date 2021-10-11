package com.system.mail.mailprocessor;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class MacroProcessor {
    private static String pre = "[$";
    private static String post = "$]";

    public String process(String macroKey, String macroValue, String content) {
        HashMap<String, String> macro = makeMacroMap(macroKey, macroValue);
        for (String key : macro.keySet()) {
            String value = macro.get(key);
            content = content.replaceAll(pre + key + post, value);
        }
        removeMacro(content);
        return content;
    }

    private void removeMacro(String content) {
        while (content.contains("[$") && content.contains("$]")) {
            String value = content.substring(content.indexOf("[$"), content.indexOf("$]")+2);
            content = content.replace(value, "");
        }
    }


    private HashMap<String, String> makeMacroMap(String macroKey, String macroValue) {
        String[] macroKeyArr = macroKey.split(",");
        int macroCnt = macroKeyArr.length;
        String[] macroValueArr = getMacroValueToArr(macroValue, macroCnt);
        HashMap<String,String> macroMap = new HashMap<>();

        for(int i=0; i < macroCnt; i++) {
            macroMap.put(macroKeyArr[i], macroValueArr[i]);
        }
        return macroMap;
    }

    private String[] getMacroValueToArr(String macroValue, int macroCnt) {
        String[] macroValueArr = new String[macroCnt];
        int pre = 0;
        int i;
        int end = 0;

        for (i=0; i < macroCnt-1; i++) {
            end = macroValue.indexOf(",");
            macroValueArr[i] = macroValue.substring(pre, end);
        }
        macroValueArr[i+1] = macroValue.substring(end);
        return macroValueArr;
    }
}
