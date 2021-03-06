/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.filewatch;

import com.fizzed.rocker.RockerModel;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.server.template.groovyController;
import io.micronaut.starter.feature.server.template.javaController;
import io.micronaut.starter.feature.server.template.kotlinController;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RockerTemplate;

/**
 * Abstract function implementation.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public abstract class AbstractFunctionFeature implements FunctionFeature {

    protected RockerModel javaControllerTemplate(Project project) {
        return javaController.template(project);
    }

    protected RockerModel kotlinControllerTemplate(Project project) {
        return kotlinController.template(project);
    }

    protected RockerModel groovyControllerTemplate(Project project) {
        return groovyController.template(project);
    }

    protected void applyFunction(GeneratorContext generatorContext, ApplicationType type) {
        BuildTool buildTool = generatorContext.getBuildTool();


        generatorContext.addTemplate("readme", new RockerTemplate(
                "README.md",
                readmeTemplate(generatorContext, generatorContext.getProject(), buildTool)));

        if (type == ApplicationType.DEFAULT) {

            Project project = generatorContext.getProject().withClassName("Hello");

            Language language = generatorContext.getLanguage();
            String sourceFile = generatorContext.getSourcePath("/{packagePath}/HelloController");

            switch (language) {
                case GROOVY:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            sourceFile,
                            groovyControllerTemplate(project)));
                    break;
                case KOTLIN:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            sourceFile,
                            kotlinControllerTemplate(project)));
                    break;
                case JAVA:
                default:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            sourceFile,
                            javaControllerTemplate(project)));
                    break;
            }

            applyTestTemplate(generatorContext, project);
        }
    }

    protected void applyTestTemplate(GeneratorContext generatorContext, Project project) {
        String testSource =  generatorContext.getTestSourcePath("/{packagePath}/HelloFunction");
        generatorContext.addTestTemplate("testFunction", testSource,
                javaJUnitTemplate(project),
                kotlinJUnitTemplate(project),
                groovyJUnitTemplate(project),
                kotlinTestTemplate(project),
                spockTemplate(project));
    }

    protected abstract RockerModel readmeTemplate(GeneratorContext generatorContext, Project project, BuildTool buildTool);

    protected abstract String getRunCommand(BuildTool buildTool);

    protected abstract String getBuildCommand(BuildTool buildTool);

    protected abstract RockerModel javaJUnitTemplate(Project project);

    protected abstract RockerModel kotlinJUnitTemplate(Project project);

    protected abstract RockerModel groovyJUnitTemplate(Project project);

    protected abstract RockerModel kotlinTestTemplate(Project project);

    public abstract RockerModel spockTemplate(Project project);
}
