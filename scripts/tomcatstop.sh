ps -ef | grep your_process_name | grep -v grep | awk '{print $2}' | xargs kill
