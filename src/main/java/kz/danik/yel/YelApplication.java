package kz.danik.yel;

import com.google.common.base.Strings;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

import kz.danik.yel.app.TonBot;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import javax.sql.DataSource;

@Push
@Theme(value = "yel")
@PWA(name = "Yel", shortName = "Yel")
@SpringBootApplication
public class YelApplication implements AppShellConfigurator {



    @Autowired
    private Environment environment;

    @Autowired
    private TonBot tonBot;

    public static void main(String[] args) {
        SpringApplication.run(YelApplication.class, args);


    }
    @Bean
    public CommandLineRunner init() {
        return args -> {
            String botToken = "7390627968:AAHhrjWDt2Itr7af6JegVfZF2gtxdVFUILE";

            // Using try-with-resources to allow autoclose to run upon finishing
            try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
                tonBot.initBot();
                botsApplication.registerBot(botToken, tonBot);
                
                System.out.println("MyAmazingBot successfully started!");
                // Ensure this process waits forever
                Thread.currentThread().join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource.hikari")
    DataSource dataSource(final DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @EventListener
    public void printApplicationUrl(final ApplicationStartedEvent event) {
        LoggerFactory.getLogger(YelApplication.class).info("Application started at "
                + "http://localhost:"
                + environment.getProperty("local.server.port")
                + Strings.nullToEmpty(environment.getProperty("server.servlet.context-path")));
    }
}
