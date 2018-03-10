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

RUN chmod +x ./install.sh

RUN ./install.sh; export NVM_DIR="/root/.nvm"; [ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"; nvm install 7; node --version; npm --version; cd "/EasyFlight/ionic4j"; npm install --loglevel verbose; cd"/EasyFlight/backend"; npm install --loglevel verbose

ADD pass.sh /EasyFlight/pass.sh

RUN chmod +x pass.sh

CMD cd "/EasyFlight"; ./pass.sh; cd backend; chmod +x mvnw; ./mvnw