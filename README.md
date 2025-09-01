# Simple Banking System

Java project from Hyperskill course. Basic banking system that simulates card operations with SQLite database.

## What it does

- Create accounts with unique card numbers (Luhn algorithm)
- Login with card number and PIN validation
- Check balance, add income
- Transfer money between accounts with transaction support
- Close accounts
- Everything stored in SQLite with proper error handling

## How to run

You need the SQLite JDBC driver. Download it from [here](https://github.com/xerial/sqlite-jdbc).

### Compilation and execution:
```bash
# If using the JAR in the project root
javac -cp "sqlite-jdbc.jar:." src/main/java/dev/shoangenes/*.java
java -cp "sqlite-jdbc.jar:src/main/java" dev.shoangenes.Main

# Or if using Maven structure
mvn compile exec:java -Dexec.mainClass="dev.shoangenes.Main"
```

## Project Structure

```
src/main/java/dev/shoangenes/
├── Main.java              # Entry point and application setup
├── BSClient.java          # User interface and menu handling  
├── BankingSystem.java     # Main business logic
├── Account.java           # Account model/entity
├── DatabaseManager.java   # SQLite persistence layer
├── CardGenerator.java     # Card number and PIN generation
├── LuhnValidator.java     # Luhn algorithm validation
├── InputReader.java       # User input handling and validation
└── DatabaseException.java # Custom exception for DB errors
```

## Database Schema

Single table with proper data types:
```sql
CREATE TABLE IF NOT EXISTS cards (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    card_number TEXT NOT NULL UNIQUE,
    pin TEXT NOT NULL,
    balance FLOAT NOT NULL
);
```

## Luhn Algorithm Implementation

Card numbers follow the format:
- **BIN:** `400000` (fake Bank Identification Number)
- **Account number:** 9 sequential digits (based on database ID)
- **Check digit:** 1 digit calculated using Luhn algorithm

**Example:** `4000000000000033`
- Base: `400000000000003` 
- Check digit: `3` (calculated via Luhn)

### How Luhn works in this implementation:
1. Take digits from right to left (excluding check digit)
2. Multiply every second digit by 2 (starting from the rightmost)
3. If result > 9, subtract 9
4. Sum all digits
5. Check digit = `(10 - (sum % 10)) % 10`

## Application Menus

**Main Menu:**
1. Create an account
2. Log into account  
3. Exit

**Account Menu (after login):**
1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit

## Key Features

- **Transaction Safety:** Transfers use database transactions with rollback on failure
- **Input Validation:** Card numbers validated with Luhn, amounts checked for positivity
- **Error Handling:** Custom exceptions with meaningful messages
- **Functional Programming:** Uses `BiFunction` and `Predicate` for validation logic
- **Secure Operations:** PIN validation, account existence checks
- **Resource Management:** Proper database connection handling

## Design Patterns Used

- **DAO (Data Access Object):** `DatabaseManager` handles all persistence
- **Factory Pattern:** `CardGenerator` creates valid card numbers and PINs  
- **MVC Architecture:** 
  - Model: `Account`
  - View: `BSClient` + `InputReader` 
  - Controller: `BankingSystem`
- **Exception Handling:** Custom `DatabaseException` for consistent error management

## Technical Implementation Details

- **Database:** SQLite with JDBC driver
- **Validation:** Luhn algorithm for card numbers, business rule validation
- **Concurrency:** Transaction management for consistent transfers
- **Memory Management:** Try-with-resources for automatic cleanup
- **Type Safety:** Optional<> for null-safe operations

## Hyperskill Project

This is my implementation of the [Simple Banking System](https://hyperskill.org/projects/93) project from JetBrains Academy.

### Completed stages:
- [x] Card generation with Luhn algorithm
- [x] Basic authentication system
- [x] SQLite database integration  
- [x] Money transfers with transaction support
- [x] Account management (create/close)
- [x] Input validation and error handling
- [x] Clean architecture with separation of concerns

## Development Notes

Developed in 2 days as a learning exercise for:
- Database connectivity and transactions
- Luhn algorithm implementation
- Clean code architecture
- Java best practices (Optional, functional interfaces, proper exception handling)
