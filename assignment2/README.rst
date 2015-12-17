Assignments 2 
=======================================================

.. document notes and metadata are at the bottom

:Author: Damian Sobieralski, Steven Githens

.. contents::



Overview
========

Assignments 2 is a Sakai Module for flexible use of assignments and 
work in the classroom, and can be used as alternative to the out of the
box Assignments module in Sakai.

Important links:

- `A2 Confluence Home <https://confluence.sakaiproject.org/display/ASNN/Home>`_
- `A2 Jira Bug Tracking <https://jira.sakaiproject.org/browse/ASNN>`_
- `A2 Subversion Source <https://source.sakaiproject.org/contrib/assignment2>`_
- `This README <https://source.sakaiproject.org/contrib/assignment2/trunk/README.html>`_
- `A1/A2 Gap List <https://confluence.sakaiproject.org/display/ASNN/Gap+Analysis+of+Assignments+and+Assignments+2>`_



Installation
============


Compatibility
-------------

Compatible with Sakai branches 2.8.x, 2.9.x and 2.10.x/trunk


Prepare Your Environment
------------------------

MySQL
`````

If you are using MySQL as your backend database for Sakai do verify 
that you have your collation set to a case sensitive one (say utf8_bin). 
Many OS distributions of MySQL default the collation to utf_general_ci.  
Renaming of assignments is a case sensitive thing and we need to make 
sure that the database is using case sensitive searching. 

Tools needed
````````````

You will need to have maven2 or 3 installed on your system.

Building for 2.8.x
````````````````````````````````````````````

Use the sakai-2.8.x branch.


Building for 2.9.x
````````````````````````````````````````````

Use the sakai-2.9.x branch.

Building for 2.10.x/trunk
````````````````````````````````````````````
mvn clean install sakai:deploy


.. Integrations
.. ============

.. Assignments 2 has a number of integrations, displayed in the matrix below.

.. This section of documentation is in progress.

.. ===============    =====  =====  ============
.. Sakai Version      2.7.x  2.8.x  2.9.x(trunk)
.. ---------------    -----  -----  ------------
.. Assignment 2   
.. OSP Matrix
.. OSP Evaluations
.. Gradebook
.. Gradebook 2
.. Turnitin CRS

.. OSP Matrix Integration 
.. -----------------------

.. OSP Evaluations
.. ---------------

.. Gradebook
.. ---------

.. Gradebook 2
.. -----------

.. Turnitin Content Review Service
.. -------------------------------


.. This document is written in restructured text, and at the moment I'm using the
.. lsr.css stylesheet for the html output.
.. The following is the order for header depths: = - ` : . ' " ~ ^ _ * + #
