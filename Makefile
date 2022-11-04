SHELL = /bin/bash

MAKEFILE_PATH := $(abspath $(lastword $(MAKEFILE_LIST)))
CURRENT_DIR := $(notdir $(patsubst %/,%,$(dir $(MAKEFILE_PATH))))

MAVEN_IMAGE := maven:3.8.6-openjdk-18
EXE_MAVEN := \
	docker run --rm -it \
    	-v maven-repos:/root/.m2 \
        -v $$(pwd):/workspace \
        -w /workspace \
        --entrypoint="mvn" \
        ${MAVEN_IMAGE}

.PHONY: test maven
test: maven
	${EXE_MAVEN} $@

maven:
ifeq ($(shell docker images -q ${MAVEN_IMAGE} 2> /dev/null),)
	docker pull ${MAVEN_IMAGE}
endif

.PHONY: clean
clean:
	docker image rm ${current_dir}:mvn
