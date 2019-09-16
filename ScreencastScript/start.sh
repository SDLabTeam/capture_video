#! /usr/bin/bash
username=asem
outputPath=./
./ffmpeg.exe -rtbufsize 1024M -video_size 1920x1080 -framerate 30 -t 00:07:00 -f dshow -i video="screen-capture-recorder" -b:v 4000k input.wmv
dateTime=`date +'_%d_%m_%y_%H'`
./ffmpeg.exe -i input.wmv -s 720x576 -c:a copy -b:v 3000k "${outputPath}${username}${dateTime}.wmv"
rm input.wmv