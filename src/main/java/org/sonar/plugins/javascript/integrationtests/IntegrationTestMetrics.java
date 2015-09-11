package org.sonar.plugins.javascript.integrationtests;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

public final class IntegrationTestMetrics implements Metrics {
    public static final String IT_TESTS_KEY = "it_tests";
    public static final Metric<Integer> TESTS = new Metric.Builder(IT_TESTS_KEY, "Integration tests", Metric.ValueType.INT)
            .setDescription("Number of integration tests")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(CoreMetrics.DOMAIN_INTEGRATION_TESTS)
            .create();

    public static final String IT_TEST_EXECUTION_TIME_KEY = "it_test_execution_time";
    public static final Metric<Long> TEST_EXECUTION_TIME = new Metric.Builder(IT_TEST_EXECUTION_TIME_KEY, "Integration tests duration", Metric.ValueType.MILLISEC)
            .setDescription("Execution duration of unit tests")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(CoreMetrics.DOMAIN_INTEGRATION_TESTS)
            .create();

    public static final String IT_TEST_SUCCESS_DENSITY_KEY = "it_test_success_density";
    public static final Metric<Double> TEST_SUCCESS_DENSITY = new Metric.Builder(IT_TEST_SUCCESS_DENSITY_KEY, "Integration test success (%)", Metric.ValueType.PERCENT)
            .setDescription("Density of successful integration tests")
            .setDirection(Metric.DIRECTION_BETTER)
            .setQualitative(true)
            .setDomain(CoreMetrics.DOMAIN_INTEGRATION_TESTS)
            .setWorstValue(0.0)
            .setBestValue(100.0)
            .setOptimizedBestValue(true)
            .create();

    public static final String IT_TEST_FAILURES_KEY = "it_test_failures";
    public static final Metric<Integer> TEST_FAILURES = new Metric.Builder(IT_TEST_FAILURES_KEY, "Integration test failures", Metric.ValueType.INT)
            .setDescription("Number of integration test failures")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(true)
            .setDomain(CoreMetrics.DOMAIN_INTEGRATION_TESTS)
            .setBestValue(0.0)
            .setOptimizedBestValue(true)
            .create();

    public static final String IT_TEST_ERRORS_KEY = "it_test_errors";
    public static final Metric<Integer> TEST_ERRORS = new Metric.Builder(IT_TEST_ERRORS_KEY, "Integration test errors", Metric.ValueType.INT)
            .setDescription("Number of integration test errors")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(true)
            .setDomain(CoreMetrics.DOMAIN_INTEGRATION_TESTS)
            .setBestValue(0.0)
            .setOptimizedBestValue(true)
            .create();

    public static final String IT_SKIPPED_TESTS_KEY = "it_skipped_tests";
    public static final Metric<Integer> SKIPPED_TESTS = new Metric.Builder(IT_SKIPPED_TESTS_KEY, "Skipped integration tests", Metric.ValueType.INT)
            .setDescription("Number of skipped integration tests")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(true)
            .setDomain(CoreMetrics.DOMAIN_INTEGRATION_TESTS)
            .setBestValue(0.0)
            .setOptimizedBestValue(true)
            .create();

    @Override
    public List<Metric> getMetrics() {
        return Arrays.<Metric>asList(TESTS, TEST_SUCCESS_DENSITY, TEST_FAILURES, TEST_ERRORS, SKIPPED_TESTS, TEST_EXECUTION_TIME);
    }
}