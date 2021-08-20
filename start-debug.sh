#!/bin/bash
chcp.com 65001
cd paper/debug/spigot
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -Xmx8G -Xms1G -jar server.jar
