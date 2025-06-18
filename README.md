# 📦 Controle de Estoque — Backend

Este é o projeto **backend** do sistema **Controle de Estoque**, desenvolvido com **Spring Boot**, **H2 Database** e
documentação automática com **Swagger (OpenAPI)**.

---

## 🚀 Tecnologias

- ✅ **Spring Boot**
- ✅ **Spring Data JPA**
- ✅ **H2 Database**
- ✅ **Bean Validation**
- ✅ **Springdoc OpenAPI 2.5.0**
- ✅ **Lombok**

---

## ⚙️ Como rodar localmente

### 1️⃣ Clone o repositório

```bash
git clone https://github.com/seu-usuario/seu-repo.git
cd seu-repo
```

### 2️⃣ Compile o projeto

```bash
mvn clean install
```

### 3️⃣ Rode a aplicação

```bash
mvn spring-boot:run
```

---

## 🗂️ Acesso ao banco de dados H2

- **Console H2:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
    - JDBC URL: `jdbc:h2:mem:testdb`
    - Username: `sa`
    - Password: *(em branco)*

---

## 📑 Documentação da API (Swagger)

- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## ✅ Endpoints principais

### 📌 Produtos

| Método   | Endpoint                              | Descrição                      |
|----------|---------------------------------------|--------------------------------|
| `POST`   | `/products`                           | Cadastrar novo produto         |
| `GET`    | `/products`                           | Listar todos os produtos       |
| `PUT`    | `/products/{id}`                      | Atualizar um produto           |
| `DELETE` | `/products/{id}`                      | Remover um produto             |
| `GET`    | `/products/type/{productType}`        | Listar produtos por tipo       |
| `GET`    | `/products/type/{productType}/report` | Relatório de produtos por tipo |
| `GET`    | `/products/profit/{productId}`        | Consultar lucro de um produto  |

### 📌 Movimentações de Estoque

| Método | Endpoint     | Descrição                                             |
|--------|--------------|-------------------------------------------------------|
| `POST` | `/movements` | Registrar movimentação de entrada ou saída de estoque |

---

## 📝 Como testar com Postman

1️⃣ Use o **Swagger UI** para ver o JSON de exemplo de cada rota.

2️⃣ Ou importe o endpoint manualmente no Postman:

- `POST /products` para criar um produto
- `POST /movements` para registrar uma movimentação
- `GET /products` para listar tudo


