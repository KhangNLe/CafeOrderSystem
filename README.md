# ☕ CafeOrderSystem

A JavaFX-based café ordering and management system that simulates the daily operations of a coffee shop.  
It allows customers to place orders, baristas to manage them, and managers to maintain inventory and menus.

This project was developed as part of a group coursework assignment.

---

## 📜 Features

- **Customer Interface** – Browse the menu, customize orders, and add pastries or beverages.
- **Barista Interface** – View, prepare, and update order statuses.
- **Manager Interface** – Modify menu items, track sales, and update inventory.
- **Inventory Management** – Real-time ingredient tracking with alerts for low stock.
- **Authentication** – Role-based access (Barista, Manager).
- **Persistent Data** – Menus, orders, and inventory saved in JSON format.

---

## 🛠 Tech Stack

- **Language:** Java 21
- **UI Framework:** JavaFX 21 (`javafx-controls`, `javafx-fxml`)
- **Build Tool:** Maven
- **JSON Processing:** Jackson (`jackson-core`, `jackson-databind`)
- **Testing:** JUnit 5

---

## 📦 Dependencies

Defined in [`pom.xml`](pom.xml):

- `org.openjfx:javafx-controls` – JavaFX UI components
- `org.openjfx:javafx-fxml` – FXML-based UI layouts
- `org.junit.jupiter:junit-jupiter-api` & `junit-jupiter` – Unit testing framework
- `com.fasterxml.jackson.core:jackson-core` & `jackson-databind` – JSON parsing, serialization, 
  and deserialization

---

## 🚀 Getting Started

### Prerequisites

- Java 21 or newer
- Maven 3.9+
- Internet connection (for Maven to download dependencies)

### Clone the Repository

```bash
git clone https://github.com/KhangNLe/CafeOrderSystem.git
cd CafeOrderSystem
```

### Run the Application

```bash
mvn clean javafx:run
```

The application will start with the **Welcome Screen UI** by default.

---

## 📂 Project Structure

```
Cafe/CafeOrderSystem/
│
├── CatalogItems/ # Domain model classes for menu categories, sizes, ingredients
├── Exceptions/ # Custom exception classes for error handling
├── Inventory/ # Ingredient tracking and stock management
├── JsonParser/ # Parsers and serializers for JSON data
│ ├── Authentication/ # User authentication parsing
│ ├── CafeMenu/ # Menu item parsing (beverages, pastries, add-ons)
│ ├── KeyDeserializer/ # JSON key deserialization utilities
│ ├── KeySerializer/ # JSON key serialization utilities
│ └── OrderItem/ # Order data parsing
├── Menu/
│  └── Items/ # Immutable data records for menu items:
├── Orders/ # Order objects and management logic
├── Roles/ # Role-based authentication logic
├── UI/ # JavaFX controllers for each role's interface
├── utility/ # Helper classes for FXML loading
└── Cafe.java # Facade entry point for business logic
```

---

## 🧪 Running Tests

```bash
mvn test
```

Unit tests are located alongside their respective packages in `src/test/java`.

---

## 👥 Contributors

- **Will Armstrong**: Backend Logic of Inventory and Roles
- **Khang Le**: JSON Parser/Writer, Backend Logic of Menu and Orders
- **Ali Ouakhchachi**: UI & UX
- **Trevor Reedy**: UI & UX
- **Amen Wolde**: Backend Logic of Roles



---

## 📄 License

This project is licensed under the MIT License – see the [LICENSE](LICENSE) file for details.
