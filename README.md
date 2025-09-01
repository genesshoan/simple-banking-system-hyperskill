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
CREATE TABLE IF
