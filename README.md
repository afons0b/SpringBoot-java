# SpringBoot-java
salvando estudos do java, uma forma de acompanhar minha propria evolução de conhecimento no framework, no futuro pretendo fazer um "relatorio" de tudo o que foi estudado


![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-UI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

API RESTful para gerenciamento de usuários e integração com serviços externos (BrasilAPI), desenvolvida com foco em boas práticas de engenharia de software, segurança e testes automatizados.

---

## Tecnologias Utilizadas

O projeto utiliza o que há de mais moderno no ecossistema **Java**:

* **Core:** Java 21, Spring Boot 3.2, Spring Data JPA.
* **Segurança:** Spring Security 6 (Auth Basic, CSRF Protection, RBAC).
* **Banco de Dados:** MySQL 9.0 (Containerizado).
* **Testes:** JUnit 5, Mockito, RestAssured, **Testcontainers** (MySQL isolado) e **WireMock** (Simulação de APIs externas).
* **DevOps:** Docker Compose e Google Jib (Criação de imagens otimizadas).
* **Documentação:** SpringDoc OpenAPI (Swagger UI).

---

##  Documentação da API (Swagger)

A aplicação possui documentação interativa. Após rodar o projeto, acesse:

👉 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

Lá você poderá testar todos os endpoints (`GET`, `POST`, etc.) diretamente pelo navegador.

> **Nota:** Para endpoints protegidos, utilize as credenciais de usuário configuradas (ex: admin/admin) ou verifique os logs de inicialização.

---

## ⚡ Como Rodar o Projeto

Você precisa apenas de **Docker** e **Java 21** instalados.

### Passo 1: Clonar o repositório
```bash
git clone [https://github.com/afonsob/SpringBoot-java.git](https://github.com/afonsob/SpringBoot-java.git)
cd SpringBoot-java

# Opção A: Se você já tem o Maven instalado
mvn clean compile jib:dockerBuild
docker compose up -d

# Opção B: Apenas rodar o banco (se quiser rodar a app na IDE)
docker compose up -d mysql

A aplicação estará rodando em: http://localhost:8080

Para executar a suíte de testes (incluindo os testes de integração com Testcontainers e WireMock): mvn test