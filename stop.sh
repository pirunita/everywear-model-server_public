#!/bin/bash

kill_process() {
  PID=`lsof -i TCP:$1 | grep -E "java|python" | grep LISTEN | awk '{print $2}'`
  if [[ "$PID"  != "" ]]; then
    kill -0 ${PID} && kill ${PID}
    kill -0 ${PID}; echo "PID ${PID} exit status: $?"  # success if return 0
  fi
}

# kill process if running on port 8090, 8095, 12222, 12223
kill_process 8090
kill_process 8095
kill_process 12222
kill_process 12223