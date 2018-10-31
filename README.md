# bontouchDictionary
Bontouch Dictionary

The project about, the user can enter a word in a search bar,  all words contain a substring of the entered word showing with custom suggestions.

The application developed in Android Studio with Java. The app downloads the dictionary from  http://runeberg.org/words/ss100.txt to the internal storage. Then, app inserts these words to the database SQLite. To make it faster I have added words to the database with batches to make it faster. To make sure that the main UI thread of the application does not block database operation I have used Asynctask for inserting the retrieved data. While the insertion on progress app showing a dialog to the user to wait. When it finishes, the user can search. For the search, I have implemented a searchable activity and modified the searchable configuration XML and added into android manifest XML. To provide search suggestion,  I have created a content provider and requested suggestions from the content provider. Then, it calls to query, and it returns a cursor pointing to the suggestion from the database. 


Prerequisites

Android API level more than 24. 

Tested with Google Pixel 2.


Built With

Android Studio

Java

Author

Bengu Erenler

