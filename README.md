# E-commerce SOA demo

Three autonomous Spring Boot 3.3 services demonstrate REST catalogue/order APIs and a contract-first SOAP payment API.

## Prerequisites and build

Install JDK 17 and Maven. From each service directory run `mvn clean test`; the JAXB classes are generated from `src/main/resources/xsd/payment.xsd`. On Windows run `scripts\start-all.bat`, or start the three services in separate terminals in this order:

```text
cd product-service && mvn spring-boot:run
cd payment-service && mvn spring-boot:run
cd order-service && mvn spring-boot:run
```

Linux/macOS: `chmod +x scripts/start-all.sh && ./scripts/start-all.sh`.

URLs: product Swagger `http://localhost:8001/swagger-ui.html`, order Swagger `http://localhost:8000/swagger-ui.html`, H2 consoles at `/h2-console` for each port, and WSDL at `http://localhost:8003/ws/payment.wsdl`.

## Happy path

```bash
curl -X POST http://localhost:8000/orders -H "Content-Type: application/json" -d "{\"customerName\":\"Asha\",\"items\":[{\"productId\":1,\"quantity\":2},{\"productId\":4,\"quantity\":1}],\"payment\":{\"method\":\"CARD\",\"cardNumber\":\"4111111111111111\"}}"
```

Expected HTTP `201` with an order whose status is `CONFIRMED`, a generated `ORD-XXXXXXXX`, and a `TXN-XXXXXXXXXX`; the card number is never returned.

SOAP examples use `soap-requests/*.xml`, for example:
`curl -H "Content-Type: text/xml; charset=utf-8" --data-binary "@soap-requests/process-payment-approved.xml" http://localhost:8003/ws`

## Troubleshooting

If a port is occupied, stop the process or change that service's `server.port`. If JAXB generation fails, confirm Maven can reach Maven Central and use Java 17. A declined card ends in `0000`; it returns 402 and restores reserved stock. Product unavailability returns 503; SOAP faults return 502.
