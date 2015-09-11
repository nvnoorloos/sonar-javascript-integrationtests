package org.sonar.plugins.javascript.integrationtests.sensors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.measures.Metric;
import org.sonar.api.utils.ParsingUtils;
import org.sonar.plugins.javascript.integrationtests.IntegrationTestMetrics;
import org.sonar.plugins.javascript.integrationtests.IntegrationTestPlugin;
import org.sonar.plugins.javascript.integrationtests.parser.IntegrationTestClassReport;
import org.sonar.plugins.javascript.integrationtests.parser.IntegrationTestIndex;
import org.sonar.plugins.javascript.integrationtests.parser.SurefireReportParser;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

public class JsIntegrationTestDriverSensor implements Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(JsIntegrationTestDriverSensor.class);

    private static final String JAVASCRIPT_LANGUAGE_KEY = "js";

    protected FileSystem fileSystem;
    protected Settings settings;
    private final FilePredicate mainFilePredicate;
    private final FilePredicate testFilePredicate;

    public JsIntegrationTestDriverSensor(FileSystem fileSystem, Settings settings) {
        this.fileSystem = fileSystem;
        this.settings = settings;

        this.mainFilePredicate = fileSystem.predicates().and(
                fileSystem.predicates().hasType(InputFile.Type.MAIN),
                fileSystem.predicates().hasLanguage(JAVASCRIPT_LANGUAGE_KEY));

        this.testFilePredicate = fileSystem.predicates().and(
                fileSystem.predicates().hasType(InputFile.Type.TEST),
                fileSystem.predicates().hasLanguage(JAVASCRIPT_LANGUAGE_KEY));
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return StringUtils.isNotBlank(getReportsDirectoryPath()) && fileSystem.hasFiles(mainFilePredicate);
    }

    @Override
    public void analyse(Project project, SensorContext context) {
        File reportsDirectory = getFile(getReportsDirectoryPath());

        LOG.info("Parsing integration test results in Surefire format from folder {}", reportsDirectory);

        SurefireReportParser parser = new SurefireReportParser();
        IntegrationTestIndex integrationTestIndex = parser.parseReports(reportsDirectory);
        if(integrationTestIndex != null) {
            saveResults(context, integrationTestIndex);
        }
    }

    private void saveResults(SensorContext context, IntegrationTestIndex integrationTestIndex) {
        long numberOfTests = 0;
        long skippedTests = 0;
        long errors = 0;
        long failures = 0;
        long totalDurationMilliseconds = 0;

        for (Map.Entry<String, IntegrationTestClassReport> entry : integrationTestIndex.getIndexByClassname().entrySet()) {
            IntegrationTestClassReport report = entry.getValue();
            if (report.getTests() > 0) {
                numberOfTests += report.getTests();
                skippedTests += report.getSkipped();
                errors += report.getErrors();
                failures += report.getFailures();
                totalDurationMilliseconds += report.getDurationMilliseconds();

                String testFileName = getTestFilename(entry.getKey());
                InputFile testInputFile = getTestFile(testFileName);
                if (testInputFile != null) {
                    saveMeasures(context,
                            context.getResource(testInputFile),
                            report.getTests(),
                            report.getSkipped(),
                            report.getErrors(),
                            report.getFailures(),
                            report.getDurationMilliseconds());
                } else {
                    LOG.warn("Specific test results will not be saved for test class \"{}\" because the corresponding file is not found", testFileName);
                }
            }
        }

        saveMeasures(context,
                null,
                numberOfTests,
                skippedTests,
                errors,
                failures,
                totalDurationMilliseconds);
    }

    private void saveMeasures(SensorContext context, Resource resource, long numberOfTests, long skippedTests, long errors, long failures, long totalDurationMilliseconds) {
        double executedTests = numberOfTests - skippedTests;
        saveMeasure(context, resource, IntegrationTestMetrics.SKIPPED_TESTS, skippedTests);
        saveMeasure(context, resource, IntegrationTestMetrics.TESTS, executedTests);
        saveMeasure(context, resource, IntegrationTestMetrics.TEST_ERRORS, errors);
        saveMeasure(context, resource, IntegrationTestMetrics.TEST_FAILURES, failures);
        saveMeasure(context, resource, IntegrationTestMetrics.TEST_EXECUTION_TIME, totalDurationMilliseconds);
        double passedTests = executedTests - errors - failures;
        if (executedTests > 0) {
            double percentage = passedTests * 100d / executedTests;
            saveMeasure(context, resource, IntegrationTestMetrics.TEST_SUCCESS_DENSITY, ParsingUtils.scaleValue(percentage));
        }
    }

    private void saveMeasure(SensorContext context, Resource resource, Metric metric, double value) {
        if (!Double.isNaN(value)) {
            if(resource != null) {
                context.saveMeasure(resource, metric, value);
            } else {
                context.saveMeasure(metric, value);
            }
        }
    }

    private String getTestFilename(String className) {
        String fileName = className.substring(className.indexOf('.') + 1);
        fileName = fileName.replace('.', File.separatorChar);
        fileName = fileName + ".js";
        return fileName;
    }

    private InputFile getTestFile(String fileName) {
        FilePredicate predicate = fileSystem.predicates().and(testFilePredicate, fileSystem.predicates().matchesPathPattern("**" + File.separatorChar + fileName));
        Iterator<InputFile> fileIterator = fileSystem.inputFiles(predicate).iterator();
        if (fileIterator.hasNext()) {
            return fileIterator.next();
        }
        return null;
    }

    private File getFile(String path) {
        File file = new File(path);
        if (!file.isAbsolute()) {
            file = new File(fileSystem.baseDir(), path);
        }
        return file;
    }

    private String getReportsDirectoryPath() {
        return settings.getString(IntegrationTestPlugin.JSTESTDRIVER_REPORTS_PATH);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}