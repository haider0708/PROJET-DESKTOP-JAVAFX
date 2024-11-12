
# Healthcare Management System (JavaFX)

## Overview

This project is a comprehensive Healthcare Management System built with JavaFX.

## Getting Started

### Prerequisites

- JDK >= 11
- JavaFX SDK
- MySQL or PostgreSQL

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/haider0708/PROJET-DESKTOP-JAVAFX.git
   cd healthcare-system-javafx
   ```

2. Configure the database connection in the configuration file:
   ```plaintext
   db.url=jdbc:mysql://localhost:3306/healthcare_db
   db.username=your-db-username
   db.password=your-db-password
   ```

3. Build and run the project using Maven:
   ```bash
   mvn clean install
   java -jar target/healthcare-system-javafx.jar
   ```

## Built With

- JavaFX - UI framework
- JDK 11 - Java Development Kit
- SQL Database (MySQL/PostgreSQL) - For persistent data storage
- OpenAI API - For ChatGPT functionality
- PDFBox or similar library - For PDF reading

## License

Licensed under the MIT License - see the [LICENSE](LICENSE) file.
