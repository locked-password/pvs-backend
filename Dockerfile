FROM maven:3.8.6-openjdk-18

WORKDIR /workspace
RUN --mount=target=/workspace \
    mvn dependency:resolve

VOLUME ["/workspace", "/root/.m2"]
ENTRYPOINT ["mvn"]
CMD ["--help"]
