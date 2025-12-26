pipeline {
    agent any
    
    environment {
        MAVEN_HOME = tool 'Maven'
        PATH = "${MAVEN_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code...'
                checkout scm
            }
        }
        
        stage('Build Service Registry') {
            steps {
                echo 'Building Service Registry...'
                dir('flightapp-service-registry') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Build Identity Service') {
            steps {
                echo 'Building Identity Service...'
                dir('flightapp-identity-service') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Build API Gateway') {
            steps {
                echo 'Building API Gateway...'
                dir('flightapp-api-gateway') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Build Flight Service') {
            steps {
                echo 'Building Flight Service...'
                dir('flightapp-flight-service') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Build Booking Service') {
            steps {
                echo 'Building Booking Service...'
                dir('flightapp-booking-service') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Build Notification Service') {
            steps {
                echo 'Building Notification Service...'
                dir('flightapp-notification-service') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Build Config Server') {
            steps {
                echo 'Building Config Server...'
                dir('config-server') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Deploy with Docker Compose') {
            steps {
                echo 'Stopping existing containers...'
                sh 'docker-compose down || true'
                
                echo 'Starting services with Docker Compose...'
                sh 'docker-compose up -d --build'
                
                echo 'Waiting for services to start...'
                sh 'sleep 30'
                
                echo 'Checking service status...'
                sh 'docker-compose ps'
            }
        }
        
        stage('Verify Deployment') {
            steps {
                echo 'Verifying services are running...'
                script {
                    def services = [
                        'Service Registry': 'http://localhost:8761',
                        'API Gateway': 'http://localhost:9000'
                    ]
                    
                    services.each { name, url ->
                        retry(3) {
                            sleep 10
                            sh "curl -f ${url}/actuator/health || exit 1"
                            echo "${name} is healthy"
                        }
                    }
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline executed successfully!'
            echo 'Services are running. Access points:'
            echo '- Eureka Dashboard: http://localhost:8761'
            echo '- API Gateway: http://localhost:9000'
        }
        failure {
            echo 'Pipeline failed! Cleaning up...'
            sh 'docker-compose down || true'
        }
        always {
            echo 'Archiving build artifacts...'
            archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
        }
    }
}