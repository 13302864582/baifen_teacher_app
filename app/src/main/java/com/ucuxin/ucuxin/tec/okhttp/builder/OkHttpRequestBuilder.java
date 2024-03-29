package com.ucuxin.ucuxin.tec.okhttp.builder;

import java.util.Map;

import com.ucuxin.ucuxin.tec.okhttp.request.RequestCall;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class OkHttpRequestBuilder
{
    protected String url;
    protected Object tag;
    protected Map<String, String> headers;
    protected Map<String, String> params;

    public abstract OkHttpRequestBuilder url(String url);

    public abstract OkHttpRequestBuilder tag(Object tag);

    public abstract OkHttpRequestBuilder headers(Map<String, String> headers);

    public abstract OkHttpRequestBuilder addHeader(String key, String val);

    public abstract RequestCall build();


}
