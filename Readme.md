# FileMaker API - Video Game Sales

An Android app to learn how to interact with the FileMaker Server 17 Data API.

### Screenshots

* [MainActivity](https://raw.githubusercontent.com/joselopezrosario/filemaker-api-vgsales/master/project-screenshots/MainActivity.png)
* [SearchActivity](https://raw.githubusercontent.com/joselopezrosario/filemaker-api-vgsales/master/project-screenshots/SearchActvitiy.png)

## Data Source

I created the FileMaker database from a [Kaggle dataset on video game sales](https://www.kaggle.com/gregorut/videogamesales).

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* [Git](https://git-scm.com/)
* [Android Studio 3.2.1](https://developer.android.com/studio/install)
* FileMaker Server 17
* [VideoGameSales.fmp12](https://github.com/joselopezrosario/filemaker-api-vgsales/raw/master/filemaker-database/VideoGameSales.zip) database


### Installing

1. Install Android Studio
2. Clone this repository in Android Studio
3. Host the VideoGameSales database in FileMaker Server - you can use a local server
4. Enable the FileMaker Data API
5. Modify the ENDPOINT, ACCOUNTNAME and PASSWORD variables in the [FMApi.java](https://github.com/joselopezrosario/filemaker-api-vgsales/blob/master/app/src/main/java/com/joselopezrosario/vgsales/filemaker_api_vgsales/api/FMApi.java) class to use your own info

### Warning

This project uses a custom TrustManager that allows Java to communicate with unverified SSL certificates to run API calls with the default certificate provided by FileMaker. Eventually, I will make this feature optional.

## Running the tests

From the command line run `gradlew test`

