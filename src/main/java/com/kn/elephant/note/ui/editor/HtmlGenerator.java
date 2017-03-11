package com.kn.elephant.note.ui.editor;

/**
 * Created by Kamil Nadłonek on 11-03-2017
 * email:kamilnadlonek@gmail.com
 */
public class HtmlGenerator {

	public static String test(String content) {
		
		String cssStyle = "\t<style type=\"text/css\">\n" + "\t\t\thtml, body {\n" + "\t\t\t\theight: 100%;\n" + "\t\t\t\tmin-height: 100%;\n"
			+ "\t\t\t\tmargin: 0;\n" + "\t\t\t\tpadding: 0;\n" + "\t\t\t}\n" + "\t\t\t.split-pane-divider {\n" + "\t\t\t\tbackground: #aaa;\n" + "\t\t\t}\n"
			+ "\t\t\t#left-component {\n" + "\t\t\t\twidth: 20em;\n" + "\t\t\t}\n" + "\t\t\t#divider {\n"
			+ "\t\t\t\tleft: 20em; /* same as left component width */\n" + "\t\t\t\twidth: 5px;\n" + "\t\t\t}\n" + "\t\t\t#right-component {\n"
			+ "\t\t\t\tleft: 20em;\n" + "\t\t\t\tmargin-left: 5px; /* same as divider width */\n" + "\t\t\t}\n" + "\t\t</style>";
		
		String hyperlinkHtml = "<div style=\"width: 100%;\">\n" + "   <div style=\"float:left; width: 49%; border-style: solid;\"><p>LEFT<br /><br /></p></div>\n"
			+ "   <div style=\"float:right; width: 49%; border-style: solid;\"><p>RIGHT<br /><br /></p></div>\n" + "</div><div style=\"clear:both\"></div>";

		content += cssStyle + hyperlinkHtml;
		return content;
	}
}
