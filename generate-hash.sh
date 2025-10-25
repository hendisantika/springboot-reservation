#!/bin/bash
cd /Users/hendisantika/IdeaProjects/springboot-reservation
mvn exec:java -Dexec.mainClass="com.hendisantika.springbootreservation.util.PasswordHashGenerator" -Dexec.classpathScope=test -q
