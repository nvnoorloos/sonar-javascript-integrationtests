package org.sonar.plugins.javascript.integrationtests;

import java.util.List;

import com.google.common.collect.ImmutableList;
import org.sonar.plugins.javascript.integrationtests.sensors.JsIntegrationTestDriverSensor;
import org.sonar.api.SonarPlugin;

import org.sonar.plugins.javascript.integrationtests.ui.IntegrationTestResultWidget;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

public final class IntegrationTestPlugin extends SonarPlugin {
    private static final String PROPERTY_PREFIX = "sonar.javascript";

    private static final String JSTESTDRIVER_REPORTS_PATH_DEFAULT_VALUE = "";
    public static final String JSTESTDRIVER_REPORTS_PATH = PROPERTY_PREFIX + ".jstestdriver.itReportsPath";

    @Override
    public List getExtensions() {
        return ImmutableList.of(
                PropertyDefinition.builder(IntegrationTestPlugin.JSTESTDRIVER_REPORTS_PATH)
                        .defaultValue(IntegrationTestPlugin.JSTESTDRIVER_REPORTS_PATH_DEFAULT_VALUE)
                        .name("JSIntegrationTestDriver output folder")
                        .description("Folder where JSIntegrationTestDriver test reports are located.")
                        .onQualifiers(Qualifiers.MODULE, Qualifiers.PROJECT)
                        .build(),

                // Metrics
                IntegrationTestMetrics.class,

                // Sensors
                JsIntegrationTestDriverSensor.class,

                // UI
                IntegrationTestResultWidget.class);
    }
}