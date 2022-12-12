# Authentication Approach

### Step 1

- Create a register controller to allow user registration
- Create a User and Role model
- Create UserAuthService containing the user authentication service
- Create RegisterDto which will be sent by the client
- Create SecurityConfig to allow all request to not be authenticated
- Create SpringBeansConfig containing BCrytPasswordEncoder bean
- Add the required application properties

After all these steps, we should be able to create a user in the database

