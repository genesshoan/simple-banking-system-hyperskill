# Simple Banking System

Java project from Hyperskill course. Basic banking system that simulates card operations with SQLite database.

## What it does

- Create accounts with unique card numbers (Luhn algorithm)
- Login with card number and PIN
- Check balance, add income
- Transfer money between accounts
- Everything stored in SQLite

## How to run

```bash
javac -cp "sqlite-jdbc.jar:." *.java
java -cp "sqlite-jdbc.jar:." Main
```

You need the SQLite JDBC driver. Download it from [here](https://github.com/xerial/sqlite-jdbc).

## Structure

```
src/
├── Main.java              # Entry point
├── BankingSystem.java     # Main system logic
├── Account.java           # Account model  
├── DatabaseManager.java   # SQLite handling
├── CardGenerator.java     # Card number generator
└── LuhnValidator.java     # Luhn validation
```

## Database

Single table:
```sql
CREATE TABLE card (
    id INTEGER PRIMARY KEY,
    number TEXT UNIQUE,
    pin TEXT,
    balance INTEGER
);
```

## Luhn Algorithm

Card numbers follow the format:
- Start with "400000" (fake BIN)
- 9 random digits
- 1 check digit calculated with Luhn

Example: `4000001234567890`

## Menus

**Main:**
1. Create an account
2. Log into account
0. Exit

**Account (after login):**
1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit

## Patterns used

- **DAO:** DatabaseManager handles all persistence
- **Factory:** CardGenerator creates cards
- **MVC:** BankingSystem (controller), Account (model), console (view)

## Hyperskill Project

This is my implementation of the [Simple Banking System](https://hyperskill.org/projects/93) project from JetBrains Academy.

Completed stages:
- [x] Card generation with Luhn
- [x] Basic authentication
- [x] SQLite database
- [x] Transfers between accounts