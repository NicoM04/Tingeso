pipeline{
    agent any
    tools{
        gradle "gradle"

    }
    stages{
        stage("Build JAR File"){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/NicoM04/Tingeso']])
                dir("demo"){
                    bat "./gradlew clean build -x test"
                }
            }
        }
        stage("Test"){
            steps{
                dir("demo"){
                    bat "gradle test"
                }
            }
        }        
        stage("Build and Push Docker Image"){
            steps{
                dir("demo"){
                    script{
                         withDockerRegistry(credentialsId: 'docker-credentials') {
                            bat "docker build -t nicom04/backend-container ."
                            bat "docker push nicom04/backend-container"
                        }
                    }                    
                }
            }
        }
    }
}
