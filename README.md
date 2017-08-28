# FIS Spring-Boot AMQ Producer

This project contains a FIS Spring-Boot application that connects to an A-MQ xPaaS message broker and use Camel to 
produce transacted or non-transacted messages.

### Building

The project can be built with

    mvn clean install

### Running the application in OpenShift

It is assumed that:
- OpenShift platform is already running, if not you can find details how to [Install OpenShift at your site](https://docs.openshift.com/container-platform/3.5/install_config/index.html) or setup a [local OpenShift cluster using the CDK](https://access.redhat.com/documentation/en-us/red_hat_container_development_kit/3.1/html/getting_started_guide/index)
- Your system is configured for Fabric8 Maven Workflow, if not you can find a [Get Started Guide](https://access.redhat.com/documentation/en/red-hat-jboss-middleware-for-openshift/3/single/red-hat-jboss-fuse-integration-services-20-for-openshift/)
- The Red Hat JBoss A-MQ xPaaS product should already be installed and running on your OpenShift installation, one simple way to run a A-MQ service is following the documentation of the A-MQ xPaaS image for OpenShift related to the `amq63-basic` template. Use the `ACTIVEMQ_SERVICE_NAME` environment variable on the deployment configuration to configure the service hosting the AMQ broker.

Then the following command will package your app and run it on OpenShift:

    mvn fabric8:deploy

To list all the running pods:

    oc get pods

Then find the name of the pod that runs this application, and output the logs from the running pods with:

    oc logs <name of pod>

You can also use the openshift [web console](https://docs.openshift.com/enterprise/3.1/getting_started/developers/developers_console.html#tutorial-video) to manage the
running pods, and view logs and much more.

### Integration Testing

The example includes a [fabric8 arquillian](https://github.com/fabric8io/fabric8/tree/v2.2.170.redhat/components/fabric8-arquillian) OpenShift Integration Test. 
Once the container image has been built and deployed in OpenShift, the integration test can be run with:

    mvn test -Dtest=*KT

The test is disabled by default and has to be enabled using `-Dtest`. Open Source Community documentation at [Integration Testing](https://fabric8.io/guide/testing.html) and [Fabric8 Arquillian Extension](https://fabric8.io/guide/arquillian.html) provide more information on writing full fledged black box integration tests for OpenShift. 

### Testing the application

The application can be tested from any web browser or using the `curl` and `HTTPie` tools.

	Usage:
	http://<host>:<service_port>/produceJmsMessage?[OPTIONS]
	Options:
	[destination         queue:.. | topic:..] - producer destination; mandatory
	[messageCount                          N] - number of messages to send; default 1000
	[messageSize                           N] - message size in bytes; default 5120 text message
	[msgTTL                                N] - message TTL in milliseconds; default 86400000
	[msgGroupID                           ..] - JMS message group identifier
	[persistent                 true | false] - use persistent or non persistent messages; default true
	[transacted                 true | false] - use transaction in sending JMS messages; default true
	[transactionBatchSize                  N] - use to send transaction batches of size N; default 10
	
	Example:
	http --timeout 120 'http://jms-service.192.168.99.100.nip.io/produceJmsMessage?destination=queue:TRANSACTED.TEST1&messageCount=3000&transactionBatchSize=100'