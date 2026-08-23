package dev.spoocy.adapter.utils;

import dev.spoocy.utils.common.text.StringUtils;

import java.util.regex.Pattern;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ColorUtils {

    public static final Pattern COLOR_PATTERN = Pattern.compile("(?i)" + '\u00A7' + "[0-9A-FK-ORX]");

    public static String removeColors(String text) {
        return StringUtils.isNullOrEmpty(text) ? text : COLOR_PATTERN.matcher(text).replaceAll("");
    }

    public static String removeColors(String text, char code) {
        if(StringUtils.isNullOrEmpty(text)) {
            return text;
        }

        Pattern pattern = Pattern.compile("(?i)" + code + "[0-9A-FK-ORX]");
        return pattern.matcher(text).replaceAll("");
    }

}
