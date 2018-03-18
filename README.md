# SSCMaster

__SSC Master__ aims to become a better __UBC Student Service Centre (SSC)__ for students. 

It parses SSC websites data for all ubc courses and renders them on Android phones. 
* You can check out a course description on this app
* You can search for a course on this app
* You can also be notified when a seat becomes available on this app!

This version is still under development, but the two previous versions of this app has achieved the same goal using different appoarches.
The development of this app basically reflected my software design learning progress. This time I started with UI design and backend abstraction.
So this version brings a much better UI (although is still not that impressive T.T) and a way cleaner backend management system.

Currently, the app has been able to render faculty, department, and course list views. 
__(Updated 3.18)__ Detailed course view is now supported! 
Section view as well as search view will be supported soon. 

### Main View
In SSC Master, courses are sorted based on their faculties and departments. This main view shows a faculty view of departments at UBC:

&nbsp;&nbsp;&nbsp;<img src="/screenshot/main_view.jpg" alt="Drawing" width="250" height="400" />

Clicking a faculty in the faculty view will bring you to all the departments in this faculty, and by clicking a department 
you can view all courses offered by this selected department:

&nbsp;&nbsp;&nbsp;<img src="/screenshot/department_view.jpg" alt="Drawing" width="250" height="400" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="/screenshot/course_view.jpg" alt="Drawing" width="250" height="400" />

### Course View
Choosing a course in courses list will open you a detailed course view. You can view course details as well as a section list. You also can search a particular section if you want, or search them based on some keywords you entered.
&nbsp;&nbsp;&nbsp;<img src="/screenshot/course_main_sample1.jpg" alt="Drawing" width="250" height="400" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="/screenshot/course_main_sample2.jpg" alt="Drawing" width="250" height="400" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="/screenshot/course_detail.jpg" alt="Drawing" width="250" height="400" />

&nbsp;&nbsp;&nbsp;<img src="/screenshot/section_search_sample1.jpg" alt="Drawing" width="250" height="400" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="/screenshot/section_search_sample2.jpg" alt="Drawing" width="250" height="400" />
