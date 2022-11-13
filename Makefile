SHELL = /bin/bash

MAKEFILE_PATH := $(abspath $(lastword $(MAKEFILE_LIST)))
CURRENT_DIR := $(notdir $(patsubst %/,%,$(dir $(MAKEFILE_PATH))))

DOCKER_RUN := \
	docker run --rm -it \
    	-v maven-repos:/root/.m2 \
        -v $$(pwd):/workspace \
        -w /workspace

MAVEN_IMAGE := maven:3.8.6-openjdk-18

.PHONY: test maven
test: maven
	${DOCKER_RUN} ${MAVEN_IMAGE} mvn $@

.PHONY: unit-test
unit-test: build-classpath
	${DOCKER_RUN} ${MAVEN_IMAGE} \
		java \
		--class-path ${shell cat target/classpath.txt} \
		org.junit.platform.console.ConsoleLauncher \
		--scan-classpath

build-classpath: maven
	${DOCKER_RUN} ${MAVEN_IMAGE} mvn test-compile
	${DOCKER_RUN} ${MAVEN_IMAGE} mvn dependency:$@ -D'mdep.outputFile=target/classpath.txt'
	echo :/workspace/target/classes:/workspace/target/test-classes >> target/classpath.txt

maven:
ifeq ($(shell docker images -q ${MAVEN_IMAGE} 2> /dev/null),)
	docker pull ${MAVEN_IMAGE}
endif

.PHONY: clean
clean: maven
	${EXE_MAVEN} $@

.PHONY: compile-all
compile-all: maven
	${EXE_MAVEN} clean compile test-compile

cp: compile-all
	${EXE_MAVEN} dependency:build-classpath -Dmdep.outputFile=target/classpath

ut:
	docker run --rm -it \
        	-v maven-repos:/root/.m2 \
            -v $$(pwd):/workspace \
            -w /workspace \
            ${MAVEN_IMAGE} \
            java -jar /root/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.9.0/junit-platform-console-standalone-1.9.0.jar --classpath ${shell cat target/classpath}:target/classes:target/test-classes:target --scan-classpath

POSTGRES_IMAGE := postgres:latest
RUN_POSTGRES := \
	docker run --rm -d \
		-e POSTGRES_DB=PVS \
		-e POSTGRES_USER=pvs-backend \
		-e POSTGRES_PASSWORD=HappyTeamMem6er5LoveTar0t \
		-p 5432:5432 \
		-v postgres-data:/var/lib/postgresql/data \
		${POSTGRES_IMAGE}

.PHONY: database postgres
database: postgres
	${RUN_POSTGRES}

postgres:
ifeq ($(shell docker images -q ${POSTGRES_IMAGE} 2> /dev/null),)
	doker pull ${POSTGRES_IMAGE}
endif