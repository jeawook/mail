package com.system.mail.mailprocessor;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MacroProcessor {
    private static final String PRE = "\\[\\$";
    private static final String POST = "\\$\\]";
    private static final String MACRO_COMMA = ",";
    public String process(String macroKey, String macroValue, String content) {
        HashMap<String, String> macro = makeMacroMap(macroKey, macroValue);
        for (String key : macro.keySet()) {
            String value = macro.get(key);
            content = content.replaceAll(PRE + key + POST, value);
        }
        return removeMacro(content);
    }


    private String removeMacro(String content) {
        String s = content.replaceAll("[" + PRE + POST + "]", "");
        return s;
    }


    private HashMap<String, String> makeMacroMap(String macroKey, String macroValue) {
        String[] macroKeyArr = macroKey.split(MACRO_COMMA);
        int macroCnt = macroKeyArr.length;
        String[] macroValueArr = getMacroValueToArr(macroValue, macroCnt);
        HashMap<String,String> macroMap = new HashMap<>();

        for(int i=0; i < macroCnt; i++) {
            macroMap.put(macroKeyArr[i].trim(), macroValueArr[i]);
        }
        return macroMap;
    }

    private String[] getMacroValueToArr(String macroValue, int macroCnt) {
        String[] macroValueArr = new String[macroCnt];
        int pre = 0;
        int i;
        int end = 0;
        for (i=0; i < macroCnt-1; i++) {
            end = macroValue.indexOf(MACRO_COMMA);
            macroValueArr[i] = macroValue.substring(pre, end);
            macroValue = macroValue.substring(end + MACRO_COMMA.length());
        }
        macroValueArr[i] = macroValue;
        return macroValueArr;
    }
}
