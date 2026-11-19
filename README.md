# Virdis Match Hub

Backend em arquitetura de microsserviços para o **Virdis Expande**, solução voltada à conexão entre proprietários de espaços urbanos ociosos e produtores interessados na implantação de hortas urbanas.

## Integrantes

- **Anna Julia Bobrzyk Gonzales** - RM: `557473`
- **Renan Lopes Martins de Vasconcelos** - RM: `555869`

## Problema

Áreas urbanas como terrenos, terraços e coberturas podem permanecer ociosas, mesmo apresentando potencial para cultivo de alimentos e implantação de hortas urbanas. Ao mesmo tempo, produtores interessados nesses espaços precisam localizar oportunidades, apresentar propostas e acompanhar o processo de avaliação.

## Solução

O Virdis Match Hub organiza esse fluxo em dois microsserviços:

- **ms-space:** gerencia anúncios de espaços urbanos, seus dados, disponibilidade e situação atual.
- **ms-project:** gerencia propostas de implantação e cultivo associadas aos espaços.

Quando uma proposta muda de estado, o `ms-project` se comunica de forma síncrona com o `ms-space` por OpenFeign. Dessa forma, o status do espaço acompanha o ciclo da proposta.

Exemplo do fluxo principal:

```text
Projeto: CREATED -> ANALYZING -> APPROVED
Espaço:  AVAILABLE -> UNDER_EVALUATION -> MATCHED
```

Também são contemplados os fluxos de rejeição e cancelamento:

```text
Projeto: ANALYZING -> REJECTED ou CANCELED
Espaço:  UNDER_EVALUATION -> AVAILABLE
```

## Arquitetura

```text
Cliente
  |
  v
API Gateway :8082
  |
  +-------------------------+
  |                         |
  v                         v
ms-space                 ms-project
porta aleatória          porta aleatória
  |                         |
  v                         v
virdis_space_db          virdis_project_db
          \               /
           \             /
             MySQL 8.4.6

Eureka Server :8761
  - API Gateway
  - ms-space
  - ms-project
```

### Componentes

- **Eureka Server:** registro e descoberta de serviços, porta `8761`.
- **API Gateway:** ponto de entrada da solução, porta `8082`.
- **ms-space:** CRUD de espaços e controle de status.
- **ms-project:** CRUD de propostas, comunicação síncrona, Circuit Breaker e fallback.
- **MySQL:** um banco lógico independente para cada microsserviço.
- **Flyway:** criação/versionamento das tabelas e carga inicial.

## Tecnologias

- Java 25
- Spring Boot 4.0.9
- Spring Cloud 2025.1.4
- Spring Cloud Netflix Eureka
- Spring Cloud Gateway
- Spring Cloud OpenFeign
- Spring Cloud LoadBalancer
- Resilience4j Circuit Breaker
- Spring Data JPA
- Bean Validation
- Springdoc OpenAPI / Swagger UI
- Flyway
- MySQL 8.4.6
- H2 para o profile `test`
- Docker e Docker Compose
- Maven
- Lombok

## Estrutura do monorepo

```text
virdis-match-hub/
├── api-gateway/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── eureka-server/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── ms-project/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── ms-space/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── compose.yaml
└── README.md
```

## Pré-requisitos

Para a execução recomendada, é necessário apenas:

- Git
- Docker Desktop com Docker Compose

O Maven não precisa estar instalado no computador para a execução com Docker. Os Dockerfiles são multi-stage e realizam o build dentro de uma imagem Maven com Java 25.

Antes de iniciar, confirme:

```bash
docker --version
docker compose version
```

## Execução completa com Docker Compose

### 1. Clonar o repositório

```bash
git clone https://github.com/Renanlmv/virdis-match-hub.git
cd virdis-match-hub
```

### 2. Validar o Compose

```bash
docker compose config
```

### 3. Construir e iniciar a solução

```bash
docker compose up -d --build
```

O comando cria ou inicia:

- `virdis-mysql`
- `virdis-eureka-server`
- `virdis-api-gateway`
- `virdis-ms-space`
- `virdis-ms-project`
- rede `virdis-net`
- volume nomeado `virdis_mysql_data`

A primeira execução pode levar alguns minutos, pois as imagens Maven e Java podem precisar ser baixadas e os projetos serão compilados.

### 4. Acompanhar o estado

```bash
docker compose ps
```

O MySQL deve aparecer como `healthy`. Os demais containers devem aparecer como `Up`.

Para acompanhar os logs:

```bash
docker compose logs -f
```

Para sair dos logs sem parar os containers, pressione `Ctrl + C`.

Logs de um serviço específico:

```bash
docker compose logs -f ms-space
docker compose logs -f ms-project
docker compose logs -f eureka-server
docker compose logs -f api-gateway
```

### 5. Confirmar o registro no Eureka

Abra:

- [Painel do Eureka Server](http://localhost:8761)

Devem aparecer as aplicações:

```text
API-GATEWAY
MS-SPACE
MS-PROJECT
```

Aguarde todas aparecerem como `UP` antes de testar as APIs.

## Endereços principais

### Infraestrutura

- [Eureka Server](http://localhost:8761)
- API Gateway: `http://localhost:8082`

### Swagger/OpenAPI pelo Gateway

- [Swagger do ms-space](http://localhost:8082/ms-space/swagger-ui/index.html)
- [OpenAPI JSON do ms-space](http://localhost:8082/ms-space/v3/api-docs)
- [Swagger do ms-project](http://localhost:8082/ms-project/swagger-ui/index.html)
- [OpenAPI JSON do ms-project](http://localhost:8082/ms-project/v3/api-docs)

## Endpoints

Todas as rotas de negócio são versionadas com `/api/v1`.

### ms-space

Base pelo Gateway:

```text
http://localhost:8082/ms-space/api/v1/spaces
```

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/ms-space/api/v1/spaces` | Lista os espaços |
| GET | `/ms-space/api/v1/spaces/{id}` | Busca espaço por ID |
| POST | `/ms-space/api/v1/spaces` | Cadastra um espaço |
| PUT | `/ms-space/api/v1/spaces/{id}` | Atualiza os dados do espaço |
| PUT | `/ms-space/api/v1/spaces/{id}/status` | Atualiza somente o status |
| DELETE | `/ms-space/api/v1/spaces/{id}` | Exclui um espaço |

#### Exemplo de cadastro de espaço

```json
{
  "ownerId": "user-004",
  "title": "Quintal para produção de ervas",
  "neighborhood": "Mooca",
  "type": "LAND",
  "areaSquareMeters": 120.50,
  "monthlyRent": 900.00,
  "description": "Quintal com ponto de água e acesso independente.",
  "imageUrl": "https://exemplo.com/imagens/quintal-mooca.png",
  "contactName": "Ana Martins",
  "contactEmail": "ana.martins@email.com",
  "residenceCount": 1,
  "city": "São Paulo",
  "state": "SP"
}
```

#### Tipos de espaço

```text
LAND
TERRACE
ROOFTOP
```

#### Status de espaço

```text
AVAILABLE
UNDER_EVALUATION
MATCHED
UNAVAILABLE
```

### ms-project

Base pelo Gateway:

```text
http://localhost:8082/ms-project/api/v1/projects
```

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/ms-project/api/v1/projects` | Lista as propostas |
| GET | `/ms-project/api/v1/projects/{id}` | Busca proposta por ID |
| POST | `/ms-project/api/v1/projects` | Cadastra uma proposta |
| PUT | `/ms-project/api/v1/projects/{id}` | Atualiza uma proposta `CREATED` |
| PATCH | `/ms-project/api/v1/projects/{id}/status` | Altera o status e sincroniza o espaço |
| DELETE | `/ms-project/api/v1/projects/{id}` | Exclui uma proposta permitida pela regra de negócio |

#### Exemplo de cadastro de proposta

```json
{
  "spaceId": 1,
  "producerName": "Bianca Fernandes",
  "producerEmail": "bianca.fernandes@email.com",
  "cultivationType": "VEGETABLES",
  "proposalDescription": "Implantação de horta orgânica com irrigação por gotejamento.",
  "estimatedCapacity": 420,
  "offeredMonthlyRent": 1850.00
}
```

#### Tipos de cultivo

```text
VEGETABLES
HERBS
FRUITS
MIXED
```

#### Status de proposta

```text
CREATED
ANALYZING
APPROVED
REJECTED
CANCELED
```

## Teste da comunicação síncrona

Os exemplos abaixo partem dos dados iniciais carregados pelo Flyway. Para garantir o estado inicial, veja a seção **Recriar o ambiente do zero**.

### 1. Listar espaços

```bash
curl http://localhost:8082/ms-space/api/v1/spaces
```

### 2. Criar uma proposta para o espaço 1

```bash
curl -i -X POST http://localhost:8082/ms-project/api/v1/projects \
  -H "Content-Type: application/json" \
  -d '{
    "spaceId": 1,
    "producerName": "Bianca Fernandes",
    "producerEmail": "bianca.fernandes@email.com",
    "cultivationType": "VEGETABLES",
    "proposalDescription": "Implantação de horta orgânica com irrigação por gotejamento.",
    "estimatedCapacity": 420,
    "offeredMonthlyRent": 1850.00
  }'
```

A resposta esperada é `201 Created`, com status `CREATED`. A URI do recurso criado é devolvida no cabeçalho `Location`.

Considere o ID retornado na resposta. Em um banco recém-criado pelos dados atuais, a nova proposta tende a receber ID `4`.

### 3. Iniciar a análise

```bash
curl -X PATCH http://localhost:8082/ms-project/api/v1/projects/4/status \
  -H "Content-Type: application/json" \
  -d '{"status":"ANALYZING"}'
```

Resultado esperado:

```text
Projeto 4: ANALYZING
Espaço 1: UNDER_EVALUATION
```

Confirmação:

```bash
curl http://localhost:8082/ms-project/api/v1/projects/4
curl http://localhost:8082/ms-space/api/v1/spaces/1
```

### 4. Aprovar a proposta

```bash
curl -X PATCH http://localhost:8082/ms-project/api/v1/projects/4/status \
  -H "Content-Type: application/json" \
  -d '{"status":"APPROVED"}'
```

Resultado esperado:

```text
Projeto 4: APPROVED
Espaço 1: MATCHED
```

### 5. Rejeitar uma proposta em análise

Nos dados iniciais, a proposta `3` está `ANALYZING` e o espaço `3` está `UNDER_EVALUATION`.

```bash
curl -X PATCH http://localhost:8082/ms-project/api/v1/projects/3/status \
  -H "Content-Type: application/json" \
  -d '{"status":"REJECTED"}'
```

Resultado esperado:

```text
Projeto 3: REJECTED
Espaço 3: AVAILABLE
```

## Regras de negócio principais

### Espaços

Transições permitidas:

```text
AVAILABLE -> UNDER_EVALUATION
AVAILABLE -> UNAVAILABLE
UNDER_EVALUATION -> MATCHED
UNDER_EVALUATION -> AVAILABLE
UNAVAILABLE -> AVAILABLE
```

### Propostas

Transições permitidas:

```text
CREATED -> ANALYZING
CREATED -> CANCELED
ANALYZING -> APPROVED
ANALYZING -> REJECTED
ANALYZING -> CANCELED
```

Regras adicionais:

- somente propostas `CREATED` podem ser atualizadas pelo `PUT`;
- propostas `ANALYZING` e `APPROVED` não podem ser excluídas;
- para iniciar uma análise, o espaço precisa estar `AVAILABLE`;
- ao aprovar uma proposta, o espaço passa para `MATCHED`;
- ao rejeitar ou cancelar uma proposta em análise, o espaço volta para `AVAILABLE`.

## Circuit Breaker e fallback

O `ms-project` protege as chamadas ao `ms-space` com Circuit Breaker e fallback.

Para demonstrar o fallback:

```bash
docker compose stop ms-space
```

Em seguida, tente cadastrar uma proposta válida por meio do `ms-project`. O retorno esperado é:

```text
503 Service Unavailable
```

Com mensagem informando que o serviço de espaços está temporariamente indisponível.

Para restaurar:

```bash
docker compose start ms-space
```

Aguarde o serviço aparecer como `UP` no Eureka antes de repetir o cadastro.

## Persistência e Flyway

Cada microsserviço utiliza um banco próprio:

```text
ms-space   -> virdis_space_db
ms-project -> virdis_project_db
```

Não há compartilhamento de tabelas. O `ms-project` mantém apenas o identificador lógico `spaceId` e consulta o `ms-space` por OpenFeign.

Cada serviço possui duas migrations:

```text
V1 -> criação/versionamento da tabela
V2 -> carga inicial de dados
```

O profile `dev` utiliza:

```properties
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
```

## Verificação no MySQL

Entre no cliente MySQL do container:

```bash
docker exec -it virdis-mysql mysql -uroot -p
```

Quando solicitado, informe a senha:

```text
root
```

Verifique o `ms-space`:

```sql
USE virdis_space_db;
SHOW TABLES;
SELECT installed_rank, version, description, script, success FROM flyway_schema_history ORDER BY installed_rank;
SELECT id, title, status FROM tb_spaces ORDER BY id;
```

Verifique o `ms-project`:

```sql
USE virdis_project_db;
SHOW TABLES;
SELECT installed_rank, version, description, script, success FROM flyway_schema_history ORDER BY installed_rank;
SELECT id, space_id, producer_name, status FROM tb_cultivation_projects ORDER BY id;
```

Saia com:

```sql
exit;
```

## Profiles

### Profile `test`

Usado para execução local com H2:

- banco H2 em memória;
- URL JDBC `jdbc:h2:mem:testdb`;
- console H2 habilitado;
- Flyway desabilitado;
- tabelas criadas pelo Hibernate;
- dados carregados por `import.sql`.

### Profile `dev`

Usado pelo Docker Compose:

- MySQL 8.4.6;
- banco independente por microsserviço;
- Flyway habilitado;
- Hibernate em modo `validate`;
- dados persistidos no volume nomeado `virdis_mysql_data`.

## Execução local pelo IntelliJ

Para executar com H2:

1. Inicie `EurekaServerApplication`.
2. Inicie `ApiGatewayApplication`.
3. Inicie `MsSpaceApplication` com profile `test`.
4. Inicie `MsProjectApplication` com profile `test`.
5. Aguarde os serviços aparecerem como `UP` no Eureka.

A ordem recomendada é:

```text
Eureka Server -> API Gateway -> ms-space -> ms-project
```

Os microsserviços usam `server.port=0`, portanto as portas são aleatórias.

Para acessar o H2 diretamente, consulte no log a porta atribuída ao microsserviço e abra:

```text
http://localhost:PORTA/h2-console
```

Dados de conexão:

```text
JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password: em branco
```

## Execução local pelo Maven Wrapper

Não é necessário instalar Maven separadamente. Cada projeto contém Maven Wrapper.

### Windows

Em terminais separados:

```powershell
cd eureka-server
.\mvnw.cmd spring-boot:run
```

```powershell
cd api-gateway
.\mvnw.cmd spring-boot:run
```

```powershell
cd ms-space
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=test
```

```powershell
cd ms-project
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=test
```

### Linux/macOS

Em terminais separados:

```bash
cd eureka-server
./mvnw spring-boot:run
```

```bash
cd api-gateway
./mvnw spring-boot:run
```

```bash
cd ms-space
./mvnw spring-boot:run -Dspring-boot.run.profiles=test
```

```bash
cd ms-project
./mvnw spring-boot:run -Dspring-boot.run.profiles=test
```

## Comandos Docker úteis

### Ver estado

```bash
docker compose ps
```

### Ver logs

```bash
docker compose logs -f
```

### Parar e remover containers e rede, preservando dados

```bash
docker compose down
```

### Subir novamente preservando dados

```bash
docker compose up -d
```

### Reconstruir após alteração de código

```bash
docker compose up -d --build
```

### Recriar containers sem reconstruir as imagens

```bash
docker compose up -d --force-recreate
```

### Recriar o ambiente do zero

> Este comando remove também o volume e apaga os bancos locais do projeto.

```bash
docker compose down -v
docker compose up -d --build
```

## Respostas HTTP

A solução retorna códigos compatíveis com as operações:

- `200 OK`: leitura, atualização ou alteração de status;
- `201 Created`: criação, com URI no cabeçalho `Location`;
- `204 No Content`: exclusão;
- `400 Bad Request`: JSON malformado, enum ou parâmetro inválido;
- `404 Not Found`: recurso inexistente;
- `409 Conflict`: regra de negócio ou transição de status inválida;
- `422 Unprocessable Content`: falha de Bean Validation;
- `503 Service Unavailable`: fallback por indisponibilidade do `ms-space`.

## Solução de problemas

### Os endpoints retornam erro logo após subir o Compose

Os containers podem estar ativos antes de todos os serviços concluírem o registro. Aguarde `API-GATEWAY`, `MS-SPACE` e `MS-PROJECT` aparecerem como `UP` no Eureka.

### Gateway retorna 503

Confirme:

```bash
docker compose ps
docker compose logs ms-space
docker compose logs ms-project
```

Verifique também o painel do Eureka.

### Porta 3306 em uso

Pare outro MySQL local ou altere o mapeamento de porta do host. A comunicação interna do Compose continua usando `mysqldb:3306`.

### Portas 8082 ou 8761 em uso

Pare aplicações executadas pelo IntelliJ antes de subir o Compose.

### Swagger exibe "Failed to load remote configuration"

Confirme se o `docker-compose.yaml` contém, para cada microsserviço, as variáveis:

```text
SPRINGDOC_SWAGGER_UI_CONFIG_URL
SPRINGDOC_SWAGGER_UI_URL
```

Depois recrie os containers:

```bash
docker compose up -d --force-recreate ms-space ms-project
```

### O banco manteve dados de testes anteriores

O volume nomeado persiste os dados. Para retornar aos dados iniciais:

```bash
docker compose down -v
docker compose up -d --build
```