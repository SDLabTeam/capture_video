### Required dependencies
* You need to download ffmpeg. It can be downloaded here https://ffmpeg.zeranoe.com/builds/. Then unpack it wherever you want
* Also you need to install screencapturer from https://sourceforge.net/projects/screencapturer/files/

### Configuring enviromnet variables
Firstly, some enviroment variables need to be initialized:
* FFMPEG_PATH - Must point to directory ffmpeg-<version info>/bin
* FTP_UPLOADER_USERS_DIRECTORY - path to directory with user folders
* JAVA_HOME - should point to root of your jdk/jre. In `JAVA_HOME/bin` should be `java.exe`
* FTPLoader_PATH - should point to a directory with FTPloader.jar

### Configuring screencast script
You need to specify path to "toUpload" folder as a second parameter of `start.sh` for each user in scripts:
* <username>_capture_screen.sh
* <username>_capture_screen_no_convertation.sh


### Downloading bash
Download files https://drive.google.com/open?id=1UYhGcm9sZQiZn1pTpS5QFY6NhVsn_Dpa to the directory with `start.sh` and other scripts

## Configuring task scheduler
1. Open Task Scheduler
1. Click 'Create Task'
1. Enter a name of the task
1. Go to tab 'Triggers'
1. Create new trigger
1. In block advanced setting select 'Repeat task every' and set '1 hour'. Then set duration 'Indefinetly'
1. Go to actions tab
1. Create new action
1. In drop-down list 'action' select 'Start a program'
1. Specify path to a `start_uploader.bat` 
1. Go to 'Conditions' tab
1. In section 'Power' disable checkboxes 'Stop if the computer swithces to battery power' and 'Start the task only if the computer is on AC power'
1. In section 'Power' set a checkbox 'Wake the computer to run this task'
1. Finish creating a task by clicking OK.
That's it.