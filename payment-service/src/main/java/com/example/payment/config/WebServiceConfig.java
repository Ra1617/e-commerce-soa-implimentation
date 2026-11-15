package com.example.payment.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/** Publishes the contract-first WSDL and maps SOAP requests under /ws. */
@EnableWs @Configuration
public class WebServiceConfig {
  @Bean public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context){MessageDispatcherServlet s=new MessageDispatcherServlet();s.setApplicationContext(context);s.setTransformWsdlLocations(true);return new ServletRegistrationBean<>(s,"/ws/*");}
  @Bean public XsdSchema paymentSchema(){return new SimpleXsdSchema(new org.springframework.core.io.ClassPathResource("xsd/payment.xsd"));}
  @Bean(name="payment") public DefaultWsdl11Definition paymentWsdl(XsdSchema schema){DefaultWsdl11Definition d=new DefaultWsdl11Definition();d.setPortTypeName("PaymentPort");d.setServiceName("PaymentService");d.setLocationUri("/ws");d.setTargetNamespace("http://ecommerce.example.com/payment");d.setSchema(schema);return d;}
}
