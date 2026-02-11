package com.projects.openlearning;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class DocumentationTest {

    ApplicationModules modules = ApplicationModules.of(OpenLearningApplication.class);

    @Test
    void writeDocumentation() {
        new Documenter(modules)
                .writeDocumentation()
                .writeIndividualModulesAsPlantUml();
    }
}
