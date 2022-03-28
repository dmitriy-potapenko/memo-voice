# android-codelab
Android project that serves as a base for code challenges implemented by applicants.
The base is written in kotlin. 

(!)If you have the NDK plugin installed, please disable it for the project, as errors may occur.

# Android Coding Challenges
Coding challenges are useful when the applicant does not provide a github repository or any work samples. Even if a github repository has been provided it is generally a good idea to give the applicant a task to solve and have him present his solution in a separate session. 

## General Instructions
The following instructions/conditions are valid independently of the actual coding challenge

- The code base has been tested with Android Studio 3.4.1 which is the recommended version, however feel free to try a higher version and adjust the configuration as needed
- The task should be implemented in kotlin
- Approach this task as if it was a real-world implementation - i.e. exactly how you would approach the task if you were working for a company
- 3rd party libraries may be used
- 3rd party libraries must be wrapped: They should be abstracted out, so any other library could be plugged into the solution
- The base project for this task will be provided by us
- Once completed, please send us your solution and presents it to us, followed by a discussion about the implementation and design decisions made
- The solution can be sent as a zip file or as a publicly accessible github/gitlub etc project link
- The solution sent to us must be complete, i.e. can be opened directly via Android Studio without additional configuration

## Voice Recognition
In this challenge the applicant has to implement voice recognition support, the following conditions are given:

- Implement voice recognition for the following two actions:
-- Create a new Memo > should open the "Create Memo" Activity
-- Switch the filter on the Homescreen to toggle between "Show All" and "Show Open" > toggling the filter should immediately update the UI accordingly
- Voice recognition should be implemented in a reusable way, i.e. with proper abstraction layers and the possibility to add further voice actions later on
- For now supporting English-language commands is sufficient