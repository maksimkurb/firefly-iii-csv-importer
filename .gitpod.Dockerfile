FROM gitpod/workspace-full

ENV GRADLE_USER_HOME=/workspace/.gradle/

USER gitpod

RUN bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh && \
    sdk install java 17.0.4-ms && \
    sdk default java 17.0.4-ms"
