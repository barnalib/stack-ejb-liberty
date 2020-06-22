# EJB on Liberty Stack

A minimal, runnable  stack that can be used as a base for developing EJB applications on liberty. This stack provides a sample application template with 3 modules, db2 connection which can be replaced with other datasources, along with all the settings in Dockerfiles and manifests to allow you to use the Appsody CLI to test out init/run/debug/test/build/deploy .
##### Add this stack repo in your local using below command
appsody repo add appsody-ejb https://github.com/barnalib/stack-ejb-liberty/releases/download/latest/appsody-ejb-index.yaml

##### Use appsody init to initialize
default template
appsody init appsody-ejb/myejbstack 
or an alternate template
appsody init appsody-ejb/myejbstack test-ejb --overwrite

##### appsody build to build the image
appsody build
##### appsody deploy to deploy the image
appsody deploy --tag <<TAG>>:latest --push-url $IMAGE_REGISTRY --push --pull-url $PULL_URL --namespace <<NAMESPACE>>  to deploy application on OCP cluster 

Note: To run it locally instead of OCP cluster, Currently appsody RUN is not functioning as expected. Instead use 
###### a>appsody build , get the docker final image of application generated as a outcome of "appsody build" and
###### b>docker run -p 9080:9080 <docker-final-image> to run it locally
