FROM ubuntu:bionic
MAINTAINER Savarious
USER root

RUN apt-get update
#RUN apt-get install -y nodejs npm
RUN apt-get install -y git
RUN apt-get install -y maven
RUN apt-get update
RUN apt-get install -y --fix-missing default-jre 
RUN apt-get install -y default-jdk

RUN git clone https://github.com/lambertrocher/EasyFlight.git

RUN apt-get install -y curl

RUN apt-get install -y wget

RUN wget https://raw.githubusercontent.com/creationix/nvm/v0.33.0/install.sh

RUN ./install.sh

RUN echo $NVM_DIR

RUN nvm install 7

RUN npm --version

WORKDIR "/EasyFlight/ionic4j"
#RUN npm install --loglevel verbose
WORKDIR "/EasyFlight/backend"
#RUN npm install --loglevel verbose

#CMD mvn