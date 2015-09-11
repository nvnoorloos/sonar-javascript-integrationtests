package org.sonar.plugins.javascript.integrationtests.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;
import org.sonar.api.utils.StaxParser;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FilenameFilter;

public class SurefireReportParser {
    private static final Logger LOG = LoggerFactory.getLogger(SurefireReportParser.class);

    private static final String REPORT_PREFIX = "ITESTS-";
    private static final String REPORT_EXTENSION = ".xml";

    public IntegrationTestIndex parseReports(File reportDirectory) {
        if(reportDirectory != null && reportDirectory.isDirectory()) {
            File[] resultFiles = matchFiles(reportDirectory, REPORT_PREFIX, REPORT_EXTENSION);
            if (resultFiles.length > 0) {
                IntegrationTestIndex integrationTestIndex = new IntegrationTestIndex();
                parseResults(resultFiles, integrationTestIndex);

                return integrationTestIndex;
            } else {
                LOG.warn("No integration test information will be parsed, no reports found at: " + reportDirectory.getAbsolutePath());
            }
        }
        return null;
    }

    private void parseResults(File[] reports, IntegrationTestIndex integrationTestIndex) {
        SurefireStaxHandler staxParser = new SurefireStaxHandler(integrationTestIndex);
        StaxParser parser = new StaxParser(staxParser, false);
        for (File report : reports) {
            try {
                parser.parse(report);
            } catch (XMLStreamException e) {
                throw new SonarException("Fail to parse the integration test report: " + report, e);
            }
        }
    }

    private File[] matchFiles(File directory, final String prefix, final String extension) {
        return directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(prefix) && name.endsWith(extension);
            }
        });
    }
}