package com.ktm.homp.webimageviewerforjava.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.ktm.homp.webimageviewerforjava.option.FilterOption;
import com.ktm.homp.webimageviewerforjava.option.type.LicenseType;
import com.ktm.homp.webimageviewerforjava.option.type.SortBy;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.SocketTimeoutException;

public class HtmlParsingRequester {

    public static final String BASE_URL = "https://www.gettyimages.com";
    public static final String REQUEST_BASE_URL = BASE_URL + "/photos/collaboration?autocorrect=none";
    public static final String SORT_FORMAT = "sort=%s";
    public static final String LICENSE_FORMAT = "license=%s";
    public static final String PAGE_FORMAT = "page=%s";

    HttpTask httpTask;

    public static String BuildURL(FilterOption filterOption, int page){

        String sort = String.format(SORT_FORMAT, filterOption.getSortBy().getName());
        String license = filterOption.getLicenseType().getName();

        if(license != null){
            license = "&" + String.format(LICENSE_FORMAT, license);
        }

        String pageNumber = String.format(PAGE_FORMAT, page);

        String combineURL = REQUEST_BASE_URL + "&" + pageNumber + "&" + sort + license;

        return combineURL;
    }

    public void request(String url, HtmlParsingCallback callback) {
        httpTask = new HttpTask(url, callback);
        httpTask.execute();
    }

    private class HttpTask extends AsyncTask<Void, Void, TaskResult> {
        HtmlParsingCallback callback;
        String url;

        public HttpTask(String url, HtmlParsingCallback callback) {
            this.callback = callback;
            this.url = url;
        }

        @Override
        protected TaskResult doInBackground(Void... nothing) {

            Connection.Response response;
            TaskResult taskResult = new TaskResult();

            try {
                Log.d("doInBackground", url);
                Connection connection = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");

                response = connection
                        .method(Connection.Method.GET)
                        .timeout(10000)
                        .execute();

                taskResult.setStatusCode(response.statusCode());
                taskResult.setStatusMsg(response.statusMessage());
                taskResult.setHtmlBody(response.parse());

            }catch (SocketTimeoutException e){
                e.printStackTrace();
                taskResult.setStatusCode(0);
                taskResult.setStatusMsg("SocketTimeoutException");
            } catch (Exception e) {
                e.printStackTrace();
                taskResult.setStatusCode(0);
                taskResult.setStatusMsg("Unknown Error");
            }

            return taskResult;
        }

        @Override
        protected void onPostExecute(TaskResult result) {

            if(result.getStatusCode() >= 200 && result.getStatusCode() <= 299){
                callback.onResult(result.htmlBody.clone());

            }else{
                callback.onError(result.getStatusCode(), result.getStatusMsg());
            }

        }
    }

    class TaskResult{
        int statusCode;
        String statusMsg;
        Document htmlBody;

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getStatusMsg() {
            return statusMsg;
        }

        public void setStatusMsg(String statusMsg) {
            this.statusMsg = statusMsg;
        }

        public Document getHtmlBody() {
            return htmlBody;
        }

        public void setHtmlBody(Document htmlBody) {
            this.htmlBody = htmlBody;
        }
    }

}
