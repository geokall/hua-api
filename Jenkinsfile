pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Building docker image...'

                script {
                    sh 'docker build'
                }
            }
        }
    }
}