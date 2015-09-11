package org.sonar.plugins.javascript.integrationtests.parser;

public class IntegrationTestResult {
    public static final String STATUS_OK = "ok";
    public static final String STATUS_ERROR = "error";
    public static final String STATUS_FAILURE = "failure";
    public static final String STATUS_SKIPPED = "skipped";

    private String name, status, stackTrace, message;
    private long durationMilliseconds = 0L;

    public String getName() {
        return name;
    }

    public IntegrationTestResult setName(String name) {
        this.name = name;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public IntegrationTestResult setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public IntegrationTestResult setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public IntegrationTestResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public long getDurationMilliseconds() {
        return durationMilliseconds;
    }

    public IntegrationTestResult setDurationMilliseconds(long l) {
        this.durationMilliseconds = l;
        return this;
    }

    public boolean isErrorOrFailure() {
        return STATUS_ERROR.equals(status) || STATUS_FAILURE.equals(status);
    }

    public boolean isError() {
        return STATUS_ERROR.equals(status);
    }
}