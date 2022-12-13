# Authentication Approach

### Step 1

- Create a register controller to allow user to register
- Create a User and Role model
- Create UserAuthService containing the user authentication service
- Create RegisterDto which will be sent by the client
- Create UserDetailsServiceImpl which will be used to get user from database
- Create SecurityConfig to allow all request to not be authenticated. Especially a custom implementation DaoAuthenticationProvider using UserDetailsServiceImpl
- Create SpringBeansConfig containing BCrytPasswordEncoder bean
- Add the required application properties

After all these steps, we should be able to create a user in the database

### Step 2

- Create a login controller to allow user to log in
- Add to UserAuthService a method to log in. This method will get an authentication from Spring thanks to the loadUserByUsername
- Authentication will be set in the context and a UserDetails will be returned
- Create JwtUtils to generate a JWT token during the login method
