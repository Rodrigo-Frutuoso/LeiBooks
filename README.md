# 📚 LEIBooks

Practical Assignment 1 — Object-Oriented Development  
Computer Engineering — 2024/2025

## 🧠 Objective

**LEIBooks** is a digital reading resource organizer. The application includes features such as:

- Adding and removing documents from a local library
- Reading documents with bookmarks and annotations
- Organizing documents into shelves (normal and smart)
- Integration with external metadata readers and document viewers

## 📁 Project Structure

```
.
├── app                 # Application configuration
├── domain              # Domain logic (documents, library, shelves, etc.)
├── services            # External services (readers, viewers)
├── ui                  # Graphical user interface (Swing, provided)
├── utils               # Utilities (observer pattern, regex support)
├── test                # JUnit unit tests
├── resources           # Config and sample files
└── README.md           # This file
```

## 🚀 How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/your-user/leibooks_XXXXX_YYYYY.git
   ```
2. Open the project in your IDE (Eclipse or VSCode).
3. Run the app using the `SimpleClient` class.
4. Run the unit tests using JUnit.

> ⚠️ Requirements: Java 17+, IDE with Java support (Maven or standard project structure).

## ✅ Features Implemented

- 🖥️ **Graphical User Interface**: A simple GUI based on Swing is provided and integrated, supporting all core functionalities.

- Searchable and organizable document library
- Support for multiple file types (PDF, images, text)
- Shelves:
  - **Normal**: manually managed
  - **Smart**: automatically populated (e.g., "Recent", "Bookmarked")
- Observer-based event system
- Extensible metadata reader system (dynamic loading)
- Simple GUI with Swing (provided)

## 🧪 Testing

Unit tests have been implemented for the following classes:

- `Document`
- `Library`
- `NormalShelf` (additional class)

Tests are located under `test/` and follow JUnit best practices.

## 📄 Design Adaptation (Part 2)

The file `adaptacao_solucao.pdf` describes design proposals for:

- **Folder Shelves** (shelves with sub-shelves)
- Integration with an **online book store** for importing free books

> Only design adaptation provided — no implementation required.

- User `dco000` added with Reporter access.

## 👨‍💻 Group

- Student 1: `61863` — Tiago Leite
- Student 2: `61865` — Rodrigo Frutuoso
---

> Project developed for the Object-Oriented Development course, Academic Year 2024/2025 — Department of Informatics, FCUL.