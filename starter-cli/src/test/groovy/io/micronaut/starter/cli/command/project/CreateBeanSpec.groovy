package io.micronaut.starter.cli.command.project

import io.micronaut.context.BeanContext
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.CommandFixture
import io.micronaut.starter.cli.CommandSpec
import io.micronaut.starter.cli.command.project.bean.CreateBeanCommand
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared

class CreateBeanSpec extends CommandSpec implements CommandFixture {

    @Shared @AutoCleanup BeanContext beanContext = BeanContext.run()

    void "test creating a bean - java"() {
        generateDefaultProject(Language.JAVA)
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateBeanCommand command = new CreateBeanCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.beanName = "Greeting"

        when:
        Integer exitCode = command.call()
        File bean = new File(dir, "src/main/java/example/micronaut/Greeting.java")

        then:
        exitCode == 0
        bean.exists()
        1 * consoleOutput.out({ it.contains("Rendered bean") })

        when:
        new File(dir, "src/main/java/example/micronaut/Application.java").write("""
package example.micronaut;

import io.micronaut.runtime.Micronaut;
import io.micronaut.context.ApplicationContext;

public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = Micronaut.run(Application.class);
        ctx.getBean(example.micronaut.Greeting.class);
    }
}
""")
        executeGradleCommand("run")

        then:
        testOutputContains("Startup completed")
    }

    void "test creating a bean - groovy"() {
        generateDefaultProject(Language.GROOVY)
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateBeanCommand command = new CreateBeanCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.beanName = "Greeting"

        when:
        Integer exitCode = command.call()
        File bean = new File(dir, "src/main/groovy/example/micronaut/Greeting.groovy")

        then:
        exitCode == 0
        bean.exists()
        1 * consoleOutput.out({ it.contains("Rendered bean") })

        when:
        new File(dir, "src/main/groovy/example/micronaut/Application.groovy").write("""
package example.micronaut

import io.micronaut.runtime.Micronaut
import io.micronaut.context.ApplicationContext

class Application {

    static void main(String[] args) {
        ApplicationContext ctx = Micronaut.run(Application)
        ctx.getBean(example.micronaut.Greeting)
    }
}
""")
        executeGradleCommand("run")

        then:
        testOutputContains("Startup completed")
    }

    void "test creating a bean - kotlin"() {
        generateDefaultProject(Language.KOTLIN)
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateBeanCommand command = new CreateBeanCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.beanName = "Greeting"

        when:
        Integer exitCode = command.call()
        File bean = new File(dir, "src/main/kotlin/example/micronaut/Greeting.kt")

        then:
        exitCode == 0
        bean.exists()
        1 * consoleOutput.out({ it.contains("Rendered bean") })

        when:
        new File(dir, "src/main/kotlin/example/micronaut/Application.kt").write("""
package example.micronaut

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        val ctx = Micronaut.build()
                .packages("example.micronaut")
                .mainClass(Application.javaClass)
                .start()
        ctx.getBean(example.micronaut.Greeting::class.java)
    }
}
""")
        executeGradleCommand("run")

        then:
        testOutputContains("Startup completed")
    }
}
