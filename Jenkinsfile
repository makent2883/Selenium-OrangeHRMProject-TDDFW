pipeline{
	agent any
	
	tools{
		MAVEN_HOME '3.10.0'
	}
	
	stages{
		stage('Checkout'){
			steps{
				git branch: 'main', url:'https://github.com/makent2883/Selenium-OrangeHRMProject-TDDFW.git'
			}
		}
		stage('Build'){
			steps{
				bat 'mvn clean install'
			}
		}
		stage('Test'){
			steps{
				bat 'mvn test'
			}
		}
		stage('Reports'){
			steps{
				publishHTML(target:[
					reportDir:'src/test/resources/ExtentReport',
					reportFiles:'ExtentReport.html',
					reportName:'Extent Spark Report'
					])
				}
		}
	}
	
	post{  //this is to show reports on jenkins UI
		always{
			archiveArtifacts artifacts: '**/src/test/resources/ExtentReport/*.html', fingerprint: true
			junit 'target/surefire-reports/*.xml'
		}
		
		success{
			emailext (
				to: 'makent2883@gmail.com',
				subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
				body: """
				<html>
				<body>
				<p>Hello Team, </p>
				
				<p>The latest Jenkins build has completed.</p>
				
				<p><b>Project Name:</b> ${env.JOB_NAME}</p>
				<p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
				<p><b>Build Status::</b> <span style="color:green;"><b>SUCCESS</b></span></p>
				<p><b>Build URL:</b><a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
				
				<p><b>Last Commit:</b></p>
				<p>${env.GIT_COMMIT}</p>
				<p><b>Branch:</b> ${env.GIT_BRANCH}</p>
				
				<p><b>Build log is attached.</b></p>
				
				<p><b>Extent Report:</b> <a href="http://localhost:8080/job/OrangeHRM_Build/HTML_20Extent_20Report/">Click Here</a></p>
				
				<p>Best Regards, </p>
				<p><b>Automation Team</b></p>
				</body>
				</html>
				""",
				mimeType: 'text/html',
				attachLog: true
			)
		}
	}
}
