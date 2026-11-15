package com.example.order.client;
import com.example.order.dto.*; import org.springframework.http.*; import org.springframework.stereotype.Component; import org.springframework.web.client.*;
/** HTTP client for the independent product service. */
@Component public class ProductClient {
 private final RestClient client; public ProductClient(RestClient productRestClient){client=productRestClient;}
 public ProductView get(long id){return client.get().uri("/products/{id}",id).retrieve().body(ProductView.class);}
 public void reserve(long id,int q){client.post().uri("/products/{id}/reserve",id).body(new QuantityRequest(q)).retrieve().toBodilessEntity();}
 public void release(long id,int q){client.post().uri("/products/{id}/release",id).body(new QuantityRequest(q)).retrieve().toBodilessEntity();}
}
