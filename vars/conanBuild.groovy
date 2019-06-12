// Usage: conanBuild { reference: "Qt/5.9.2" }

def call(String reference,
  Boolean clang = true, Boolean gcc = true, Boolean win = true, Boolean mac = true) {
  pipeline {
    agent none
    options {
      ansiColor('xterm')
      timestamps()
    }
    parameters {
      booleanParam(name: 'clang', defaultValue: clang)
      booleanParam(name: 'gcc', defaultValue: gcc)
      booleanParam(name: 'win', defaultValue: win)
      booleanParam(name: 'mac', defaultValue: mac)
    }
    stages {
      stage('Build') {
        parallel {
          // ************************** glang7 *********************************
          stage('clang7') {
            when {
              beforeAgent true
              expression { return params.clang }
            }
            agent {
              docker {
                image 'ogs6/conan_clang7'
                label 'docker'
                args '--security-opt seccomp:unconfined'
                alwaysPull true
              }
            }
            environment {
              CONAN_REFERENCE = "${reference}"
              JFROG = credentials('3a3e2a63-4509-43c9-a2e9-ea0c50fdcd4c')
              CONAN_USERNAME = "bilke"
              CONAN_UPLOAD = "https://ogs.jfrog.io/ogs/api/conan/conan"
              CONAN_STABLE_BRANCH_PATTERN = "release/*|stable/*"
              CONAN_CLANG_VERSIONS = "7.0"
              CONAN_USER_HOME = "$WORKSPACE/conan"
              CONAN_ARCHS = "x86_64"
              CONAN_UPLOAD_RETRY = "5"
              CONAN_BUILD_POLICY = "outdated"
            }
            steps {
              sh "rm -rf $WORKSPACE/conan"
              sh "CONAN_LOGIN_USERNAME=$JFROG_USR CONAN_PASSWORD=$JFROG_PSW python build.py"
              sh "rm -rf $WORKSPACE/conan"
            }
          }
          // *************************** gcc7 ************************************
          stage('gcc7') {
            when {
              beforeAgent true
              expression { return params.gcc }
            }
            agent {
              docker {
                image 'ogs6/conan_gcc7'
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
              CONAN_GCC_VERSIONS = "7"
              CONAN_USER_HOME = "$WORKSPACE/conan"
              CONAN_ARCHS = "x86_64"
              CONAN_UPLOAD_RETRY = "5"
              CONAN_BUILD_POLICY = "outdated"
            }
            steps {
              sh "rm -rf $WORKSPACE/conan"
              sh "CONAN_LOGIN_USERNAME=$JFROG_USR CONAN_PASSWORD=$JFROG_PSW python build.py"
              sh "rm -rf $WORKSPACE/conan"
            }
          }
          // *************************** gcc8 ************************************
          stage('gcc8') {
            when {
              beforeAgent true
              expression { return params.gcc }
            }
            agent {
              docker {
                image 'ogs6/conan_gcc8'
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
              CONAN_GCC_VERSIONS = "8"
              CONAN_USER_HOME = "$WORKSPACE/conan"
              CONAN_ARCHS = "x86_64"
              CONAN_UPLOAD_RETRY = "5"
              CONAN_BUILD_POLICY = "outdated"
            }
            steps {
              sh "rm -rf $WORKSPACE/conan"
              sh "CONAN_LOGIN_USERNAME=$JFROG_USR CONAN_PASSWORD=$JFROG_PSW python build.py"
              sh "rm -rf $WORKSPACE/conan"
            }
          }
          // *************************** gcc9 ************************************
          stage('gcc9') {
            when {
              beforeAgent true
              expression { return params.gcc }
            }
            agent {
              docker {
                image 'ogs6/conan_gcc9'
                label 'docker'
                args '-v /home/jenkins/.ccache:/usr/src/.ccache --security-opt seccomp:unconfined'
                alwaysPull true
              }
            }
            environment {
              CONAN_REFERENCE = "${reference}"
              JFROG = credentials('3a3e2a63-4509-43c9-a2e9-ea0c50fdcd4c')
              CONAN_USERNAME = "bilke"
              CONAN_UPLOAD = "https://ogs.jfrog.io/ogs/api/conan/conan"
              CONAN_STABLE_BRANCH_PATTERN = "release/*|stable/*"
              CONAN_GCC_VERSIONS = "9"
              CONAN_USER_HOME = "$WORKSPACE/conan"
              CONAN_ARCHS = "x86_64"
              CONAN_UPLOAD_RETRY = "5"
              CONAN_BUILD_POLICY = "outdated"
            }
            steps {
              sh "rm -rf $WORKSPACE/conan"
              sh "CONAN_LOGIN_USERNAME=$JFROG_USR CONAN_PASSWORD=$JFROG_PSW python build.py"
              sh "rm -rf $WORKSPACE/conan"
            }
          }
          // ************************** vs2017 ***********************************
          stage('vs2017') {
            when {
              beforeAgent true
              expression { return params.win }
            }
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
              CONAN_UPLOAD_RETRY = "5"
              CONAN_BUILD_POLICY = "outdated"
            }
            steps {
                  bat "conan user"
                  bat """
                  set CONAN_LOGIN_USERNAME=%JFROG_USR%
                  set CONAN_PASSWORD=%JFROG_PSW%
                  python build.py"""
                  bat 'rd /S /Q %CONAN_USER_HOME%'

            }
          }
          // ************************** macos ***********************************
          stage('macos') {
            when {
              beforeAgent true
              expression { return params.mac }
            }
            agent { label 'mac' }
            environment {
              CONAN_REFERENCE = "${reference}"
              JFROG = credentials('3a3e2a63-4509-43c9-a2e9-ea0c50fdcd4c')
              CONAN_USERNAME = "bilke"
              CONAN_UPLOAD = "https://ogs.jfrog.io/ogs/api/conan/conan"
              CONAN_STABLE_BRANCH_PATTERN = "release/*|stable/*"
              CONAN_USER_HOME = "$WORKSPACE/conan"
              CONAN_APPLE_CLANG_VERSIONS = "10.0"
              CONAN_UPLOAD_RETRY = "5"
              CONAN_BUILD_POLICY = "outdated"
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
