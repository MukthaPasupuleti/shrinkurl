This is Pasupuleti Mukthasree

Spring Version : 3.3.0
Lombok Version : 1.18.30

Mysql Specs
Host        : localhost
Port        : 3306
User        : root
Password    : Mysql@123
Database    : shrinkurl

To run the project use : mvn spring-boot:run -DskipTests //Since the project is using clashing version of frameworks it fails few tests of the compile phase

Workings

Modules :   1. User
            2. Url

Each module has its own entity, dto, service, repository.

1. User

The user has a supporting module; Role. This is used to set security access according to the spring security framework.

The user entity has the following data members :    1. ID       - used for setting key reference in db
                                                    2. Name     - Name of the user
                                                    3. Email    - Acts as the primary key for the user
                                                    4. Password - Authentication


The user repository and user service has the following methods: 1. findByEmail  - Find a user by email. This is used to access all the urls shortened by the user
                                                                2. saveUser     - This is used to store a new user data in the database


2. Url

The url module is accompanied by two seperate dto for full url and shorturl to facilitate better code readability.

The url entity has the following data members : 1. ID       - used for setting key reference in db
                                                2. User     - Email of the user
                                                3. FullUrl  - Full url entered by the user
                                                4. ShortUrl - Shortened Url
                                                5. Usage    - Number of times the shortened Url has been used


The user repository and user service has the following methods: 1. get                          - This is used to access all the urls shortened by the user
                                                                2. getFullUrl                   - This is used to store a new user data in the database
                                                                3. getShortUrl                  - This retrieves the shortened url for the corresponding full url from db
                                                                4. findAllUrl                   - This is find all the urls registered by a particular user
                                                                5. checkFullUrlAlreadyExists    - this is to check if a certatin url has been registed by a certain user
                                                                6. save                         - this is save a new entry to the db

