pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        skipDefaultCheckout(true)
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Check Environment') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn -B -DskipTests clean compile'
            }
        }

        stage('Unit Test') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn -B -DskipTests package'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/assignment04-java-demo-1.0.0.jar', fingerprint: true
            }
        }

        stage('Publish to Local Staging') {
            steps {
                sh '''
                    set -eu
                    install -d "$JENKINS_HOME/assignment04-release"
                    cp target/assignment04-java-demo-1.0.0.jar "$JENKINS_HOME/assignment04-release/app.jar"
                    echo "Published: $JENKINS_HOME/assignment04-release/app.jar"
                '''
            }
        }
    }

    post {
        success {
            echo 'BUILD SUCCESSFUL: compile, tests, package and publish completed.'
        }
        failure {
            echo 'BUILD FAILED: open Console Output and inspect the failed stage.'
        }
    }
}
