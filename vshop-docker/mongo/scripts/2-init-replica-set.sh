#!/bin/bash

retryCount=10
readonly secondsBeforeAnotherRetry=2

function tryInitiateReplicaSet() {
  echo "Try to initiate replica set. Trial countdown: $retryCount"

  # try to initiate replica set using mongosh command
  # if mongo not yet available we will get "MongoNetworkError: connect ECONNREFUSED 127.0.0.1:27017" in the console
  initResult=$(mongosh --quiet -p root -u root admin --eval 'rs.initiate().ok' || :)

  if [[ $initResult == 1 ]]; then
    echo "Successfully initiated replica set"
    exit 0
  fi

  # little pause before next try
  sleep $secondsBeforeAnotherRetry

  retryCount=$((retryCount - 1))
  if [ $retryCount == 0 ]; then
    echo "Failed to initiate replica set. Giving up."
    exit 0
  fi

  # retry
  tryInitiateReplicaSet
}

# start replica set initiation (async as we don't want to block mongo container)
tryInitiateReplicaSet &
