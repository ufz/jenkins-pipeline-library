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
          // ************************** glang39 ********************************
          stage('clang39') {
            agent {
              docker {
                image 'ogs6/conan_clang39'
                label 'docker'
                alwaysPull true
              }
            }
            environment {
              CONAN_REFERENCE = "${reference}"
              JFROG = credentials('3a3e2a63-4509-43c9-a2e9-ea0c50fdcd4c')
              CONAN_USERNAME = "bilke"
              CONAN_UPLOAD = "https://ogs.jfrog.io/ogs/api/conan/conan"
              CONAN_STABLE_BRANCH_PATTERN = "release/*|stable/*"
              CONAN_CLANG_VERSIONS = "3.9"
              CONAN_USER_HOME = "$WORKSPACE/conan"
              CONAN_ARCHS = "x86_64"
            }
            steps {
              sh "rm -rf $WORKSPACE/conan"
              sh 'sudo pip install --upgrade conan'
              sh 'sudo pip install conan_package_tools bincrafters-package-tools'
              sh 'conan user'
              sh "CONAN_LOGIN_USERNAME=$JFROG_USR CONAN_PASSWORD=$JFROG_PSW python build.py"
              sh "rm -rf $WORKSPACE/conan"
            }
          }
          // *************************** gcc49 ************************************
          stage('gcc49') {
            agent {
              docker {
                image 'ogs6/conan_gcc49'
                label 'docker'
                // args '-v /home/jenkins/.ccache:/usr/src/.ccache'
                alwaysPull true
              }
            }
            environment {
              CONAN_REFERENCE = "${reference}"
              JFROG = credentials('3a3e2a63-4509-43c9-a2e9-ea0c50fdcd4c')
              CONAN_USERNAME = "bilke"
              CONAN_UPLOAD = "https://ogs.jfrog.io/ogs/api/conan/conan"
              CONAN_STABLE_BRANCH_PATTERN = "release/*|stable/*"
              CONAN_GCC_VERSIONS = "4.9"
              CONAN_USER_HOME = "$WORKSPACE/conan"
              CONAN_ARCHS = "x86_64"
            }
            steps {
              sh "rm -rf $WORKSPACE/conan"
              sh 'sudo pip install --upgrade conan'
              sh 'sudo pip install conan_package_tools bincrafters-package-tools'
              sh 'conan user'
              sh "CONAN_LOGIN_USERNAME=$JFROG_USR CONAN_PASSWORD=$JFROG_PSW python build.py"
              sh "rm -rf $WORKSPACE/conan"
            }
          }
          // *************************** gcc5 ************************************
          stage('gcc5') {
            agent {
              docker {
                image 'ogs6/conan_gcc5'
                label 'docker'
                args '-v /home/jenkins/.ccache:/usr/src/.ccache'
                alwaysPull true
              }
            }
            environment {
              CONAN_REFERENCE = "${reference}"
              JFROG = credentials('3a3e2a63-4509-43c9-a2e9-ea0c50fdcd4c')
              CONAN_USERNAME = "bilke"
              CONAN_UPLOAD = "https://ogs.jfrog.io/ogs/api/conan/conan"
              CONAN_STABLE_BRANCH_PATTERN = "release/*|stable/*"
              CONAN_GCC_VERSIONS = "5"
              CONAN_USER_HOME = "$WORKSPACE/conan"
              CONAN_ARCHS = "x86_64"
            }
            steps {
              sh "rm -rf $WORKSPACE/conan"
              sh 'sudo pip install --upgrade conan'
              sh 'sudo pip install conan_package_tools bincrafters-package-tools'
              sh 'conan user'
              sh "CONAN_LOGIN_USERNAME=$JFROG_USR CONAN_PASSWORD=$JFROG_PSW python build.py"
              sh "rm -rf $WORKSPACE/conan"
            }
          }
          // *************************** gcc6 ************************************
          stage('gcc6') {
            agent {
              docker {
                image 'ogs6/conan_gcc6'
                label 'docker'
                args '-v /home/jenkins/.ccache:/usr/src/.ccache'
                alwaysPull true
              }
            }
            environment {
              CONAN_REFERENCE = "${reference}"
              JFROG = credentials('3a3e2a63-4509-43c9-a2e9-ea0c50fdcd4c')
              CONAN_USERNAME = "bilke"
              CONAN_UPLOAD = "https://ogs.jfrog.io/ogs/api/conan/conan"
              CONAN_STABLE_BRANCH_PATTERN = "release/*|stable/*"
              CONAN_GCC_VERSIONS = "6"
              CONAN_USER_HOME = "$WORKSPACE/conan"
              CONAN_ARCHS = "x86_64"
            }
            steps {
              sh "rm -rf $WORKSPACE/conan"
              sh 'sudo pip install --upgrade conan'
              sh 'sudo pip install conan_package_tools bincrafters-package-tools'
              sh 'conan user'
              sh "CONAN_LOGIN_USERNAME=$JFROG_USR CONAN_PASSWORD=$JFROG_PSW python build.py"
              sh "rm -rf $WORKSPACE/conan"
            }
          }
          // *************************** gcc7 ************************************
          stage('gcc7') {
            agent {
              docker {
                image 'ogs6/conan_gcc7'
                label 'docker'
                args '-v /home/jenkins/.ccache:/usr/src/.ccache'
                alwaysPull true
              }
            }
            environment {
              CONAN_REFERENCE = "${reference}"
              JFROG = credentials('3a3e2a63-4509-43c9-a2e9-ea0c50fdcd4c')
              CONAN_USERNAME = "bilke"
              CONAN_UPLOAD = "https://ogs.jfrog.io/ogs/api/conan/conan"
              CONAN_STABLE_BRANCH_PATTERN = "release/*|stable/*"
              CONAN_GCC_VERSIONS = "7"
              CONAN_USER_HOME = "$WORKSPACE/conan"
              CONAN_ARCHS = "x86_64"
            }
            steps {
              sh "rm -rf $WORKSPACE/conan"
              sh 'sudo pip install --upgrade conan'
              sh 'sudo pip install conan_package_tools bincrafters-package-tools'
              sh 'conan user'
              sh "CONAN_LOGIN_USERNAME=$JFROG_USR CONAN_PASSWORD=$JFROG_PSW python build.py"
              sh "rm -rf $WORKSPACE/conan"
            }
          }
          // ************************** vs2017 ***********************************
          stage('vs2017') {
            agent {label 'win && conan' }
            environment {
              CONAN_REFERENCE = "${reference}"
              JFROG = credentials('3a3e2a63-4509-43c9-a2e9-ea0c50fdcd4c')
              CONAN_USERNAME = "bilke"
              CONAN_UPLOAD = "https://ogs.jfrog.io/ogs/api/conan/conan"
              CONAN_STABLE_BRANCH_PATTERN = "release/*|stable/*"
              CONAN_VISUAL_VERSIONS = "15"
              CONAN_USER_HOME = "$WORKSPACE\\conan"
              CONAN_ARCHS = "x86_64"
            }
            steps {
              script {
                withEnv(['CONAN_LOGIN_USERNAME=%JFROG_USR%', 'CONAN_PASSWORD=%JFROG_PSW%']) {
                  bat 'rd /S /Q %CONAN_USER_HOME%'
                  sh "conan remove -f ${reference}"
                  sh "conan remote add upload_repo $CONAN_UPLOAD"
                  sh "conan user $JFROG_USR -p $JFROG_PSW -r upload_repo"
                  bat 'python build.py'
                  bat 'rd /S /Q %CONAN_USER_HOME%'
                }
              }
            }
          }
          // ************************** macos ***********************************
          stage('macos') {
            agent { label 'mac && conan' }
            environment {
              CONAN_REFERENCE = "${reference}"
              JFROG = credentials('3a3e2a63-4509-43c9-a2e9-ea0c50fdcd4c')
              CONAN_USERNAME = "bilke"
              CONAN_UPLOAD = "https://ogs.jfrog.io/ogs/api/conan/conan"
              CONAN_STABLE_BRANCH_PATTERN = "release/*|stable/*"
              CONAN_USER_HOME = "$WORKSPACE/conan"
              CONAN_APPLE_CLANG_VERSIONS = "9.1"
            }
            steps {
              sh "rm -rf $WORKSPACE/conan"
              sh 'conan user'
              sh "CONAN_LOGIN_USERNAME=$JFROG_USR CONAN_PASSWORD=$JFROG_PSW python3 build.py"
              sh "rm -rf $WORKSPACE/conan"
            }
          }
        } // end parallel
      }
    }
  } // end pipeline
}
