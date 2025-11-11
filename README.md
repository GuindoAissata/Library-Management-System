# Library-Management-System
Project in Advanced Object-Oriented Technology / Java / JavaFX / MySQL / UML

## 1. Objective
The goal of this project is to develop a Java-based software application to computerize and manage the borrowing and returning operations of documents within a library.
The system should allow library administrators to efficiently manage members, documents, and borrowing transactions.

## 2. Main Features
### 2.1. Document Management

Add: Register new documents (Books, Magazines, CDs, etc.) with complete information.

Search: Find documents by title, author, ISBN/ID, or genre.

Edit: Update the information of an existing document.

Delete: Remove a document from the catalog (if not currently borrowed).

### 2.2. Member Management

Registration: Add a new member with a unique identifier, name, and contact details.

Search: Find a member by ID or name.

Edit: Update member information.

History: Display a member’s borrowing history.

Status: Check whether the member has any penalties (late returns).

### 2.3. Borrowing and Returning Management (System Core)

Borrowing: Record a borrowing transaction by verifying:

The document’s availability.

The maximum number of items allowed per member (e.g., 5 documents).

The member’s status (no major penalties or unresolved late returns).

The borrowing date and calculate the expected return date (e.g., 3 weeks later).

Returning: Record a document return by verifying:

Whether the return is late.

If so, calculate the penalty (e.g., €0.50 per day late).

Update the document’s status to Available.

Consultation: List all ongoing borrowings.

Alerts: Identify overdue borrowings.
