package net.chrzastek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Main {

  public static final String LOCALHOST = "http://localhost:3000";

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/users").allowedOrigins(LOCALHOST);
        registry.addMapping("/login").allowedOrigins(LOCALHOST);
        registry.addMapping("/cars").allowedOrigins(LOCALHOST);
        registry.addMapping("/carsnouser").allowedOrigins(LOCALHOST);
      }
    };
  }
}
