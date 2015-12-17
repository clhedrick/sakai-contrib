Asnn2 Selenium Tests
====================

Running the Tests
-----------------

Currently the tests require you to login in as Instructor before running 
the test, but that will change eventually.

1. Install Selenium in Firefox from here http://selenium-ide.openqa.org/download.jsp
   Currently we are using Version 1.0 Beta 2
#. Login to Sakai as an Instructor and navigate to the Assignments 2 tool on a 
   site in the portal. Open the source of that page.
#. cd asnn2/selenium-test
#. Open asnn2_selenium_setup.py. Copy the /portal/* portion of the Assignments2 page
   into the new_asnn2_page variable so it looks like the current_asnn2_page variable.
   Search the page source for 'iframe' and copy the name/id of the iframe Main* id
   into new_asnn2_mainframe so it looks like current_asnn2_mainframe.
#. Run './asnn2_selenium_setup.py' at the command line while inside the selenium-tests
   directory. You should see it say that it fixed all the .html files. If it's not
   executable for some reason, use 'python ./asnn2_selenium_setup.py'
#. In firefox, still logged in as Instructor, open Selenium IDE from
   'Tools -> Selenium IDE'
#. Choose 'File -> Open Test Suite...' and open 'suite_assignments2.html' 
#. Click the 'Play Entire Test Suite' button. It's right next to the Fast-Slow
   Slider on the toolbar.
#. Hopefully it works. You'll see the Runs and Failures as they occur.

Instructor Tests
----------------

1. Test validation for no title
2. Create Assn with Due Date
3. Create Assn that is graded
4. Create Assn that is NOT graded
5. Create Assn with formatted instructions
6. Create Assn with no instructions
7. Create Assn with attachments
8. Create Assn with no attachments
9. Create Assn that requires honor pledge
