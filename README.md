## Testing REST Functionality

### 1. Run the Application
- Build and run the main application.

### 2. Test with Postman

#### ➤ Add a Tool
- **Method:** `POST`  
- **URL:** `http://localhost:8081/add?name=Drill&category=Power Tools`

**Expected Response:**
Added tool: Drill


#### ➤ List Tools
- **Method:** `GET`  
- **URL:** `http://localhost:8081/list`

**Expected Response:**
```json
[
  {
    "id": 1,
    "name": "Drill",
    "category": "Power Tools",
    "available": true
  }
]
