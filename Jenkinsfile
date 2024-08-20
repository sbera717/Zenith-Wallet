pipeline{
    agent{
        docker{
            image 'sbera717/dockeragent:v2'
            args '--user root -v /var/run/docker.sock:/var/run/docker.sock'
        }
    }
    stages{
        stage('Checkout'){
            steps{
                sh 'echo passed'
            }
        }
        stage("Build"){
            steps{
                sh 'mvn clean package'
            }
        }
        stage('Code Analysis'){
            environment{
                SONAR_HOST_URL = 'http://34.130.111.207:9000'
            }
            steps{
                withCredentials([string(credentialsId: 'sonarqube', variable: 'SONAR_AUTH_TOKEN')]) {
                sh 'mvn sonar:sonar -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.token=${SONAR_AUTH_TOKEN}'
                }
            }
        }
        stage('Build and Push Docker Image') {
      environment {
        DOCKER_IMAGE = "sbera717/user-service:${BUILD_NUMBER}"
        REGISTRY_CREDENTIALS = credentials('docker-login')
      }
      steps {
        script {
            sh 'docker build -t ${DOCKER_IMAGE} .'
            def dockerImage = docker.image("${DOCKER_IMAGE}")
            docker.withRegistry('https://index.docker.io/v1/', "docker-login") {
                dockerImage.push()
            }
            sh 'docker rmi ${DOCKER_IMAGE}'
        }
      }
    }
       stage('Mark Safe Directory') {
        steps {
            script {
            def workspaceDir = pwd()
            sh "git config --global --add safe.directory ${workspaceDir}"
        }
      }
    }

        stage('Update Deployment File') {
        environment {
            GIT_REPO_NAME = "Zenith-Wallet"
            GIT_USER_NAME = "sbera717"
        }
        steps {
            withCredentials([string(credentialsId: 'github', variable: 'GITHUB_TOKEN')]) {
                sh '''
                    git status
                    git config user.email "sbera717@gmail.com"
                    git config user.name "Subrat Bera"
                    sed -i "s/tag: .*/tag: ${BUILD_NUMBER}/" user-chart/values.yaml
                    git add user-chart/values.yaml
                    git commit -m "Update values in Helm chart to version ${BUILD_NUMBER}"
                    git push https://${GITHUB_TOKEN}@github.com/${GIT_USER_NAME}/${GIT_REPO_NAME} HEAD:User-Service
                '''
            }
        }
    }
    }
}