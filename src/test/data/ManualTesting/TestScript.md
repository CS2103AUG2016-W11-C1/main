# Test Script for V0.5 Demo 

1. Load 
  * **Scenario**: Load saved schedule file. 
  * **Input Command**: load src/test/data/manualtesting/sampledata.xml
  * **Expected Result**: Linenux reads input file and shows populated data. 

2. Freetime 
  * **Scenario**: Want to schedule a doctor's appointment for tomorrow.
  * **InputCommand**: freetime et/tomorrow 9.00pm
  * **ExpectedResult**: All time slots unoccupied by events from now until 9.00pm tomorrow are shown. 

3. Add 
  * **Scenario**: Schedule appointment at 3.00pm tomorrow. 
  * **Input Command**: add Doctorrs Appointment et/tomorrow 3.00pm #/Appointment
  * **Expected Result**: Input task shows up in "deadlines" list with specified details.

4. Edit 
  * **Scenario**: Realised that there was a typo. In addition, want to make it into a event. 
  * **Input Command**: edit Appointment n/Doctor's Appointment st/tomorrow 3.00pm et/tomorrow 3.30pm 
  * **Expected Result**: Task moves from "deadlines" list to "events" list, with new specified details.

5. Remind 
  * **Scenario**: Remind to bring relevant documents for doctor's appointment. 
  * **Input Command**: remind Appointment n/Bring medical records. t/tomorrow 1.00pm 
  * **Expected Result**: Reminder shows up in results box.

6. View
  * **Scenario**: Look at all details of task entered for doctor's appointment. 
  * **Input Command**: view Appointment
  * **Expected Result**: Information relevant to task shows up in results box, including tags and reminders. 

7. Today
  * **Scenario**: See what has to be done for today. 
  * **Input Command**: today
  * **Expected Result**: All todos, deadlines due before 11.59pm today, as well as events which start before today (even overdue ones) are shown. Reminders attached to these tasks are also shown.

8. Done
  * **Scenario**: v0.5 demo is done. 
  * **Input Command**: Done v0.5
  * **Expected Result**: Conflict over which task to mark as done. Correct task to be seleced. That task no longer appears in default 

9. List 
  * **Scenario**: Look at done tasks to ensure that newly marked task is there. 
  * **Input Command**: list d/yes
  * **Expected Result**: Only done tasks are displayed.  

10. Undone
  * **Scenario**: Realised that there is something which has been marked done, but hasn't been completed. 
  * **Input Command**: undone haven't 
  * **Expected Result**: Task moves to regular list view, and is removed from done list. 
  * **Follow-up Action**: Show that task has been removed from done list with "list d/yes".

11. Tomorrow
  * **Scenario**: See what has to be prepared for tomorrow. 
  * **Input Command**: tomorrow
  * **Expected Result**: All todos, deadlines which occur between 12.00am and 11.59pm tomorrow, as well as events which occur within this time frame are shown. Reminders attached to these tasks are also shown. 

12. Editr, Up/Down Key, "yes" Response to Callouts, Undo
  * **Scenario**: Come up with an idea of what gift to give. Change task.
  a. **Input Command**: eidtr get n/Get flowers for professor. 
    * **Expected Result**: Callout suggests "editr"
  b. **Input Command**: yes
    * **Expected Result**: Specified changes are made to the reminder. 
    * **Follow-up Action**: "undo" 
  c. **Input Command**: <up><up><modify editr to editr>
    * **Expected Result**: Specified changes are made to the reminder. 

13. Deleter
  * **Scenario**: Already placed preorder for book release next year. 
  * **Input Command**: deleter place
  * **Expected Result**: Specified reminder is deleted.

14. Clear
  * **Scenario**: Done tasks not needed, can be cleared. 
  * **Input Command**: clear
  * **Expected Result**: All done tasks are deleted from schedule. 
  * **Follow-up Action**: Execute "list d/yes" again to ensure that done tasks have been cleared. 

15. Undo 
  * **Scenario**: Realize it may be useful to keep record of done tasks. 
  * **Input Command**: undo
  * **Expected Result**: All done tasks reappear in schedule. 
  * **Follow-up Action**: Execute "list d/yes" again to ensure that done tasks are back in schedule. 

16. List 
  * **Scenario**: Imagine semester has ended, see what has been done over school year. 
  * **Input Command**: list d/all #/school 
  * **Expected Result**: All tasks (done or undone) that have the tag "school" are shown. Reminders attached to these tasks are also shown. 

17. Clear 
  * **Scenario**: Tasks are no longer needed, clear them. 
  * **Input Command**: clear #/school
  * **Expected Result**: All tasks with the tag "school" are deleted. Reminders attached to these tasks are deleted as well. 

18. Alias
  * **Scenario**: Up till now, keep using list command. Want to have a shorter and quicker alternative. 
  * **Input Command**: alias list l 
  * **Expected Result**: 'l' now acts the same way as the list command. 

19. Help 
  * **Scenario**: Forgot what has been sat as an alias. 
  * **Input Command**: help
  * **Expected Result**: All command formats shown, as well as aliases for all formats. 

20. Unalias
  * **Scenario**: Realise that "ad" is not very useful as an alias. 
  * **Input Command**: unalias ad
  * **Expected Result**: "ad" no longer functions as "add".

21. Information
  * **Scenario**: Want to see what is the location of the saved schedule file.
  * **Input Command**: information
  * **Expected Result**: ?Information about Linenux, including the version, working directory, and current save location is shown. 

22. Tab
  * **Scenario**: Want to save to current location, but troublesome to type out entire file location. 
  * **Input Command**: save s<tab><tab>
  * **Expected Result**: The full path name of the current folder appears in the command bar. Pressing enter will cause Linenux to prompt for permission to rewrite the file. Pressing yes will cause file to be saved to current location.