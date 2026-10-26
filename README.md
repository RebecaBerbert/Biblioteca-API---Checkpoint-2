# Biblioteca API

API REST em **Java 17 + Spring Boot 3** com persistência em **SQL Server** via **Spring Data JPA**.
Permite cadastrar, consultar, alterar e excluir **autores** e **livros** (cada livro pertence a um autor).

**Checkpoint 2 — Microservices and Web Engineering**

| Nome | RM |
|------|----|
| _Rebeca Berbert Barcelos_ | _RM556683_ |
| _Julia Mendes_ | _RM557645_ |

---

## Tecnologias

- Java 17
- Spring Boot 3.3 (Web, Data JPA, Validation)
- Microsoft SQL Server (driver `mssql-jdbc`)
- Hibernate (ORM)
- Springdoc OpenAPI (Swagger UI)
- Maven

## Estrutura do projeto

```
biblioteca-api
├── database/script.sql            # cria o banco, as tabelas e insere dados de exemplo
├── docker-compose.yml             # (opcional) SQL Server local em container
├── pom.xml
└── src/main
    ├── java/br/com/fiap/biblioteca
    │   ├── BibliotecaApipplication.java
    │   ├── controller/   # endpoints REST (AutorController, LivroController, StatusController)
    │   ├── dto/          # dados de entrada com validação (AutorRequest, LivroRequest)
    │   ├── exception/    # exceções e tratamento global de erros
    │   ├── model/        # entidades JPA (Autor -> TB_AUTOR, Livro -> TB_LIVRO)
    │   ├── repository/   # interfaces Spring Data JPA
    │   └── service/      # regras de negócio
    └── resources/application.properties   # configuração da conexão
```

## Modelo de dados

```
TB_AUTOR                         TB_LIVRO
-----------------------          ---------------------------
ID_AUTOR (PK, identity)  <──┐    ID_LIVRO (PK, identity)
NOME                        │    TITULO
NACIONALIDADE               │    ISBN (único)
DATA_NASCIMENTO             │    ANO_PUBLICACAO
                            │    PRECO
                            └──  ID_AUTOR (FK)
```

---

## 1. Pré-requisitos

- **JDK 17** ou superior
- **Maven 3.9+** (ou uma IDE como IntelliJ / Eclipse / VS Code, que já traz o Maven)
- Acesso a um **SQL Server** (local, em Docker ou remoto)

## 2. Configurar o SQL Server

### Opção A — SQL Server local com Docker (mais rápido)

```bash
docker compose up -d
```

Isso sobe um SQL Server 2022 em `localhost:1433` com:

| Campo   | Valor             |
|---------|-------------------|
| Host    | `localhost`       |
| Porta   | `1433`            |
| Usuário | `sa`              |
| Senha   | `Biblioteca@2026` |

### Opção B — SQL Server já existente (local ou remoto / da faculdade)

Use o servidor, usuário e senha que você já tem e siga o passo 3.

## 3. Criar o banco de dados

O Hibernate cria as **tabelas** automaticamente, mas o **banco** `biblioteca` precisa existir.
Execute o arquivo [`database/script.sql`](database/script.sql) no SSMS, Azure Data Studio ou sqlcmd.
Ele cria o banco, as tabelas e insere dados de exemplo.

Com Docker, dá para rodar direto pelo terminal:

```bash
docker cp database/script.sql biblioteca-sqlserver:/tmp/script.sql
docker exec -it biblioteca-sqlserver /opt/mssql-tools18/bin/sqlcmd \
  -S localhost -U sa -P "Biblioteca@2026" -C -i /tmp/script.sql
```

## 4. Configurar a conexão na aplicação

A conexão fica em `src/main/resources/application.properties`:

```properties
spring.datasource.url=${DB_URL:jdbc:sqlserver://localhost:1433;databaseName=biblioteca;encrypt=true;trustServerCertificate=true}
spring.datasource.username=${DB_USERNAME:sa}
spring.datasource.password=${DB_PASSWORD:Biblioteca@2026}
```

Se você usar o Docker do passo 2, **não precisa mudar nada**.
Para outro servidor, altere os valores no arquivo **ou** defina variáveis de ambiente (assim a senha não vai para o GitHub):

**Linux / macOS**
```bash
export DB_URL="jdbc:sqlserver://SEU_SERVIDOR:1433;databaseName=biblioteca;encrypt=true;trustServerCertificate=true"
export DB_USERNAME="seu_usuario"
export DB_PASSWORD="sua_senha"
```

**Windows (PowerShell)**
```powershell
$env:DB_URL="jdbc:sqlserver://SEU_SERVIDOR:1433;databaseName=biblioteca;encrypt=true;trustServerCertificate=true"
$env:DB_USERNAME="seu_usuario"
$env:DB_PASSWORD="sua_senha"
```

> Se o banco tiver outro nome, troque `databaseName=biblioteca` na URL.

## 5. Executar a aplicação

```bash
mvn spring-boot:run
```

Ou pela IDE: abra o projeto e execute a classe `BibliotecaApiApplication`.

Ou gerando o `.jar`:

```bash
mvn clean package
java -jar target/biblioteca-api-1.0.0.jar
```

A API sobe em **http://localhost:8080**. No console aparecem os comandos SQL executados no SQL Server (`spring.jpa.show-sql=true`).

---

## 6. Endpoints

**Swagger UI (testar pelo navegador):** http://localhost:8080/swagger-ui.html

### Status / conexão com o banco

| Método | Endpoint      | Descrição |
|--------|---------------|-----------|
| GET    | `/api/status` | Mostra o banco conectado, a versão do SQL Server e o total de registros |

### Autores

| Método | Endpoint                    | Descrição |
|--------|-----------------------------|-----------|
| GET    | `/api/autores`              | Lista todos os autores |
| GET    | `/api/autores?nome=machado` | Busca autores pelo nome |
| GET    | `/api/autores/{id}`         | Busca um autor pelo id |
| POST   | `/api/autores`              | Cadastra um autor |
| PUT    | `/api/autores/{id}`         | Altera um autor |
| DELETE | `/api/autores/{id}`         | Exclui um autor (somente se não tiver livros) |

### Livros

| Método | Endpoint                  | Descrição |
|--------|---------------------------|-----------|
| GET    | `/api/livros`             | Lista todos os livros |
| GET    | `/api/livros?titulo=dom`  | Busca livros pelo título |
| GET    | `/api/livros?autorId=1`   | Lista os livros de um autor |
| GET    | `/api/livros/{id}`        | Busca um livro pelo id |
| POST   | `/api/livros`             | Cadastra um livro |
| PUT    | `/api/livros/{id}`        | Altera um livro |
| DELETE | `/api/livros/{id}`        | Exclui um livro |

### Códigos de resposta

| Código | Quando |
|--------|--------|
| 200 OK | Consulta ou alteração com sucesso |
| 201 Created | Cadastro com sucesso |
| 204 No Content | Exclusão com sucesso |
| 400 Bad Request | Dados inválidos (ex.: nome vazio) |
| 404 Not Found | Id não encontrado |
| 409 Conflict | ISBN repetido ou autor com livros na exclusão |

---

## 7. Roteiro de teste (curl)

> No Windows, use o Swagger UI, Postman/Insomnia ou o Git Bash para rodar os comandos abaixo.

**1. Verificar a conexão com o SQL Server**
```bash
curl http://localhost:8080/api/status
```

**2. Cadastrar um autor**
```bash
curl -X POST http://localhost:8080/api/autores \
  -H "Content-Type: application/json" \
  -d '{"nome":"Cecília Meireles","nacionalidade":"Brasileira","dataNascimento":"1901-11-07"}'
```

**3. Listar autores**
```bash
curl http://localhost:8080/api/autores
```

**4. Cadastrar um livro** (use o `id` do autor retornado no passo 2)
```bash
curl -X POST http://localhost:8080/api/livros \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Romanceiro da Inconfidência","isbn":"978-8526023455","anoPublicacao":1953,"preco":49.90,"autorId":4}'
```

**5. Consultar livros**
```bash
curl http://localhost:8080/api/livros
curl http://localhost:8080/api/livros/1
curl "http://localhost:8080/api/livros?autorId=1"
```

**6. Alterar um livro**
```bash
curl -X PUT http://localhost:8080/api/livros/1 \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Dom Casmurro (Edição Comentada)","isbn":"978-8535910663","anoPublicacao":1899,"preco":54.90,"autorId":1}'
```

**7. Excluir um livro**
```bash
curl -X DELETE http://localhost:8080/api/livros/1
```

**8. Conferir direto no banco**
```sql
USE biblioteca;
SELECT * FROM TB_AUTOR;
SELECT * FROM TB_LIVRO;
```

### Exemplo de resposta (GET `/api/livros/2`)

```json
{
  "id": 2,
  "titulo": "Memórias Póstumas de Brás Cubas",
  "isbn": "978-8535910664",
  "anoPublicacao": 1881,
  "preco": 34.90,
  "autor": {
    "id": 1,
    "nome": "Machado de Assis",
    "nacionalidade": "Brasileira",
    "dataNascimento": "1839-06-21"
  }
}
```

---

## Problemas comuns

| Erro | Solução |
|------|---------|
| `Cannot open database "biblioteca"` | O banco não existe: execute `database/script.sql` (passo 3) |
| `Login failed for user` | Usuário/senha errados em `application.properties` ou nas variáveis de ambiente |
| `The TCP/IP connection to the host ... has failed` | SQL Server desligado, porta 1433 bloqueada ou TCP/IP desabilitado no SQL Server Configuration Manager |
| Erro de certificado SSL | Confira se a URL tem `encrypt=true;trustServerCertificate=true` |
