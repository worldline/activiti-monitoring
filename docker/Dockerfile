FROM tomcat:7

RUN curl -SL https://github.com/Activiti/Activiti/releases/download/activiti-5.17.0/activiti-5.17.0.zip -o activiti.zip \
	&& unzip activiti.zip \
	&& rm activiti.zip \
	&& mkdir webapps/activiti-explorer \
	&& cd webapps/activiti-explorer \
	&& unzip ../../activiti-5.17.0/wars/activiti-explorer.war \
	&& rm -rf ../../activiti-5.17.0

ADD db.properties /usr/local/tomcat/webapps/activiti-explorer/WEB-INF/classes/
ADD ojdbc6.jar /usr/local/tomcat/lib/

CMD sleep 20 && catalina.sh run

