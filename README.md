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

## Technologies

* Java 8
* Spring Boot
* Spring Web (including Spring MVC)
* H2 (in-memory database)
* Spring Data (JPA)
* Hibernate (ORM)
* Spring HATEOAS
