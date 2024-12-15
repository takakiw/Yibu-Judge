FROM ubuntu:22.04

# 设置工作目录
WORKDIR /app

# 更新镜像源并安装所需依赖
RUN sed -i 's@/archive.ubuntu.com/@/ftp.cn.debian.org/@g' /etc/apt/sources.list && \
    sed -i 's@/security.ubuntu.com/@/ftp.cn.debian.org/@g' /etc/apt/sources.list && \
    apt-get update && \
    apt-get install -y openjdk-17-jdk gcc g++ python3 tzdata libseccomp-dev && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apt-get clean && rm -rf /var/lib/apt/lists/* && mkdir /usr/lib/judger

# 复制项目文件
COPY ./target/Yibu-Judge.jar /app/Yibu-Judge.jar
COPY ./libjudger/libjudger.so /usr/lib/judger/libjudger.so

RUN chmod +x /usr/lib/judger/libjudger.so

# 设置卷
VOLUME ["/judge/file", "/judge/log"]

# 暴露端口
EXPOSE 8080

# 设置启动命令
CMD ["java", "-Duser.timezone=Asia/Shanghai", "-Djava.library.path=/usr/lib/judger", "-jar", "Yibu-Judge.jar"]