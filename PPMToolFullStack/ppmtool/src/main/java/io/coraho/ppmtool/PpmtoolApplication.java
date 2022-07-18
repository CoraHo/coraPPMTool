package io.coraho.ppmtool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import javax.servlet.annotation.WebServlet;

@SpringBootApplication
public class PpmtoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(PpmtoolApplication.class, args);
    }

}
