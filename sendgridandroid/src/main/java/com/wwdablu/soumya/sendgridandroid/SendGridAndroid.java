package com.wwdablu.soumya.sendgridandroid;

import android.text.TextUtils;
import android.util.Base64;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class SendGridAndroid {

    private static final String KEY_PERSONALIZATIONS = "personalizations";

    private static final String SENDGRID_URL = "https://api.sendgrid.com/v3/mail/send";

    private String mApiKey;

    public SendGridAndroid(@NonNull String apiKey) {
        mApiKey = apiKey;
    }

    public Observable<Void> send(@NonNull EMail email) {

        return Observable.create(emitter -> {

            if(email.getHtmlContent() == null) {
                email.setHtmlContent(" ");
            }

            if(email.getTextContent() == null) {
                email.setTextContent(" ");
            }

            try {
                sendAndGetResponse(getBodyContent(email), emitter);
                emitter.onComplete();
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        });
    }

    private void sendAndGetResponse(String body, ObservableEmitter<Void> emitter) {

        Request.Builder request = new Request.Builder();

        request.header("Authorization", "Bearer " + mApiKey);
        request.addHeader("Content-Type", "application/json; charset=utf-8");
        if (body != null && !TextUtils.isEmpty(body)) {
            RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json; charset=utf-8"));
            request.post(requestBody);
        }

        request.url(SENDGRID_URL);

        OkHttpClient.Builder builder;
        builder = new OkHttpClient.Builder();
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(15, TimeUnit.SECONDS);
        builder.networkInterceptors().add(new UserAgentInterceptor());
        OkHttpClient okHttpClient = builder.build();

        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                emitter.onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String res = response.body().string();
                    if (Integer.toString(response.code()).startsWith("2")) {
                        emitter.onComplete();
                    } else {
                        emitter.onError(new Exception(res));
                    }

                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

    private String getBodyContent(EMail email) throws Exception {

        JSONObject rootObject = new JSONObject();

        JSONArray personalizationsArray = new JSONArray();

        //Add subject
        JSONObject personalizationsObject = new JSONObject();
        personalizationsObject.put("subject", email.getSubject());

        //Add TOs
        JSONArray tosArray = new JSONArray();
        LinkedHashMap<String, String> tos = email.getTos();

        for(String toEmail : tos.keySet()) {

            JSONObject tosObject = new JSONObject();
            tosObject.put("email", toEmail);
            tosObject.put("name", tos.get(toEmail));
            tosArray.put(tosObject);
        }
        personalizationsObject.put("to", tosArray);

        //Add CCs
        JSONArray ccsArray = new JSONArray();
        LinkedList<String> ccs = email.getCCs();

        for(String ccEmail : ccs) {

            JSONObject ccsObject = new JSONObject();
            ccsObject.put("email", ccEmail);
            ccsArray.put(ccsObject);
        }

        if(!ccs.isEmpty()) {
            personalizationsObject.put("cc", ccsArray);
        }

        //Add BCCs
        JSONArray bccsArray = new JSONArray();
        LinkedList<String> bccs = email.getBCCs();

        for(String bcc : bccs) {

            JSONObject bccsObject = new JSONObject();
            bccsObject.put("email", bcc);
            bccsArray.put(bccsObject);
        }

        if(!bccs.isEmpty()) {
            personalizationsObject.put("bcc", bccsArray);
        }

        personalizationsArray.put(personalizationsObject);
        rootObject.put(KEY_PERSONALIZATIONS, personalizationsArray);

        //Add From Information
        JSONObject fromObject = new JSONObject();
        fromObject.put("email", email.getFrom());
        fromObject.put("name", email.getFromName());
        rootObject.put("from", fromObject);

        //Add reply to information
        JSONObject replyToObject = new JSONObject();
        replyToObject.put("email", email.getReplyTo());
        replyToObject.put("name", email.getReplyToName());
        rootObject.put("reply_to", replyToObject);

        //Attachments
        if(email.getAttachments().size() != 0) {
            JSONArray attachmentArray = new JSONArray();
            for (Map.Entry<String, File> entry : email.getAttachments().entrySet()) {
                JSONObject jsonObject = new JSONObject();
                InputStream fileInputStream = new FileInputStream(entry.getValue());
                byte[] imageBytes = new byte[(int) entry.getValue().length()];
                fileInputStream.read(imageBytes, 0, imageBytes.length);
                fileInputStream.close();
                String imageStr = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

                jsonObject.put("content", imageStr);
                jsonObject.put("filename", entry.getValue().getName());

                attachmentArray.put(jsonObject);
            }

            rootObject.put("attachments", attachmentArray);
        }

        //Add contents
        JSONArray contentArray = new JSONArray();

        JSONObject textContentObject = new JSONObject();
        textContentObject.put("type", "text/plain");
        textContentObject.put("value", email.getTextContent());
        contentArray.put(textContentObject);

        JSONObject htmlContentObject = new JSONObject();
        htmlContentObject.put("type", "text/html");
        htmlContentObject.put("value", email.getHtmlContent());
        contentArray.put(htmlContentObject);

        rootObject.put("content", contentArray);
        return rootObject.toString();
    }

    private class UserAgentInterceptor implements Interceptor {

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request requestWithUserAgent = originalRequest.newBuilder()
                    .header("User-Agent", "sendgridandroid" + ";java")
                    .build();
            return chain.proceed(requestWithUserAgent);
        }
    }
}
