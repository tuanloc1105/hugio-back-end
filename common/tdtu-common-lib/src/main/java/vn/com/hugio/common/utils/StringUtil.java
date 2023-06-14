package vn.com.hugio.common.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtil {

    public static String removeAccent(String s) {
        if (s == null)
            return null;
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    public static String addZeroLeadingNumber(long number, String prefix) {
        return String.format(
                "%s%06d",
                prefix,
                number
        );
    }

    public static boolean isTrustIp(String ip, String[] lstAllowIp) {
        String pattern = null;
        for (String allowIp : lstAllowIp) {
            pattern = allowIp.replaceAll("[*]", "[0-9.]*");
            if (ip.matches(pattern))
                return true;
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isNullOrEmpty(String str) {
        return (str == null || str.trim().equals(""));
    }


}
