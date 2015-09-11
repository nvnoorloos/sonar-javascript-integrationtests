# sonar-javascript-integrationtests
> Extends SonarQube with integration tests results for javascript projects (tested with SonarQube 5.1.2).

## Usage
- Upload the plugin jar to the SonarQube 'extensions/plugins' folder.
- Use the sonar property 'sonar.javascript.jstestdriver.itReportsPath' to point to the directory containing surefire reports. The plugin will scan for reports prefixed with 'IUNIT-'
- Add the 'Integration Tests Results' widget to your dashboard (this widget also shows coverage information so you don't have to use two widgets for integration tests)

*The grunt-karma-sonar plugin (https://github.com/mdasberg/grunt-karma-sonar) supports the generation of reports that can be processed by this plugin.*

## Metrics
This plugin contains the following metrics that can be used for Quality Gates:
- Integration tests
- Integration tests duration
- Integration test success (%)
- Integration test failures
- Integration test errors
- Skipped integration tests

## Developing  
When developing the following command can be used to upload a new plugin version to a local SonarQube instance:
```shell
mvn clean install org.codehaus.sonar:sonar-dev-maven-plugin:1.8:upload -DsonarUrl=http://localhost:<sonarPort>/ -DsonarHome=<path/to/sonar/directory>
```

## Notes
Based on parts of the Sonar Javascript Plugin (http://docs.sonarqube.org/display/PLUG/JavaScript+Plugin)

## To-do
- Support for multiple languages
- Mark files with information about which part of the test failes