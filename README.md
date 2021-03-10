# url-shortening-service

**URL을 입력받아 짧게 줄여주고, Shortening된 URL을 입력하면 원래 URL로 리다이렉트하는 URL Shortening Service**


# 실행 방법
**※mvn 필요**

```
git clone https://github.com/sup2is/url-shortening-service.git
cd url-shortening-service
./mvn spring-boot:run
```

- [localhost](localhost) 접속

**h2-console (Database) 접속 방법**

- [localhost/h2-console](localhost/h2-console) 접속
- 접속 정보
  - Driver Class: `org.h2.Driver`
  - JDBC URL: `jdbc:h2:mem:test`
  - User Name : `sa`



