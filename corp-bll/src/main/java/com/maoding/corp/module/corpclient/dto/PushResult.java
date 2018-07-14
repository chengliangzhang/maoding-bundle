package com.maoding.corp.module.corpclient.dto;

/**
 * Created by Wuwq on 2017/4/24.
 */
public class PushResult {
    private int ResultType;
    private String Message;
    private String LogMessage;
    private Object AppendData;

    public PushResult() {
    }

    public PushResult(int resultType) {
        this.ResultType = resultType;
    }

    public static PushResult failed() {
        return new PushResult(1);
    }

    public static PushResult success() {
        return new PushResult(0);
    }

    public int getResultType() {
        return ResultType;
    }

    public void setResultType(int resultType) {
        this.ResultType = resultType;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public String getLogMessage() {
        return LogMessage;
    }

    public void setLogMessage(String logMessage) {
        this.LogMessage = logMessage;
    }

    public boolean isSuccessful() {
        return this.ResultType == 0;
    }

    public Object getAppendData() {
        return AppendData;
    }

    public void setAppendData(Object appendData) {
        this.AppendData = appendData;
    }
}
