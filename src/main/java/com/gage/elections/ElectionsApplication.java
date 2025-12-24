
package com.gage.elections;

import com.gage.elections.config.properties.ContributionProperties;
import com.gage.elections.config.properties.JudicialProperties;
import com.gage.elections.config.properties.TransparencyProperties;
import com.gage.elections.config.properties.TrustProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.text.Normalizer;

@EnableConfigurationProperties({
        JudicialProperties.class,
        TransparencyProperties.class,
        TrustProperties.class,
        ContributionProperties.class
})
@SpringBootApplication
public class ElectionsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElectionsApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner testNormalization() {
//        return args -> {
//            System.out.println("=== PRUEBA DE NORMALIZACIÓN ===");
//
//            System.out.println(normalize("Educación"));
//            System.out.println(normalize("corrupción"));
//            System.out.println(normalize("salúd pública"));
//            System.out.println(normalize("ÁÉÍÓÚ Ñ ñ ü"));
//            System.out.println(normalize("plan POLítIcO"));
//
//            System.out.println("=== FIN DE PRUEBA ===");
//        };
//    }
//
//    // 🔥 Método que deseas probar
//    public String normalize(String text) {
//        if (text == null) return null;
//        return Normalizer
//                .normalize(text, java.text.Normalizer.Form.NFD)
//                .replaceAll("\\p{M}", "") // elimina tildes
//                .toLowerCase();
//    }

}
