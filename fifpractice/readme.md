## This is a simple CRUD app that shows how a basic crud application works. The app demonstrate the flow of user and equipment creation, supported with functionalities for inserting, showing, updating, and deleting data. This app also supported by JWT Authentication for security.

## ENTITIES in the app:
- User : with id, name, and address
- Equipment : with id, type, color, and file of the equipment (image of it)

## RELATIONS in the app:
- User can have multiple Equipment (One to many)
- Many Equipment can belong to one user (Many to one)

## WHAT YOU CAN DO:
- Insert User data first, then insert equipment data, this process will be stored to the database
- Get the data from the database
- Update the data
- Delete the data

## TESTING TOOLS:
- POSTMAN for testing the crud operations
- JUnit and Mockito for unit testing
- prove can be seen at : https://drive.google.com/drive/folders/1bbnHqe919YlsZVTdqn-gfjf6aszOf9w5?usp=sharing
