package com.ktm.homp.webimageviewerforjava.utils;

import org.jsoup.nodes.Document;

public interface HtmlParsingCallback {

	void onResult(Document result);
	void onError(int statusCode, String statusMsg);

}
