package io.micronaut.starter.feature.messaging.kafka

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom

class KafkaStreamsSpec extends BeanContextSpec {

    void "test kafka-streams features"() {
        when:
        Features features = getFeatures(['kafka-streams'])

        then:
        features.contains("kafka")
        features.contains("kafka-streams")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(["kafka-streams"])).render().toString()

        then:
        template.contains('implementation("io.micronaut.kafka:micronaut-kafka")')
        template.contains('implementation("io.micronaut.kafka:micronaut-kafka-streams")')
    }

    void "test dependencies are present for maven"() {
        when:
        String template = pom.template(buildProject(), getFeatures(["kafka-streams"]), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.kafka</groupId>
      <artifactId>micronaut-kafka</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.kafka</groupId>
      <artifactId>micronaut-kafka-streams</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['kafka-streams'])

        then:
        ctx.configuration.containsKey('kafka.bootstrap.servers')
    }
}
