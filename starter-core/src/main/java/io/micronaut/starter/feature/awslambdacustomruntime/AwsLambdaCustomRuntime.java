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
package io.micronaut.starter.feature.awslambdacustomruntime;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class AwsLambdaCustomRuntime implements Feature {

    @Override
    public String getName() {
        return "aws-lambda-custom-runtime";
    }

    @Override
    public String getTitle() {
        return "Custom AWS Lambda runtime";
    }

    @Override
    public String getDescription() {
        return "Adds support for deploying a Micronaut Function to a Custom AWS Lambda Runtime";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return (applicationType == ApplicationType.FUNCTION ||
                applicationType == ApplicationType.DEFAULT);
    }
}
