
### 🔐 Segurança

- Implementação de **autenticação e autorização com JWT (JSON Web Token)**  
- Cada usuário possui um **Auth vinculado ao seu Role**  
- Tokens incluem tempo de expiração e status de bloqueio  
- Perfis de acesso:
  - `ADMIN` → Gerencia todas as entidades
  - `DOCTOR` → Solicita e acompanha exames
  - `SECRETARY` → Gerencia cadastros e agendamentos
  - `LABORATORY` → Recebe, processa e envia resultados
  - `PATIENT` → Visualiza resultados e solicitações

---

## 🗄️ Banco de Dados

- **Sistema Gerenciador:** PostgreSQL  
- **ORM:** Hibernate (implementação JPA)
- **Criação automática das tabelas:** `spring.jpa.hibernate.ddl-auto=update`
- **Banco de testes:** H2 (memória)

---

## 🚀 Tecnologias Utilizadas

- **Linguagem:** Java 23
- **Framework principal:** Spring Boot 
- **Dependências:**
  - Spring Web  
  - Spring Data JPA  
  - Spring Security  
  - PostgreSQL Driver  
  - Lombok  
  - Validation  
  - JUnit 5 / Mockito  
  - H2 Database (para testes)

---

## 🧪 Testes Automatizados

Os testes utilizam **JUnit 5** com contexto de aplicação carregado via **@DataJpaTest** e **@SpringBootTest**.  
O objetivo é validar:
- Regras de negócio em `Service`
- Integração entre repositórios e entidades
- Persistência e deleção em cascata
- Consistência dos relacionamentos entre as entidades

Banco de testes: **H2 (em memória)**.

---

## 💻 Como Executar o Projeto Localmente

### Pré-requisitos

- **Java 23+**
- **Maven**
- **PostgreSQL** instalado e configurado

### Passos

1. Clone o repositório:
   ```bash
   git clone https://github.com/SEU_USUARIO/SCX.git
2. Rode a aplicação em uma IDE com suporte ao java
