#!/bin/bash

if [[ $# -eq 0 ]]
  then
    echo "1 argument required. (prod/dev)"
    exit 1
fi

kill_process() {
  PID=`lsof -i TCP:$1 | grep -E "java|python" | grep LISTEN | awk '{print $2}'`
  if [[ "$PID"  != "" ]]; then
    kill -0 ${PID} && kill ${PID}
    kill -0 ${PID}; echo "PID ${PID} exit status: $?"  # success if return 0
  fi
}

run_gmm() {
  cd gmm/$1
  eval "$(conda shell.bash hook)"  # conda (4.6+)
  conda activate pytorch
  nohup python -u test.py >> ${EW_SOCKET_GMM_LOG_PATH} &  # -u: avoid output buffering
  conda deactivate
  cd ../..
}

run_in_development_mode() {
  # run kafka
  docker-compose -f docker/docker-compose-kafka.yml up -d

  # run gmm socket server
  run_gmm 1001
  run_gmm 1102

  # run ew-synthesis server
  nohup sh -c 'export CUDA_VISIBLE_DEVICES=0 && ./gradlew -Dgpu.maximum-count=1 -Dgpu.visible-list="0" -Dlogging.config=$HOME/.everywear/logback.xml :ew-synthesis:bootRun' >> ${EW_SYNTHESIS_LOG_PATH} &

  # run ew-fitting server
  nohup ./gradlew :ew-fitting:bootRun >> ${EW_FITTING_LOG_PATH} 2>&1 < /dev/null &
  # 2>&1: redirect stderr to stdout
  # < /dev/null: use /dev/null as input stream
}

run_in_production_mode() {
  export GOOGLE_APPLICATION_CREDENTIALS=$HOME/.everywear/everywear-a7350-4459fdf270b6.json

  # run kafka
  docker-compose -f docker/docker-compose-kafka.yml up -d

  # jar packaging
  ./gradlew bootJar

  # run gmm socket server
  run_gmm 1001
  run_gmm 1102

  # run ew-synthesis server
  nohup sh -c 'export CUDA_VISIBLE_DEVICES=0 && java -jar -Dgpu.maximum-count=1 -Dgpu.visible-list="0" -Dlogging.config=$HOME/.everywear/logback.xml ew-synthesis/build/libs/ew-synthesis-1.0.0.jar' >> ${EW_SYNTHESIS_LOG_PATH} &

  # run ew-fitting server
  nohup java -jar -javaagent:$HOME/everywear-tools/pinpoint-agent/pinpoint-bootstrap-1.8.5.jar -Dpinpoint.agentId=ew-agent-1 -Dpinpoint.applicationName=ew-fitting ew-fitting/build/libs/ew-fitting-1.0.0.jar  >> ${EW_FITTING_LOG_PATH} &
}

# script absolute path
SCRIPT_PATH="$( cd "$(dirname "$0")" ; pwd -P )"

# log file path
TODAY=$(date '+%Y-%m-%d')
EW_SOCKET_GMM_LOG_PATH="${SCRIPT_PATH}/logs/$1-socket-gmm-${TODAY}.log"
EW_SYNTHESIS_LOG_PATH="${SCRIPT_PATH}/logs/$1-synthesis-${TODAY}.log"
EW_FITTING_LOG_PATH="${SCRIPT_PATH}/logs/$1-fitting-${TODAY}.log"

# create log file
mkdir -p logs
touch ${EW_SYNTHESIS_LOG_PATH}
touch ${EW_FITTING_LOG_PATH}

# kill process if running on port 8090, 8095, 12222, 12223
kill_process 8090
kill_process 8095
kill_process 12222
kill_process 12223

# run server in each mode
if [[ $1 = 'dev' ]]; then
  run_in_development_mode
else
  run_in_production_mode
fi

# print log to terminal
tail -n0 -f ${EW_SOCKET_GMM_LOG_PATH} ${EW_SYNTHESIS_LOG_PATH} ${EW_FITTING_LOG_PATH}