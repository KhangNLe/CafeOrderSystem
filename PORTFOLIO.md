🧭 Portfolio Overview: CafeOrderSystem  
This project was completed as part of **ICS372: Object-Oriented Design and Implementation** at 
**Metropolitan 
State University** (Summer 2025).

---

🚀 Project Summary  
A role-based café ordering and management system that:

- Provides dedicated interfaces for **Customers**, **Baristas**, and **Managers**
- Supports **real-time inventory tracking** with ingredient depletion
- Persists menu items, orders, and inventory in JSON format
- Implements role-based authentication and secure access control
- Uses **Java 21 records** for immutable menu data
- Follows **MVC architecture** with clear separation of UI, business logic, and data persistence layers

---

🧠 Technologies Used
- Java 21
- JavaFX 21 (Controls, FXML)
- Maven
- Jackson (Core, Databind) for JSON serialization/deserialization
- JUnit 5 for testing

---

🧩 Key Components Implemented

**Menu/Items** (record classes):
- `BeverageCost`, `BeverageItem`, `CustomItem`, `PastriesCost`, `PastriesItem`
    - **Reason for using records:** Ensures immutability and thread safety for menu data.
    - **Suitability:** Ideal for value-based domain objects without mutable state.
    - **Why selected:** Reduces boilerplate, improves maintainability, and leverages Java 21 features.

**Inventory Management**
- Tracks stock levels in real time and raises exceptions when items run out.

**JSON Parser Suite**
- Custom Jackson-based serializers/deserializers for menus, orders, and authentication.
- Centralized in `ParserManagement` for consistency across modules.

---

📊 Architecture & Design Documentation

- **UML Diagrams**
    - **Use Case Diagram**: Illustrates interactions for Customers, Baristas, and Managers.
    - **Class Diagram**: Maps conceptual entities to software classes, including relationships, inheritance, and composition.
    - **Sequence Diagrams**: Show message flow for core use cases (e.g., placing an order, fulfilling an order, updating inventory).
    - **Activity Diagram**: Demonstrates state changes in the order lifecycle.

- **Wireframes**
    - Clear, annotated wireframes for Customer, Barista, and Manager interfaces.
    - Emphasis on intuitive navigation and role-specific actions.

- **MVC Implementation Justification**
    - **Model**: Domain objects (`Menu`, `Orders`, `Inventory`) and parsers handle business logic and data persistence.
    - **View**: FXML-based UI layouts with role-specific screens.
    - **Controller**: JavaFX controllers handle user input and mediate between View and Model.
    - **Justification**: Ensures clean separation of concerns, testability, and ease of maintenance.

- **OO Principles & Patterns**
    - **Observer Pattern**: Used for updating UI when inventory or order status changes.
    - **Factory Method**: Centralized creation of menu items and parsers.
    - **SRP (Single Responsibility Principle)**: Each class has a narrowly defined responsibility (e.g., `OrdersManagement` only handles order logic).
    - **Cohesion**: Grouped related behavior within the same module for high cohesion.
    - **Inheritance**: Used where roles or items share a common abstraction.
    - **Composition**: Preferred for “has-a” relationships (e.g., an `Order` has multiple `OrderItem` objects).

---

📊 Sample Workflow

1. Customer browses menu and place order
2. Order is sent to Barista UI with preparation status updates
3. Ingredients are deducted from inventory
4. Manager can adjust the menu, restock inventory, and review order history

---

🧪 Testing Strategy
- JUnit 5 unit tests for:
    - Order creation & status changes
    - Inventory depletion logic
    - JSON parsing & data persistence
- Integration tests covering full customer-to-barista flow
- Exception handling tests for invalid input & stock shortages

---

💬 What I Learned
- Designed complete UML artifacts (Use Case, Class, Sequence, Activity diagrams) to validate conceptual design before coding
- Created wireframes to guide UI development for multiple user roles
- Applied MVC architecture rigorously, ensuring separation of concerns
- Implemented Observer and Factory Method patterns to meet mandatory requirements
- Practiced applying SRP, cohesion, inheritance, and composition principles in a real-world system

---

🎯 Resume Summary  
Designed and implemented a role-based café ordering system with JavaFX and Java 21 records, featuring real-time inventory management, custom JSON parsers, and persistent data storage.
- Produced UML Use Case, Class, Sequence, and Activity diagrams to model system behavior
- Designed annotated wireframes for role-specific UI screens
- Applied MVC architecture, Observer, and Factory Method patterns
- Implemented immutable domain models using Java 21 `record` types
- Built a modular, maintainable codebase applying SRP, cohesion, inheritance, and composition principles
- Stack: Java 21, Maven, JavaFX 21, Jackson, JUnit 5, Git
