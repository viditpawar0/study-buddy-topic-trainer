pipeline {
	agent any

	tools {
		jdk 'jdk21'
	}

	stages {
		stage('Build') {
			steps {
				echo 'Building...'

				echo 'Building Jar file...'
				sh './gradlew bootJar'
				echo 'Jar build completed successfully'

				echo 'Building Docker image...'
				sh 'docker build -t viditpawar/study-buddy-topic-trainer --platform linux/amd64,linux/arm64 .'
				echo 'Docker image build completed successfully'
			}
		}

		stage('Deploy') {
			steps {
				echo 'Deploying...'

				echo 'Pushing Docker image to Docker Hub...'
				sh 'docker push viditpawar/study-buddy-topic-trainer'
				echo 'Docker image pushed to Docker Hub successfully'
			}
		}
	}
}
