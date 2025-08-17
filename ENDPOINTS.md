# API Endpoints for Haenaem Project

This document outlines the API endpoints available in the Haenaem Spring Boot project, categorized by their domain.

---

## 1. User Management (`/api/users`)

| Method | Path                                          | Description                                   | Request Body/Params                               |
|--------|-----------------------------------------------|-----------------------------------------------|---------------------------------------------------|
| `POST` | `/api/users`                                  | Register a new user                           | `email`, `nickname`, `password`, `profileImage` (MultipartFile, optional) |
| `POST` | `/api/users/login`                            | Login                                         | `email`, `password`                               |
| `GET`  | `/api/users/me/{userId}`                      | Get user's own information                    | Path Variable: `userId`                           |
| `PATCH`| `/api/users/update/{userId}`                  | Update user information                       | Path Variable: `userId`, Body: `nickname`, `password` (optional) |
| `DELETE`| `/api/users/delete/{userId}`                  | Delete a user                                 | Path Variable: `userId`                           |
| `POST` | `/api/users/{userId}/profile-image`           | Update user's profile image                   | Path Variable: `userId`, RequestParam: `profileImage` (MultipartFile) |
| `PATCH`| `/api/users/admin/{adminUserId}/users/{targetUserId}/role` | (Admin) Update a user's role                  | Path Variables: `adminUserId`, `targetUserId`, RequestParam: `role` (UserRole enum) |

---

## 2. Todo Management (`/api/todos`)

| Method | Path                                          | Description                                   | Request Body/Params                               |
|--------|-----------------------------------------------|-----------------------------------------------|---------------------------------------------------|
| `POST` | `/api/todos/create`                           | Create a new todo                             | `title`, `description`                            |
| `GET`  | `/api/todos/{todoId}`                         | Get a specific todo                           | Path Variable: `todoId`                           |
| `PATCH`| `/api/todos/update/{todoId}`                  | Update a todo                                 | Path Variable: `todoId`, Body: `title`, `description` |
| `DELETE`| `/api/todos/delete/{todoId}`                  | Delete a todo                                 | Path Variable: `todoId`                           |

---

## 3. Shop (`/api/shop`)

| Method | Path                                          | Description                                   | Request Body/Params                               |
|--------|-----------------------------------------------|-----------------------------------------------|---------------------------------------------------|
| `GET`  | `/api/shop/items`                             | Get all shop items                            | None                                              |
| `GET`  | `/api/shop/items/active`                      | Get only active shop items                    | None                                              |
| `GET`  | `/api/shop/items/{itemId}`                    | Get a specific shop item                      | Path Variable: `itemId`                           |
| `POST` | `/api/shop/users/{userId}/items/{itemId}/purchase` | Purchase an item                              | Path Variables: `userId`, `itemId`                |

---

## 4. Inventory (`/api/inventory`)

| Method | Path                                          | Description                                   | Request Body/Params                               |
|--------|-----------------------------------------------|-----------------------------------------------|---------------------------------------------------|
| `POST` | `/api/inventory/users/{userId}/items/{inventoryItemId}/equip` | Equip an item                                 | Path Variables: `userId`, `inventoryItemId`       |
| `POST` | `/api/inventory/users/{userId}/items/{inventoryItemId}/unequip` | Unequip an item                               | Path Variables: `userId`, `inventoryItemId`       |
| `POST` | `/api/inventory/users/{userId}/items/{shopItemId}` | Add an item to inventory (likely part of purchase flow) | Path Variables: `userId`, `shopItemId`            |
| `GET`  | `/api/inventory/users/{userId}/equipped`      | Get equipped items                            | Path Variable: `userId`                           |
| `GET`  | `/api/inventory/users/{userId}/unequipped`    | Get unequipped items                          | Path Variable: `userId`                           |

---

## 5. Room (`/api/room`)

| Method | Path                                          | Description                                   | Request Body/Params                               |
|--------|-----------------------------------------------|-----------------------------------------------|---------------------------------------------------|
| `GET`  | `/api/room/users/{userId}`                    | Get a user's room                             | Path Variable: `userId`                           |
| `POST` | `/api/room/users/{userId}/items/place`        | Place an item in the room                     | Path Variable: `userId`, Body: `inventoryItemId`, `x`, `y` |
| `DELETE`| `/api/room/users/{userId}/items/{inventoryItemId}` | Remove an item from the room                  | Path Variables: `userId`, `inventoryItemId`       |
