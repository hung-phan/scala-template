#!/usr/bin/env bash

sbt -Denv=Production webpackProTask
sbt -Denv=Production docker:publishLocal
