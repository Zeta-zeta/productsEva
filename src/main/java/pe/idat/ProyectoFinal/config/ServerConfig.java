package pe.idat.ProyectoFinal.config;


import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import pe.idat.ProyectoFinal.controller.ProductController;
import pe.idat.ProyectoFinal.security.BasicAuthFilter;
import pe.idat.ProyectoFinal.security.apiKeyAuthFilter;

@Configuration
public class ServerConfig extends ResourceConfig {
    public ServerConfig() {
        //register(apiKeyAuthFilter.class);
        register(BasicAuthFilter.class);

        register(ProductController.class);
    }
}
