# ECommerce App

Below you will find the 3 user stories that are implemented in this Application.

## User Requirements

### Create database schema

Create schema, which will allow storing different products and categories.
Keep in mind that getting full category path for the product might be a requirement in the future.
Currency for product price is EUR.
You can choose any RDBMS.

### Develop RESTful API for managing products and categories

A client should be able to perform CRUD operations for Product and Category resources.
Add an ability to create products with price in different from EUR currency. Integrate the system with some open currency exchange rates API (for example http://fixer.io)
Use Spring as a core framework.
Use JPA/ORM
Use Spring MVC.
Any other technologies / libraries can be chosen on your own.

### Secure Web Application

Add Spring Security support.
Add a "super" user.

## Assumptions

1. A product might be associated to multiple categories. For instance, an electric toothbrush may belong to both "Electronics" and "Beauty & Personal Care" categories.
2. There are some fields that for simplicity we are not having in Product table such as description, features, price discount, refurbished, among others.

## Testing

### Authentication

Currently there are two roles defined as follows:
- ADMIN: it's the super user role that can manipulate categories and products.
- USER: it's a user role that can manipulate only products. It can also get data for products and categories. Ideally, Users should only access to their own Products.

#### Admin User
`curl -H "Content-Type: application/json" -X POST "http://localhost:8080/login" -d '{ "username": "admin", "password": "admin" }'`

#### Normal User
`curl -H "Content-Type: application/json" -X POST "http://localhost:8080/login" -d '{ "username": "user", "password": "user" }'`

### Resources

Excepting the `/login` API endpoint that is being used for authentication, the other API endpoints require a HTTP header to be passed in as parameter
This HTTP header will have the name "Authorization" and the value will be the authentication token that was retrieved during the authentication transaction.
Note: this token is a JWT token that is signed with a private key configured in the server, and it has the user information.
The idea behind this JWT token is to avoid going back to the database to validate whether a JWT token is valid or not, because that information is already contained in the token itself.

Note: we are using HATEOAS-oriented REST endpoints (https://en.wikipedia.org/wiki/HATEOAS), so you will find the possible operations to perform on resources while browsing the main endpoints: `/products` and `/categories`

#### Products

The list of Products is always a paginated result just in case.

URL: `/products`

e.g. to get products: `curl -H "Authorization: XXXX" -X GET "http://localhost:8080/products"`

#### Categories

URL: `/categories`

e.g. to get products: `curl -H "Authorization: XXXX" -X GET "http://localhost:8080/categories"`

##### Add / Remove child categories

To associate / dis-associate a child category with / from a parent category you can use the following URL: `/categories/{parentid}/subcategories/{childid}`
To see the current child categories for a given category, you can do a GET on `/categories/{parentid}/subcategories`

##### Link / Unlink products

To link / unlink products with categories you can use the following URL: `/categories/{categoryid}/products/{productid}`
To see the current products for a given category, you can do a GET on `/categories/{parentid}/products`.
Note: the API will return also products that are being associated indirectly.
That means if a Product is associated with Category B, which is in turn a child of Category A,
then the product is directly associated with Category B, and indirectly associated with Category A.
Accessing to `/categories/A/products` will return that product that is associated with Category A indirectly along with the products being associated directly with the Category A.

## Technologies

* Java 8
* Spring Boot
* Spring Web / MVC
* H2 (in-memory database)
* Spring Data (JPA)
* Hibernate (ORM)
* Spring HATEOAS
