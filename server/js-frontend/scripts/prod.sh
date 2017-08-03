#!/bin/bash

source $(dirname "$0")/env/production.sh
npm run clean
webpack --config config/production.js