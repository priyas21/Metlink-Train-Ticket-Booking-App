# Metlink-Train-Ticket-Booking-App

INTRODUCTION

Metlink M-ticket application allows the customers can book local train tickets in Wellington region through their mobile phones without going to the station and standing in the queue. Only registered users can book tickets. The booking is done through MTicket which has a unique id (Ticket number) and an SMS of ticket is send to the user for validation purpose. User can also view the ticket in the form of Barcode which will be scanned by ticket inspector at station. Your Smartphone is you ticket. Instead of printing the ticket, customers can travel with MTicket as an SMS or Barcode which is shown to ticket inspector before customers get on board. 
The information of user is stored in cloud server in PHP-MySQL database so that the user has access to his/her tickets and profile anytime and anywhere. The information about the train stations, their routes and fares are also stored on cloud server.  Customers can buy different types of tickets which include: Standard Ticket, Daily Pass, Monthly Pass, 10 trip pass. The application will show the monthly pass ticket booking module.


MODULES OF APPLICATION

1.	Splash Screen:  Before the application’s main page comes, a splash screen will show for 3 seconds using function sleep (3000) of Thread class.
2.	Login: Login module lets the user to login into his/her profile in order to book train tickets and view ticket history. User don’t need to login in order to view fare detail of a particular route selected.
3.	Register:  This is first step to book train ticket. User enter his personal information like name, email, mobile number, profile picture(optional) and password.  User can upload the profile pic either from camera or from gallery. All this information is stored on cloud database. This information in order to generate the ticket.
4.	Book Ticket: In booking module, user has to select the train line, source station and destination station, ticket type (monthly) and Passenger type (Adult / Child). Then next button is clicked to generate the ticket selected and monthly ticket module is opened.
5.	Monthly Ticket: This module will show the monthly ticket. User have to select the month for which he/she wants the ticket. User cannot book the monthly pass of current month. He can select any month from next six months. Then user clicks on pay button to go to payment module and complete the booking transaction.
6.	Payment Module: In this module user complete the payment of ticket via credit or debit card. After entering the valid details, alert dialog of successful payment is shown. SMS is send to user regarding the ticket details and expiry date of ticket after successful payment. Barcode is generated for each ticket booked. 
7.	Plan Journey: This module implements the Maps feature. User can enter the address and search it on Map. User can also change the type of map from normal to satellite. When a location is searched, this module has feature to zoom in and zoom out the searched location.
8.	View Ticket: User can view the tickets booked in this module. Tickets which are expired can be deleted. If the user tries to delete the ticket which are not yet expired, then an alert box will open to show that tickets cannot be deleted. 
9.	My Profile: After registering, user can view his/her account. In this module, user can update the mobile number and password. 


	FEATURES COMPLETED IN APPLICATION:

1.	Registration and Login: User can Register and login by putting valid details in order to book tickets and view ticket history.
2.	Update mobile number and password:  Mobile numbers’ and passwords can be updated after logging in to one’s profile.
3.	Delete expired Ticket: In the View ticket module, user can delete tickets which are expired. 
4.	Show fare of selected route in list view: User can select the Train line, source and destination stations to check the fare details. User need not to log in in order to check fare.
5.	SMS feature: Once the user has booked the ticket and done payment successfully, an SMS is send to his/her mobile which can be shown to the ticket inspector. 
6.	Camera implemented: User can upload his/her picture through camera or gallery of mobile. When user logs in, his profile picture is shown on main page.
7.	Stored the login details in SQLite Database: We have saved the log in details of user in SQLite database of user’s device in order to get faster access to one’s account. During the first time log in, the data is fetched from cloud server. 
8.	Validations: We have implemented following validation:
a.	Email must be valid
b.	Password must be 6 to 8 characters’ long
c.	Mobile number must be valid
d.	Source and destination stations cannot be same
e.	Card number must be of 16 digits.
f.	In user registration details, none of the fields can be empty.
g.	Deletion of ticket which is not yet expired is prohibited
9.	Maps: Google maps have been implemented. User can search the desired address. The map type can be change from normal to satellite. We can zoom in and zoom out the searched address.
10.	Shared Preferences: Shared preferences are the simplest mechanism to stored data in android. We have used shard preferences in this project to store the log in sessions. They store Key Value pairs. Value can be accessed later using key. 
11.	Barcode generation: When the ticket is booked by user, tickets can be viewed as a barcode. In the view ticket module, barcode for each ticket booked is generated with the ticket number as barcode number. Barcode can be scanned by Ticket inspector.

