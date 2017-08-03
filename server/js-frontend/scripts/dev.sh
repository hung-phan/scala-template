#!/bin/bash

source $(dirname "$0")/env/development.sh
npm run clean
webpack-dev-server --config config/development.js
