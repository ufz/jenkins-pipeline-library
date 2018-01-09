// Usage: conanBuild { reference: "Qt/5.9.2" }

def call(String reference) {

  pipeline {
    agent none
    options {
      ansiColor('xterm')
      timestamps()
    }
    stages {
      stage('Build') {
        parallel {
          // *************************** gcc49 ************************************
          stage('gcc49') {
            agent {
              docker {
                image 'ogs6/conangcc49'
                label 'docker'
                args '-v /home/jenkins/.ccache:/usr/src/.ccache'
                alwaysPull true
              }
            }
            environment {
              CONAN_REFERENCE = "${reference}"
              JFROG = credentials('3a3e2a63-4509-43c9-a2e9-ea0c50fdcd4c')
              CONAN_USERNAME = "bilke"
              CONAN_CHANNEL = "testing"
              CONAN_UPLOAD = "https://ogs.jfrog.io/ogs/api/conan/conan"
              CONAN_STABLE_BRANCH_PATTERN = "release/*"
              CONAN_GCC_VERSIONS = "4.9"
              CONAN_USER_HOME = "$WORKSPACE/conan"
            }
            steps {
              script {
                withEnv(['CONAN_LOGIN_USERNAME=$JFROG_USR', 'CONAN_PASSWORD=$JFROG_PSW']) {
                  sh 'sudo pip install conan_package_tools'
                  sh 'conan user'
                  sh("conan remove -f ${reference}")
                  sh 'python build.py'
                  sh 'rm -r $CONAN_USER_HOME'
                }
              }
            }
            post { always { cleanWs() } }
          }
          // *************************** gcc5 ************************************
          stage('gcc5') {
            agent {
              docker {
                image 'ogs6/conangcc5'
                label 'docker'
                args '-v /home/jenkins/.ccache:/usr/src/.ccache'
                alwaysPull true
              }
            }
            environment {
              CONAN_REFERENCE = "${reference}"
              JFROG = credentials('3a3e2a63-4509-43c9-a2e9-ea0c50fdcd4c')
              CONAN_USERNAME = "bilke"
              CONAN_CHANNEL = "testing"
              CONAN_UPLOAD = "https://ogs.jfrog.io/ogs/api/conan/conan"
              CONAN_STABLE_BRANCH_PATTERN = "release/*"
              CONAN_GCC_VERSIONS = "5"
              CONAN_USER_HOME = "$WORKSPACE/conan"
            }
            steps {
              script {
                withEnv(['CONAN_LOGIN_USERNAME=$JFROG_USR', 'CONAN_PASSWORD=$JFROG_PSW']) {
                  sh 'sudo pip install conan_package_tools'
                  sh 'conan user'
                  sh("conan remove -f ${reference}")
                  sh 'python build.py'
                  sh 'rm -r $CONAN_USER_HOME'
                }
              }
            }
            post { always { cleanWs() } }
          }
          // *************************** gcc6 ************************************
          stage('gcc6') {
            agent {
              docker {
                image 'ogs6/conangcc6'
                label 'docker'
                args '-v /home/jenkins/.ccache:/usr/src/.ccache'
                alwaysPull true
              }
            }
            environment {
              CONAN_REFERENCE = "${reference}"
              JFROG = credentials('3a3e2a63-4509-43c9-a2e9-ea0c50fdcd4c')
              CONAN_USERNAME = "bilke"
              CONAN_CHANNEL = "testing"
              CONAN_UPLOAD = "https://ogs.jfrog.io/ogs/api/conan/conan"
              CONAN_STABLE_BRANCH_PATTERN = "release/*"
              CONAN_GCC_VERSIONS = "6"
              CONAN_USER_HOME = "$WORKSPACE/conan"
            }
            steps {
              script {
                withEnv(['CONAN_LOGIN_USERNAME=$JFROG_USR', 'CONAN_PASSWORD=$JFROG_PSW']) {
                  sh 'sudo pip install conan_package_tools'
                  sh 'conan user'
                  sh("conan remove -f ${reference}")
                  sh 'python build.py'
                  sh 'rm -r $CONAN_USER_HOME'
                }
              }
            }
            post { always { cleanWs() } }
          }
          // *************************** gcc7 ************************************
          stage('gcc7') {
            agent {
              docker {
                image 'ogs6/conangcc7'
                label 'docker'
                args '-v /home/jenkins/.ccache:/usr/src/.ccache'
                alwaysPull true
              }
            }
            environment {
              CONAN_REFERENCE = "${reference}"
              JFROG = credentials('3a3e2a63-4509-43c9-a2e9-ea0c50fdcd4c')
              CONAN_USERNAME = "bilke"
              CONAN_CHANNEL = "testing"
              CONAN_UPLOAD = "https://ogs.jfrog.io/ogs/api/conan/conan"
              CONAN_STABLE_BRANCH_PATTERN = "release/*"
              CONAN_GCC_VERSIONS = "7"
              CONAN_USER_HOME = "$WORKSPACE/conan"
            }
            steps {
              script {
                withEnv(['CONAN_LOGIN_USERNAME=$JFROG_USR', 'CONAN_PASSWORD=$JFROG_PSW']) {
                  sh 'sudo pip install conan_package_tools'
                  sh 'conan user'
                  sh("conan remove -f ${reference}")
                  sh 'python build.py'
                  sh 'rm -r $CONAN_USER_HOME'
                }
              }
            }
            post { always { cleanWs() } }
          }
          // ************************** vs2017 ***********************************
          stage('vs2017') {
            agent {label 'win && conan' }
            environment {
              CONAN_REFERENCE = "${reference}"
              JFROG = credentials('3a3e2a63-4509-43c9-a2e9-ea0c50fdcd4c')
              CONAN_USERNAME = "bilke"
              CONAN_CHANNEL = "testing"
              CONAN_UPLOAD = "https://ogs.jfrog.io/ogs/api/conan/conan"
              CONAN_STABLE_BRANCH_PATTERN = "release/*"
              CONAN_VISUAL_VERSIONS = "15"
              CONAN_USER_HOME = "$WORKSPACE\\conan"
            }
            steps {
              script {
                withEnv(['CONAN_LOGIN_USERNAME=%JFROG_USR%', 'CONAN_PASSWORD=%JFROG_PSW%']) {
                  bat("conan remove -f ${reference}")
                  bat 'python build.py'
                  bat 'rd /S /Q %CONAN_USER_HOME%'
                }
              }
            }
            post { always { cleanWs() } }
          }
        } // end parallel
      }
    }
  } // end pipeline
}
