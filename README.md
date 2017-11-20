# REMOVE FILES ON BULK FROM SLACK

This program removes files from slack using the web api from slack.

# REQUISITES

- JDK 1.8
- Maven
- Web token from slack.

To obtain the token it is needed to go to slack 

https://get.slack.help/hc/en-us/articles/215770388-Create-and-regenerate-API-tokens

https://api.slack.com/custom-integrations/legacy-tokens


- User id

To obtain the user id it can be done going on:

https://api.slack.com/methods/files.list/test

Your username must be on a textbox click it and it will generate your userId.

# CONFIGURATION

To configure it go to config.example.properties and add the token and userId.
copy this properties and then rename it to config.properties.

# RUNNING

This has a void main to run the program.
Slack allows only 100 files to be deleted at the same time.
So if you have more than 100 files run the program several times.
