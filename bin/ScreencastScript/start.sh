#! /usr/bin/bash
outputPath=$2
dateTime=`date +'%d_%m_%y_%H_%M'`
filename="$1_${dateTime}.wmv"
$FFMPEG_PATH/ffmpeg.exe -rtbufsize 1024M -video_size 1920x1080 -framerate 30 -t 00:07:00 -f dshow -i video="screen-capture-recorder" -b:v 4000k "${TEMP}/${filename}"
if [ "$3" = "no-convertation" -a -n "$3" ] 
then
	mv "${TEMP}/${filename}" "${outputPath}/${filename}"
else
	$FFMPEG_PATH/ffmpeg.exe -i "${TEMP}/${filename}" -s 720x576 -c:a copy -b:v 3000k "${outputPath}/${filename}"
	rm "${TEMP}/${filename}"
fi