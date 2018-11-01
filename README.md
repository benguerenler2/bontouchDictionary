# bontouchDictionary
This is a simple dictionary application that let's the user search for the existence of a word.
It offers suggestions of words that start with the string the user has entered.

The application was developed in Android Studio, with Java. It downloads the dictionary from http://runeberg.org/words/ss100.txt everytime it is opened to maintain an up-to-date version of the dictionary. 
The user is shown a loading screen and is unable to perform any searches during this update.
The list of words is stored in an SQLite database and the search for matching words is done using SQL queries.
Insertions of the words into the database is done in batches and in a single transaction, which significantly increases the performance.
To provide search suggestions, Android's Search Widget was used and a simple Content Provider was implemented.
The content provider queries the database to find the matching words whenever the user changes the input by adding or deleting characters.
Clicking on one of the search suggestions simply reloads the search activity as the dictionary does not provide definitions for the words.

Prerequisites
Android 7.0 Nougat (API level 24).

Tested with Google Pixel 2.

Built With
Android Studio, Java

Author
Bengu Erenler

