# DIT3-1-IvanDelumen-Activty05

RefLection 

**1. How did you implement CRUD using SQLite?**

I implemented CRUD using the DatabaseHelper class in my app. To create a note, I used the insertNote() method that saves the note’s title, content, and date into the database. To read data, I used the getAllNotes() method which gets all notes from the database and shows them in order. To update a note, I used the updateNote() method that changes the selected note’s details. To delete, I used the deleteNote() method which removes a note by its ID and asks for confirmation before deleting.
   
**2. What challenges did you face in maintaining data persistence?**

The app keeps notes saved using an SQLite database handled by the DatabaseHelper class. The notes stay in the app even after closing it or restarting the phone. One challenge I faced was setting up the RecyclerView to show the notes properly, but I fixed it by creating a separate adapter and view holder. I also had to make sure the database was closed properly after use to avoid errors. Another challenge was using the same screen for adding and editing notes, which I solved by checking the note ID. I also made the dates look better using SimpleDateFormat.
   
**3. How could you improve performance or UI design in future versions?**

In the future, I can improve performance by using Room Database for faster data handling and adding search and pagination features. For the UI, I want to add categories, color tags, swipe-to-delete, and rich text formatting. I also plan to add features like image attachments, dark mode, sharing notes, and syncing across devices. These improvements will make the app easier and more fun to use.
