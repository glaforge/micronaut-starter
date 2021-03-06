package io.micronaut.starter.cli.feature.server.controller

import io.micronaut.context.BeanContext
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.CommandFixture
import io.micronaut.starter.cli.CommandSpec
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Unroll

class CreateControllerSpec extends CommandSpec implements CommandFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void "test creating a controller and running the test for #language.getName() and #testFramework.getName()"() {
        generateDefaultProject(new Options(language, testFramework, BuildTool.GRADLE))
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateControllerCommand command = new CreateControllerCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput, [])
        command.controllerName = "Greeting"

        when:
        Integer exitCode = command.call()

        then:
        exitCode == 0
        1 * consoleOutput.out({ it.contains("Rendered controller") })
        1 * consoleOutput.out({ it.contains("Rendered test") })

        when:
        executeGradleCommand("test")

        then:
        testOutputContains("BUILD SUCCESSFUL")

        where:
        language        | testFramework
        Language.JAVA   | TestFramework.JUNIT
        Language.GROOVY | TestFramework.JUNIT
        Language.KOTLIN | TestFramework.JUNIT
        Language.GROOVY | TestFramework.SPOCK
        Language.KOTLIN | TestFramework.KOTLINTEST
    }
}
