package com.kn.elephant.note.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Kamil Nadłonek on 09-03-2017 email:kamilnadlonek@gmail.com
 */
public class LinkUtilsTest {

    @Test
    public void shouldIgnoreHtmlLinkTag() {
        // Given
        // links and html link which should be ignore
        String test = "<font face=\"Sans\"><a href=\"www.wp.pl\" target=\"_blank\">www.wp.pl</a></font><font>dsafasdfafsadf</font></p><p><font face=\"Lucida Sans\">www.sport.pl</font></p>";
        String expected = "<font face=\"Sans\"><a href=\"www.wp.pl\" target=\"_blank\">www.wp.pl</a></font><font>dsafasdfafsadf</font></p><p><a href=\"http://www.sport.pl\"  target=\"_blank\">www.sport.pl</a></p>";

        // When
        String result = LinkUtils.replaceLinkOnHtmlLinkTag(test);
        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldReplaceTwoLinks() {
        // Given
        // two links
        String test = "<font face=\"Sans\">test1<font>www.kamil.pl</font>ASD<a href=\"www.wp.pl\" target=\"_blank\">www.wp.pl</a></font><font>dsafasdfafsadf</font></test><font face=\"Lucida Sans\">www.sport.pl</font></p>";
        String expected = "<font face=\"Sans\">test1<a href=\"http://www.kamil.pl\"  target=\"_blank\">www.kamil.pl</a>ASD<a href=\"www.wp.pl\" target=\"_blank\">www.wp.pl</a></font><font>dsafasdfafsadf</font></test><a href=\"http://www.sport.pl\"  target=\"_blank\">www.sport.pl</a></p>";

        // When
        String result = LinkUtils.replaceLinkOnHtmlLinkTag(test);
        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldReplaceDuplicateLinks() {
        // Given
        // duplicate links
        String test3 = "<font face=\"Sans\">test1<font>www.kamil.pl</font>ASDtest2<font>www.kamil.pl</font><a href=\"www.wp.pl\" target=\"_blank\">www.wp.pl</a></font><font>dsafasdfafsadf</font></test><font face=\"Lucida Sans\">www.sport.pl</font></p>";

        // When
        // String result2 = LinkUtils.replaceLinkOnHtmlLinkTag(test2);
        String result3 = LinkUtils.replaceLinkOnHtmlLinkTag(test3);

        // Then

        String expected3 = "<font face=\"Sans\">test1<a href=\"http://www.kamil.pl\"  target=\"_blank\">www.kamil.pl</a>ASDtest2<a href=\"http://www.kamil.pl\"  target=\"_blank\">www.kamil.pl</a><a href=\"www.wp.pl\" target=\"_blank\">www.wp.pl</a></font><font>dsafasdfafsadf</font></test><a href=\"http://www.sport.pl\"  target=\"_blank\">www.sport.pl</a></p>";

        // Assert.assertEquals(expected2, result2);
        Assert.assertEquals(expected3, result3);
    }

}