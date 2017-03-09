package com.kn.elephant.note.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 07-03-2017 email:kamilnadlonek@gmail.com
 */
@Log4j2
public class LinkUtils {
    private static final String URL_REGEX = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private static Pattern URL_PATTERN = Pattern.compile("((https|ftp|file)?(://|www.)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])(?!<)");
    private static Pattern HTML_TAG_WITH_URL_PATTERN = Pattern
            .compile("(?i)<[a-z]*([^>]+)>((https|ftp|file)?(://|www.)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])</[a-z]*>");

    public static boolean isUrl(String url) {

        log.info("Validate url: {}", url);
        if (StringUtils.isNotEmpty(url)) {
            Matcher matcher = URL_PATTERN.matcher(url);
            return matcher.find();
        }
        return false;
    }

    public static String replaceLinkOnHtmlLinkTag(String content) {
        Matcher matcher = HTML_TAG_WITH_URL_PATTERN.matcher(content);
        while (matcher.find()) {
            if (!matcher.group(0).startsWith("<a href")) {
                log.debug("Found url:" + matcher.group(2));
                String htmlUrl = createHtmlLink(matcher.group(2));
                content = content.replace(matcher.group(), htmlUrl);
            }
        }

        return content;
    }

    private static String createHtmlLink(String url) {
        if (url.startsWith("http")) {
            return "<a href=\"" + url + "\"  target=\"_blank\">" + url + "</a>";
        }
        return "<a href=\"http://" + url + "\"  target=\"_blank\">" + url + "</a>";

    }
}
