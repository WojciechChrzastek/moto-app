package net.chrzastek.moto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Main {

  public static final String MOTO_FRONT1 = "http://localhost:3000";
  public static final String MOTO_FRONT2 = "http://localhost:4000";

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/users").allowedOrigins(MOTO_FRONT1, MOTO_FRONT2);
        registry.addMapping("/login").allowedOrigins(MOTO_FRONT1, MOTO_FRONT2);
        registry.addMapping("/cars").allowedOrigins(MOTO_FRONT1, MOTO_FRONT2);
        registry.addMapping("/carsnouser").allowedOrigins(MOTO_FRONT1, MOTO_FRONT2);
      }
    };
  }
}
