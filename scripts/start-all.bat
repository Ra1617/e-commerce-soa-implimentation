@echo off
start "product-service" cmd /k "cd /d %~dp0..\product-service && mvn spring-boot:run"
timeout /t 3 /nobreak >nul
start "payment-service" cmd /k "cd /d %~dp0..\payment-service && mvn spring-boot:run"
timeout /t 3 /nobreak >nul
start "order-service" cmd /k "cd /d %~dp0..\order-service && mvn spring-boot:run"
