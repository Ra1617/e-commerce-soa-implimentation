#!/usr/bin/env bash
set -e
(cd product-service && mvn spring-boot:run) &
sleep 3
(cd payment-service && mvn spring-boot:run) &
sleep 3
(cd order-service && mvn spring-boot:run) &
wait
