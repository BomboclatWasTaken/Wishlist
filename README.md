# Ønskeseddel

Ønskeseddel er en webapplikation, hvor brugere kan oprette ønskelister, tilføje ønsker til dem og dele listerne med familie og venner via et link. Applikationen er lavet som semesterprojekt på 2. semester af Datamatikeruddannelsen på Erhvervsakademiet i København.


Applikationen er hostet hos Microsoft Azure, og databasen kører som en MySQL-database hos Microsoft Azure.

## Softwaremæssige forudsætninger

### For at bruge applikationen

- En almindelig webbrowser (fx Chrome, Firefox, Edge eller Safari)
- Internetforbindelse

Der skal ikke installeres noget. Opret en bruger på siden, og log ind.

### For at køre applikationen lokalt

| Forudsætning | Version / bemærkning |
|---|---|
| Java (JDK) | 17 eller nyere |
| Maven | Kan også køres direkte fra IntelliJ |
| MySQL Server | 8.x |
| IDE (valgfri) | Fx IntelliJ IDEA |


## Funktioner

- Opret bruger og log ind og ud
- Opret, omdøb og slet egne ønskelister
- Tilføj, redigér og slet ønsker med navn, beskrivelse, pris, produktlink og billede
- Del en ønskeliste via et link, som kan åbnes uden login
- Brugere kan kun se og ændre deres egne lister og ønsker

## Teknologi og opbygning

- **Java** og **Spring Boot** (Spring MVC)
- **Thymeleaf** til HTML-skabeloner
- **Spring JDBC** (`JdbcTemplate`) til adgang til databasen
- **MySQL** som database

Koden er delt i lag, hvor hvert lag kun kender laget under sig:

```
controller  →  service  →  repository  →  MySQL
                  ↓
               model
```

| Pakke | Ansvar |
|---|---|
| `controller` | Tager imod HTTP-requests, vælger hvilken side der vises |
| `service` | Forretningslogik, validering og tjek af ejerskab |
| `repository` | SQL mod databasen (interfaces og implementationer) |
| `model` | Klasserne `User`, `Wishlist` og `Wish` |
| `exception` | Egne exceptions, fx ved ugyldigt login eller ukendt liste |

## Database

Databasen består af tre tabeller. En bruger har mange lister, og en liste har mange ønsker. Når en liste slettes, slettes dens ønsker automatisk (`ON DELETE CASCADE`).

```mermaid
erDiagram
    users ||--o{ wish_list : "ejer"
    wish_list ||--o{ wish : "indeholder"

    users {
        int id PK
        string name
        string email UK
        string password
    }

    wish_list {
        int id PK
        string title
        string share_code UK
        int user_id FK
    }

    wish {
        int id PK
        string name
        string description
        double price
        string link_url
        string picture_url
        int wish_list_id FK
    }
```

## Afgrænsninger

- Adgangskoder gemmes i klartekst og bliver ikke hashet.
- Det er ikke muligt at reservere ønsker på en delt liste.

## Gruppe 1

Mathias, Oliver, Rasmus & Sebastian
