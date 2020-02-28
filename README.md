[![](https://jitpack.io/v/wwdablu/SendGridAndroid.svg)](https://jitpack.io/#wwdablu/SendGridAndroid)  

# SendGridAndroid  
An Android library which provides the ability to send email through SendGrid. Ability to send attachment is also present.  

# Usage  
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

	dependencies {
	        implementation 'com.github.wwdablu:SendGridAndroid:1.0.0'
	}
```  
Code:  
```  
SendGridAndroid sendGridAndroid = new SendGridAndroid(SENDGRID_API);

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
```
