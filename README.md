# EJB on Liberty Stack

A minimal, runnable  stack that can be used as a base for developing EJB applications on liberty. This stack provides a sample application template with 3 modules, db2 connection which can be replaced with other datasources, along with all the settings in Dockerfiles and manifests to allow you to use the Appsody CLI to test out init/run/debug/test/build/deploy .

Use appsody init to initialize
appsody build to build the image
appsody deploy --tag <<TAG>>:latest --push-url $IMAGE_REGISTRY --push --pull-url $PULL_URL --namespace <<NAMESPACE>>  to deploy application on OCP cluster 

Note: To run it locally instead of OCP cluster, Currently appsody RUN is not functioning as expected. Instead use 
a>appsody build , get the docker final image of application generated as a outcome of "appsody build" and
b>docker run -p 9080:9080 <<docker-final-image>> to run it locally
