# Doggo Life API
Doggo Life API is a REST API for a hypothetical social media app. It allows users to perform 
various operations (e.g., get posts from users I follow) through its endpoints. This is a 
backend app built using Java, Spring Boot, Cypher, and SQL. It uses a Neo4j graph database 
for relationships and recommendations, a MySQL database for posts, and AWS S3 for images.

## Key Highlights
•	Developed a REST API for a hypothetical social media app using Java, Spring Boot, Cypher, 
and SQL

•	Utilized Neo4j for relationships and recommendations, MySQL for posts, and AWS S3 for images

## Languages
Java, Spring Boot, Cyper, SQL

## Systems
Neo4j graph database, MySQL relational database, AWS S3

## Endpoint Documentation and Example Screenshots
### Posts (Relational Database) Endpoints
#### Get Posts
URI: /posts/getPosts | Summary: retrieves all posts from the relational database and provides a response with a list of PostEntity objects
<img width="468" alt="image" src="https://github.com/jdouglas9025/doggo-life-api/assets/111395383/134fb710-15b2-45df-bc17-f9ad6023e6da">

#### Get Posts by User
URI: /posts/getPostsByUser | Query Params: username | Summary: retrieves all posts by the specified user from the relational database and provides a response with a list of PostEntity objects

<img width="468" alt="image" src="https://github.com/jdouglas9025/doggo-life-api/assets/111395383/05451062-4bf6-4c8f-99d8-51921c099a09">

#### Create Post
URI: /posts/createPost | Query Params: username, caption, image | Summary: creates a post for the specified user, uploading the associated image to S3 and storing a URI reference to the image in the database entry

<img width="468" alt="image" src="https://github.com/jdouglas9025/doggo-life-api/assets/111395383/76a5c2e7-50ab-462f-a3f3-24cab517790d">

#### Delete Post
URI: /posts/deletePost | Query Params: id | Summary: deletes the specified post from the relational database and associated image from S3

<img width="468" alt="image" src="https://github.com/jdouglas9025/doggo-life-api/assets/111395383/d1e9fe25-0818-4fe1-a07a-a88246210045">

#### Get Posts by Following
URI: /posts/getPostsByFollowing | Query Params: username | Summary: retrieves all posts made by users that the specified user is following (has a “FOLLOWS” relationship in graph database) from the relational database and returns a response with a list of PostEntity objects

<img width="468" alt="image" src="https://github.com/jdouglas9025/doggo-life-api/assets/111395383/2f4245c2-e3c7-4d5f-a98a-46ee2ba95b2f">

#### Get Posts by Interests
URI: /posts/getPostsByInterests | Query Params: username | Summary: retrieves all posts made by users who share a common interest(s) with the specified user (shares a “LIKES” relationship in graph database) from the relational database and returns a response with a list of PostEntity objects

<img width="468" alt="image" src="https://github.com/jdouglas9025/doggo-life-api/assets/111395383/4bc76229-4944-4379-984d-5f476065dda1">

### Users (Graph Database) Endpoints
#### Get Users
URI: /users/getUsers | Summary: retrieves all users from the graph database and returns a response with a list of UserEntity objects

<img width="468" alt="image" src="https://github.com/jdouglas9025/doggo-life-api/assets/111395383/79395696-4feb-4593-9884-85d7767564b2">

#### Get User
URI: /users/getUser | Query Params: username | Summary: retrieves the specified user from the graph database and returns a response with a UserEntity object

<img width="468" alt="image" src="https://github.com/jdouglas9025/doggo-life-api/assets/111395383/ea5d7e21-3b17-45c2-8053-5e6a4be16f57">

#### Get Followers
URI: /users/getFollowers | Query Params: username | Summary: retrieves all users who are following (has a “FOLLOWS” relationship) the specified user from the graph database and returns a response with a list of UserEntity objects

<img width="468" alt="image" src="https://github.com/jdouglas9025/doggo-life-api/assets/111395383/a2d79e32-408e-4cf6-bd87-fc0e0428fc00">

#### Create User
URI: /users/createUser | Body: JSON mapping of user | Summary: creates a user based on the JSON body argument in the graph database and returns a response with the configured UserEntity object

<img width="468" alt="image" src="https://github.com/jdouglas9025/doggo-life-api/assets/111395383/3460e52e-ab18-4705-b8a4-7bd3728a4b64">

#### Follow User
URI: /users/followUser | Query Params: source, target | Summary: follows the specified target user from the source user (creates a “FOLLOWS” relationship from source to target) in the graph database

<img width="468" alt="image" src="https://github.com/jdouglas9025/doggo-life-api/assets/111395383/5a42fad0-444a-4eb9-9868-3372c0e06c3e">

#### Like Interest
URI: /users/likeInterest | Query Params: username, interest | Summary: likes the specified interest from the source user (creates a “LIKES” relationship from user to interest) in the graph database

<img width="468" alt="image" src="https://github.com/jdouglas9025/doggo-life-api/assets/111395383/53b3b912-8918-4970-9fa3-557fa96e7850">


Additional endpoints coming soon. Stay tuned!
