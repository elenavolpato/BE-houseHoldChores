# Chores Mate — Backend Architecture Documentation
(Italiano sotto)

Welcome to the backend engineering documentation for **Chores Mate**. This service provides a multi-tenant household coordination system, handling secure user authentication, real-time chore tracking, and shared tasks.
The backend was built already with som functionalities that the FE hasn't deployed yet. Such as: creation of groceries lists, filtering tasks by user, and others.

---

## 🛠️ Tech Stack & Production Infrastructure

* **Language Runtime:** Java 21 (OpenJDK)
* **Framework:** Spring Boot 3.x (Web, Security, Data JPA)
* **Database Integration:** PostgreSQL via HikariCP Connection Pooling
* **Security layer:** JWT (JSON Web Tokens) + BCrypt Password Hashing
* **Hosting Cloud:** Render (Web Service Mode)

---

## 🚀 Live API Server & Interactive Documentation

The production REST API is live and actively processes transactional operations. Interactive API exploration, live parameter testing, and schema contracts are fully handled via Swagger / OpenAPI 3.0.

* **Production API Gateway Base URL:** `(https://spring-boot-api-production-980f.up.railway.app/)`
* **Interactive Testing Panel:** 👉 **[Live Swagger UI Documentation](https://spring-boot-api-production-980f.up.railway.app/swagger-ui/index.html)**

> 🔐 **API Authentication Notice:** To invoke protected operations directly within Swagger, register or log in via the authentication resource endpoints. Copy the returned token, select the **Authorize** lock emblem at the top of the dashboard, and input the parameter as: `Bearer <your_token>`.

---

## 🛡️ Router Security & Middleware Architecture

Our Spring Security pipeline monitors and separates resource visibility using token-intercept criteria:

### 1. Unprotected Public Layer
These pathways are accessible globally without authentication tokens:
* `POST /api/auth/register` — Onboards a fresh individual identity.
* `POST /api/auth/login` — Confirms credentials and drops down user claims and a JWT.
* `POST /api/auth/register-with-invite` — Handshakes onboarding identities straight to an existing household.

### 2. Guarded Context Layer
Requires an explicit HTTP Request Header signature: `Authorization: Bearer <JWT_TOKEN>`. The custom internal security filter intercepts incoming traffic, parses the signature, validates structural expiration timestamps, and securely injects the matching principal profile context into the application thread pool.

---

## 🗺️ System API Schema Blueprint

### 🗝️ Identity & Profile Layer (`/api/auth`)
| HTTP Method | Endpoint Target | Operational Action |
| :--- | :--- | :--- |
| `GET` | `/api/auth/me` | Validates active authorization state and returns the current user profile. |
| `POST` | `/api/auth/forgot-password` | Dispatches password renewal indicators. |
| `POST` | `/api/auth/reset-password` | Commits credential modifications. |

### 👥 Household Group Layer (`/api/groups`)
| HTTP Method | Endpoint Target | Operational Action |
| :--- | :--- | :--- |
| `POST` | `/api/groups/create` | Initializes a unique group identity code and registers the creator as head. |
| `POST` | `/api/groups/join` | Maps an authenticated profile onto a group via an active invitation link. |
| `DELETE` | `/api/groups/leave` | Destroys active group context bindings for the profile. |

### 📋 Chores & Task Engine (`/api/tasks`)
| HTTP Method | Endpoint Target | Operational Action |
| :--- | :--- | :--- |
| `GET` | `/api/tasks/weekly` | Grabs the current weekly chore grid for the household. |
| `POST` | `/api/tasks/new` | Spawns a new chore configuration entry. |


### 🛒 Grocery Inventory Layer (`/api/groceries`) - not yet on the FE
| HTTP Method | Endpoint Target | Operational Action |
| :--- | :--- | :--- |
| `GET` | `/api/groceries` | Pulls the active checklist items for the household. |
| `POST` | `/api/groceries` | Pushes a item down into the group shopping array. |

---

## ⚙️ Environment Configuration & Deployment Specs

To prevent binding conflicts or synchronization locks between local engineering work and production clusters, the codebase dynamically maps connection variables.

### Environment Variable Bindings
Ensure these keys match your target server panel exactly (e.g., your Render Environment settings):

```properties
# Custom Network Bindings (Maps to dynamic environment host assignments)
server.port=${PORT:3001}

# Production Relational Database URL Mapping
spring.datasource.url=jdbc:postgresql://<production_db_host>:<port>/<db_name>
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# Connection Life Safeguard (Prevents silent boot stalls)
spring.datasource.hikari.connection-timeout=5000

# JPA Engine Behavior Mapping
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Internal Cryptography Configuration
jwt.secret.key=${JWT_SECRET_ENCRYPTION_STRING}
jwt.expiration.ms=86400000
```

---

# Chores Mate — Documentazione dell’Architettura Backend  
(English sopra)

Benvenuto nella documentazione di ingegneria backend per **Chores Mate**. Questo servizio fornisce un sistema multi-tenant per il coordinamento domestico, gestendo autenticazione sicura degli utenti, tracciamento in tempo reale delle faccende e attività condivise.

Il backend è già stato sviluppato con alcune funzionalità che il frontend non ha ancora distribuito, come: creazione di liste della spesa, filtraggio delle attività per utente e altro.

---

## 🛠️ Stack Tecnologico & Infrastruttura di Produzione

- **Runtime del linguaggio:** Java 21 (OpenJDK)  
- **Framework:** Spring Boot 3.x (Web, Security, Data JPA)  
- **Integrazione database:** PostgreSQL tramite pooling di connessioni HikariCP  
- **Livello di sicurezza:** JWT (JSON Web Tokens) + hashing password BCrypt  
- **Hosting Cloud:** Render (modalità Web Service)

---

## 🚀 Server API Live & Documentazione Interattiva

L’API REST in produzione è attiva e gestisce operazioni transazionali. L’esplorazione interattiva delle API, i test live dei parametri e i contratti degli schemi sono completamente gestiti tramite Swagger / OpenAPI 3.0.

- **URL base API in produzione:** `https://be-householdchores.onrender.com`  
- **Pannello di test interattivo:** 👉 **[Documentazione Swagger UI Live](https://be-householdchores.onrender.com/swagger-ui/index.html)**  

> 🔐 **Nota autenticazione API:** Per invocare operazioni protette direttamente in Swagger, registrati o effettua login tramite gli endpoint di autenticazione. Copia il token restituito, seleziona l’icona **Authorize** in alto nel dashboard e inserisci il valore come: `Bearer <tuo_token>`.

---

## 🛡️ Sicurezza Router & Architettura Middleware

La pipeline di Spring Security monitora e separa la visibilità delle risorse usando criteri di intercettazione basati su token.

### 1. Livello pubblico non protetto
Questi endpoint sono accessibili globalmente senza token di autenticazione:

- `POST /api/auth/register` — Registra un nuovo utente nel sistema  
- `POST /api/auth/login` — Verifica le credenziali e restituisce un JWT  
- `POST /api/auth/register-with-invite` — Onboarding in una household esistente tramite invito  

---

### 2. Livello protetto
Richiede un header HTTP:

`Authorization: Bearer <JWT_TOKEN>`

Il filtro di sicurezza intercetta il traffico, valida il token, verifica la scadenza e inietta il profilo utente nel contesto dell’applicazione.

---

## 🗺️ Schema API del Sistema

### 🗝️ Livello Identità & Profilo (`/api/auth`)

| Metodo | Endpoint | Azione |
|--------|----------|--------|
| GET | `/api/auth/me` | Restituisce il profilo utente autenticato |
| POST | `/api/auth/forgot-password` | Avvia procedura di reset password |
| POST | `/api/auth/reset-password` | Applica il reset della password |

---

### 👥 Livello Gruppi Familiari (`/api/groups`)

| Metodo | Endpoint | Azione |
|--------|----------|--------|
| POST | `/api/groups/create` | Crea un nuovo gruppo domestico |
| POST | `/api/groups/join` | Unisce un utente a un gruppo tramite invito |
| DELETE | `/api/groups/leave` | Rimuove l’utente dal gruppo |

---

### 📋 Motore Faccende & Attività (`/api/tasks`)

| Metodo | Endpoint | Azione |
|--------|----------|--------|
| GET | `/api/tasks/weekly` | Recupera le faccende settimanali del gruppo |
| POST | `/api/tasks/new` | Crea una nuova attività |

---

### 🛒 Lista Spesa (`/api/groceries`) — non ancora nel FE

| Metodo | Endpoint | Azione |
|--------|----------|--------|
| GET | `/api/groceries` | Recupera la lista della spesa attiva |
| POST | `/api/groceries` | Aggiunge un elemento alla lista condivisa |

---

## ⚙️ Configurazione Ambiente & Deployment

Per evitare conflitti tra ambiente locale e produzione, il progetto usa variabili d’ambiente dinamiche.

### Variabili di configurazione

```properties
# Porta server dinamica (Render o locale)
server.port=${PORT:3001}

# Connessione database PostgreSQL
spring.datasource.url=jdbc:postgresql://<production_db_host>:<port>/<db_name>
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# HikariCP connection pool timeout
spring.datasource.hikari.connection-timeout=5000

# Configurazione JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Configurazione JWT
jwt.secret.key=${JWT_SECRET_ENCRYPTION_STRING}
jwt.expiration.ms=86400000?
```
