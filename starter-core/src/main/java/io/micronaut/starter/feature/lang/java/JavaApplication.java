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
package io.micronaut.starter.feature.lang.java;

import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Features;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;

@Singleton
public class JavaApplication implements JavaApplicationFeature {

    @Override
    public String mainClassName(Project project) {
        return project.getPackageName() + ".Application";
    }

    @Override
    public String getName() {
        return "application";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType != ApplicationType.CLI && applicationType != ApplicationType.FUNCTION;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        JavaApplicationFeature.super.apply(generatorContext);

        Features features = generatorContext.getFeatures();
        boolean hasFunction = features.getFeatures().stream().anyMatch(f -> f instanceof FunctionFeature);

        if (!hasFunction) {
            generatorContext.addTemplate("application", new RockerTemplate(getPath(),
                    application.template(generatorContext.getProject(), generatorContext.getFeatures())));
        }
    }

    protected String getPath() {
        return "src/main/java/{packagePath}/Application.java";
    }
}
