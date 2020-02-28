package com.wwdablu.soumya.sendgridandroid;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public final class EMail {

    //Stores the list of to and name
    private LinkedHashMap<String, String> mTos;

    //Stores the list of CCs
    private LinkedList<String> mCCs;

    //Stores the list of BCCs
    private LinkedList<String> mBCCs;

    private String mFrom;
    private String mFromName;

    private String mReplyTo;
    private String mReplyToName;

    private String mSubject;

    private String mTextContent;
    private String mHtmlContent;

    private LinkedHashMap<String, File> mAttachments;

    public EMail() {

        mTos = new LinkedHashMap<>();
        mCCs = new LinkedList<>();
        mBCCs = new LinkedList<>();
        mAttachments = new LinkedHashMap<>();
    }

    public EMail addTo(@NonNull String toName, @NonNull String toEmail) {
        mTos.put(toEmail, toName);
        return this;
    }

    public EMail addCC(@NonNull String ccEmail) {
        mCCs.add(ccEmail);
        return this;
    }

    public EMail addBCCs(@NonNull String bccEmail) {
        mBCCs.add(bccEmail);
        return this;
    }

    public EMail addAttachment(@NonNull String attachmentName, @NonNull File attachmentFile) {
        mAttachments.put(attachmentName, attachmentFile);
        return this;
    }

    public EMail setFrom(@NonNull String fromName, @NonNull String fromEmail) {
        mFrom = fromEmail;
        mFromName = fromName;
        return this;
    }

    public EMail setReplyTo(@NonNull String replyToName, @NonNull String replyToEmail) {
        mReplyTo = replyToEmail;
        mReplyToName = replyToName;
        return this;
    }

    public EMail setSubject(@NonNull String subject) {
        mSubject = subject;
        return this;
    }

    public EMail setTextContent(@NonNull String textContent) {
        mTextContent = textContent;
        return this;
    }

    public EMail setHtmlContent(@NonNull String htmlContent) {
        mHtmlContent = htmlContent;
        return this;
    }

    String getSubject() {
        return mSubject;
    }

    LinkedHashMap<String, String> getTos() {
        return mTos;
    }

    LinkedList<String> getCCs() {
        return mCCs;
    }

    LinkedList<String> getBCCs() {
        return mBCCs;
    }

    String getFrom() {
        return mFrom;
    }

    String getFromName() {
        return mFromName;
    }

    String getReplyTo() {
        return mReplyTo;
    }

    String getReplyToName() {
        return mReplyToName;
    }

    LinkedHashMap<String, File> getAttachments() {
        return mAttachments;
    }

    String getTextContent() {
        return mTextContent;
    }

    String getHtmlContent() {
        return mHtmlContent;
    }
}
