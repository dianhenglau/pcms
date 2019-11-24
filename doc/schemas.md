# User

- ID (PK)
- Full name
- Address
- Email
- Role
- Username (UK)
- Password
- Status

# Supplier

- ID (PK)
- Name (UK)
- Email
- Phone
- Address
- Status

# Category

- ID (PK)
- Name (UK)
- Description

# Product

- ID (PK)
- Image
- Name (UK)
- Brand
- Category ID (FK)
- Quantity
- Description
- Retail price
- Current discount
- Supplier ID (FK)

# Catalog

- ID (PK)
- Title
- Banner
- Description
- Season start date
- Season end date
- Product ID - Discount pairs (list of pairs containing FK)
- Created on
- Created by (FK)

# Login Record

- ID (PK)
- User ID (FK)
- Action
- Date and time
