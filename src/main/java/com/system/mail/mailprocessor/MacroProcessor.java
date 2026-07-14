package com.system.mail.mailprocessor;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MacroProcessor {
    private static final String PRE = "\\[\\$";
    private static final String POST = "\\$\\]";
    private static final String MACRO_COMMA = ",";
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile(PRE + "(.+?)" + POST);

    /**
     * target 에 [$key$] 형태로 실제 사용된 매크로 키 목록을 등장 순서대로 추출한다.
     */
    public Set<String> extractKeys(String target) {
        Set<String> keys = new LinkedHashSet<>();
        if (target == null) {
            return keys;
        }
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(target);
        while (matcher.find()) {
            keys.add(matcher.group(1));
        }
        return keys;
    }

    public String process(String macroKey, String macroValue, String target) {
        HashMap<String, String> macro = makeMacroMap(macroKey, macroValue);
        for (String key : macro.keySet()) {
            String value = macro.get(key);
            target = target.replaceAll(PRE + key + POST, value);
        }
        return target;
    }


    private String removeMacro(String content) {
        return content.replaceAll("[" + PRE + POST + "]", "");
    }


    private HashMap<String, String> makeMacroMap(String macroKey, String macroValue) {
        String[] macroKeyArr = macroKey.split(MACRO_COMMA);
        int macroCnt = macroKeyArr.length;
        String[] macroValueArr = macroValueToArr(macroValue, macroCnt);
        HashMap<String,String> macroMap = new HashMap<>();

        for(int i=0; i < macroCnt; i++) {
            macroMap.put(macroKeyArr[i].trim(), macroValueArr[i]);
        }
        return macroMap;
    }

    private String[] macroValueToArr(String macroValue, int macroCnt) {
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
