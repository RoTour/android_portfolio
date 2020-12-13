Implemented features:
  - Data fetch from Firebase Firestore
  - Courses are displayed by weeks using RecyclerViews and ViewPager2
  - Possibility of adding new courses intuitively (I hope) and quickly (to be upgraded)
  - Supports DarkTheme
  
Todo:
  - update weeks when a course has been edited/added/deleted
  - Handle onFailure cases
  - add tests
  - add true authentification
  - Upgrade UI ==> Themes and courses (time not displayed, using default colors...)
  - add gradebook system
  

Current bugs:
  - When navigating up from AddCourseFragment, the displayed week isn't the one expected
