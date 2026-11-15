package com.example.order.config;
import org.springframework.context.annotation.*; import org.springframework.web.client.RestClient; import org.springframework.ws.client.core.WebServiceTemplate; import org.springframework.oxm.jaxb.Jaxb2Marshaller;
/** Configures downstream clients from application settings rather than hard-coded workflow logic. */
@Configuration public class ClientConfig {
 @Bean RestClient productRestClient(org.springframework.core.env.Environment e){return RestClient.builder().baseUrl(e.getProperty("product.base-url","http://localhost:8001")).build();}
 @Bean Jaxb2Marshaller marshaller(){var m=new Jaxb2Marshaller();m.setPackagesToScan("com.example.order.soap");return m;}
 @Bean WebServiceTemplate webServiceTemplate(Jaxb2Marshaller m){var t=new WebServiceTemplate(m);return t;}
}
