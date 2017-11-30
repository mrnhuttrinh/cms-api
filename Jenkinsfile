#!groovy

def appName = "ecash/cms-api"

node {
    def imageTag = "${env.DOCKER_REGISTRY_URL}/${appName}:latest"
    def containerName = "cms-api"

    println "Branch name = " + env.BRANCH_NAME

    stage("Checkout ${appName} Repository") {
      checkout scm
    }

    stage("Maven: mvn clean install") {
      sh "mvn clean package --settings auth_settings.xml -Dsettings.security=auth_settings-security.xml"
    }

    if (currentBuild.result == "UNSTABLE" || currentBuild.result == "FAILURE") {
        return this;
    }

    if ( env.BRANCH_NAME == "master" ) {
      withCredentials([usernamePassword(credentialsId: 'ecash-docker-login', passwordVariable: 'DOCKER_REPO_PASSWORD', usernameVariable: 'DOCKER_REPO_USERNAME')]) {
          stage("Build ${appName} docker image") {
              sh("docker build --rm=true -t ${imageTag} .")
              sh("docker login -e ${env.DOCKER_REGISTRY_EMAIL} -u $DOCKER_REPO_USERNAME -p $DOCKER_REPO_PASSWORD ${env.DOCKER_REGISTRY_URL}")
              sh("docker push ${imageTag}")
              sh("docker rmi ${imageTag} || true")
          }

          withCredentials([file(credentialsId: 'ecash_pem', variable: 'ECASH_PEM_FILE')]) {
            stage("Deploy ${appName} docker image") {
              sh("ssh -tt -i $ECASH_PEM_FILE ${env.CMS_UI_USER}@${env.CMS_UI_SERVER} docker login -e ${env.DOCKER_REGISTRY_EMAIL} -u $DOCKER_REPO_USERNAME -p $DOCKER_REPO_PASSWORD ${env.DOCKER_REGISTRY_URL}")
              sh("ssh -tt -i $ECASH_PEM_FILE ${env.CMS_UI_USER}@${env.CMS_UI_SERVER} docker pull ${imageTag}")
              sh("ssh -tt -i $ECASH_PEM_FILE ${env.CMS_UI_USER}@${env.CMS_UI_SERVER} docker rm -f ${containerName} || true")
              sh("ssh -tt -i $ECASH_PEM_FILE ${env.CMS_UI_USER}@${env.CMS_UI_SERVER} docker run --name=${containerName} --restart=on-failure:7 -d -t -p 8081:8080 ${imageTag}")
            }
          }
      }
    }
}
