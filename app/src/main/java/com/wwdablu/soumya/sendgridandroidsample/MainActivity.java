package com.wwdablu.soumya.sendgridandroidsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.wwdablu.soumya.sendgridandroid.EMail;
import com.wwdablu.soumya.sendgridandroid.SendGridAndroid;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SendGridAndroid sendGridAndroid = new SendGridAndroid("");

        EMail eMail = new EMail();

        eMail.addTo("John Doe", "john.doe@gmail.com")
                .setSubject("Test Email")
                .setReplyTo("No One", "no.reply@gmail.com")
                //.setTextContent("Hello World")
                .setHtmlContent(getDemoHtml())
                //.addAttachment("attachment.pdf", pdfFile)
                .setFrom("Postman", "postman@gmail.com");

        sendGridAndroid.send(eMail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Void>() {
                    @Override
                    public void onNext(Void aVoid) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getDemoHtml() {

        return "<table border=\"1\">" +
                "<tr><td>sample</td></tr>" +
                "<tr>Hello World</tr>" +
                "</table>";
    }
}
