# 镜像地址
# registry.cn-shanghai.aliyuncs.com/techservice/hybm

FROM registry.cn-shanghai.aliyuncs.com/techservice/builder-jhipster as builder
# vpn network
# FROM registry-vpc.cn-shanghai.aliyuncs.com/techservice/builder-jhipster as builder
ADD . /code/

RUN \
    cd /code/ && \
    rm -Rf build node_modules .gradle && \
    chmod +x /code/gradlew && \
    sleep 1 && \
    ./gradlew -Pprod clean bootWar && \
    mv /code/build/libs/*.war /app.war && \
    apt-get clean && \
    rm -Rf /code/ /root/.m2 /root/.cache /tmp/* /var/lib/apt/lists/* /var/tmp/*


FROM registry.cn-shanghai.aliyuncs.com/techservice/8-jre-alpine-tswx:1.0
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="" \
    SPRING_PROFILES_ACTIVE=prod \
    WX="/opt/tswx/zssss.p12"

## set timezone
ENV TZ=Asia/Shanghai
# RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8080
RUN \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone &&\
    mkdir /target && \
    chmod g+rwx /target &&\
    mkdir /data &&\
    chmod g+rwx /data

CMD java \
        ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom \
        -jar /app.war

COPY --from=builder /app.war .


