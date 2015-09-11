package org.sonar.plugins.javascript.integrationtests.ui;

import org.sonar.api.web.*;

@UserRole(UserRole.USER)
@Description("Widget that reports information about Integration Tests")
@WidgetCategory("Tests")
public class IntegrationTestResultWidget extends AbstractRubyTemplate implements RubyRailsWidget {
    @Override
    public String getId() {
        return "it_result_widget";
    }

    @Override
    public String getTitle() {
        return "Integration Tests Results";
    }

    @Override
    protected String getTemplatePath() {
        return "/widgets/it_result_widget.html.erb";
    }
}