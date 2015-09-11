package org.sonar.plugins.javascript.integrationtests.parser;

import com.google.common.collect.Maps;

import java.util.Map;

public class IntegrationTestIndex {

    private Map<String, IntegrationTestClassReport> indexByClassname;

    public IntegrationTestIndex() {
        this.indexByClassname = Maps.newHashMap();
    }

    public IntegrationTestClassReport index(String classname) {
        IntegrationTestClassReport classReport = indexByClassname.get(classname);
        if (classReport == null) {
            classReport = new IntegrationTestClassReport();
            indexByClassname.put(classname, classReport);
        }
        return classReport;
    }

    public Map<String, IntegrationTestClassReport> getIndexByClassname() {
        return indexByClassname;
    }
}