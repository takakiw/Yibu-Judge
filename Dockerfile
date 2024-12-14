FROM openjdk:17-jdk-slim

WORKDIR /app

RUN sed -i 's@/deb.debian.org/@/ftp.cn.debian.org/@g' /etc/apt/sources.list && \
    sed -i 's@/security.debian.org/@/ftp.cn.debian.org/@g' /etc/apt/sources.list && \
    apt-get update && \
    apt-get install -y libseccomp-dev gcc g++ python3 tzdata && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apt-get clean && rm -rf /var/lib/apt/lists/* && mkdir /usr/lib/judger

COPY ./target/Yibu-Judge.jar /app/Yibu-Judge.jar
COPY ./libjudger/libjudger.so /usr/lib/judger/libjudger.so

VOLUME ["/judge/file", "/judge/log"]

EXPOSE 8080

CMD ["java", "-Duser.timezone=Asia/Shanghai", "-Djava.library.path=/usr/lib/judger", "-jar", "Yibu-Judge.jar"]
