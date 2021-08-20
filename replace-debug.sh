#!/bin/bash
chcp.com 65001
# Build
./gradlew :paper:build
# Copy the plugin into folder
cp paper/build/libs/*-all.jar paper/debug/spigot/plugins/
