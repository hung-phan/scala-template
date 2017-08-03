#!/usr/bin/env bash

if [ -z "${NODE_ENV}" ]; then
  export NODE_ENV=development
fi

if [ -z "${RUN_MODE}" ]; then
  export RUN_MODE=es
fi
