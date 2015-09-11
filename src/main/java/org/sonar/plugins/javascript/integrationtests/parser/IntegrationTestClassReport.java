package org.sonar.plugins.javascript.integrationtests.parser;

import com.google.common.collect.Lists;

import java.util.List;

public class IntegrationTestClassReport {
    private long errors = 0L;
    private long failures = 0L;
    private long skipped = 0L;
    private long tests = 0L;
    private long durationMilliseconds = 0L;

    private List<IntegrationTestResult> results = null;

    public IntegrationTestClassReport add(IntegrationTestResult result) {
        if (results == null) {
            results = Lists.newArrayList();
        }
        results.add(result);
        if (result.getStatus().equals(IntegrationTestResult.STATUS_SKIPPED)) {
            skipped += 1;

        } else if (result.getStatus().equals(IntegrationTestResult.STATUS_FAILURE)) {
            failures += 1;

        } else if (result.getStatus().equals(IntegrationTestResult.STATUS_ERROR)) {
            errors += 1;
        }
        tests += 1;
        durationMilliseconds += result.getDurationMilliseconds();
        return this;
    }

    public long getErrors() {
        return errors;
    }

    public long getFailures() {
        return failures;
    }

    public long getSkipped() {
        return skipped;
    }

    public long getTests() {
        return tests;
    }

    public long getDurationMilliseconds() {
        return durationMilliseconds;
    }
}