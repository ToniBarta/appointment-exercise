# Denteo

I have created a primitive command line booking system. 

In order to run the program please run the _AppointmentMain_ class.

This will give you a small menu where you can do the following:

* view the current bookings
* view the free slots
* book a new slot


I have decided to return an array of _from_ and _to_ objects that contains the whole block 
of free time and this is because I was not sure if we are only going to have 30-min sessions,
1h session or even 15-min sessions. Knowing the free slots range we can split that even further
based on new requirements. In the end the bulk of logic to give us the information is there.

### Short history of my train of thought

After I know the search range I get back a list of only the booked appointments form that rage.

Next step is that I wanted to know how many appointments are in a day, so I will not have a more 
complex logic later to find this out, so I created a Map of Day and List of appointments. 

Iterating through the keys of the map I wanted to check if there is only one appointment then
it has to go trough a couple of special cases. (maybe there is a better solution for this rather
than a bunch of if cases)

If there are more appointments in a day then I want to iterate trought it and always check
the current appointment with the next one and based on that figure out the empty slots. 
In order to make my life easier I decided to add the LUNCH timeslot as a booking. 

###  Things that can be improved
* more input validation
* when booking a new timeslot another validation to check if the time selected fits the range
* whe booking a new slot double check if that slot is not already taken
* when starting the app I wanted to create a list of random appointments starting from today.
* also add a validation to check if the booking dates are not over the weekend. 

### Things that could have been done different
1. We could use a spring framework to create a REST API that would have a couple of operations, but
in the end the bulk of logic was similar with what I did.
    * GET current bookings
    * GET available slots
    * POST a new booking
    * DELETE a booking
    * PUT update a booking to a different times




