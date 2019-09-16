=== Simple REST API


To build the application:
```
gradle build
```

To run the application:
```
gradlew run
```



Sample successful PUT request with a correct response status code:
```
http://localhost:8080/api/accounts/transfer
```

![Image of ScreenShot1](https://user-images.githubusercontent.com/52873210/64944455-366bcc00-d823-11e9-940c-5036782fdc1e.png)




Sample successful GET request showing a list of all accounts and each current balance:
```
http://localhost:8080/api/accounts/
```

![Image of ScreenSht2](https://user-images.githubusercontent.com/52873210/64944484-484d6f00-d823-11e9-8384-fb6bde1ed43c.png)
