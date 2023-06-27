# Java server
### How to run manually (Java11 required)
gradle clean build

KEY='[KEY GOES HERE]' FACEBOOK_APP_SECRET='[FACEBOOK APP SECRET GOES HERE]' ./gradlew bootRun

### How to build docker image
docker build -t appcharge/server .

### How to run the container locallyу
docker run --rm -it -e KEY='[KEY GOES HERE]' -e FACEBOOK_APP_SECRET='[FACEBOOK APP SECRET GOES HERE]' -p8080:8080 appcharge/server

### Where to get the key
Go to the admin panel, open the integration tab, if you are using signature authentication - copy the key from the primary key field, if you are using encryption, take the key from the primary key field and the IV from the secondary key field.
