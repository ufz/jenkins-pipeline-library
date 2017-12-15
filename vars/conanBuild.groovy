def call(body) {
  // evaluate the body block, and collect configuration into the object
  def pipelineParams= [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = pipelineParams
  body()

  def compilerId = pipelineParams.compiler + pipelineParams.compiler_version

  agent {
    docker {
      image 'ogs6/conan' + compilerId
      label 'docker'
      args '-v /home/jenkins/.ccache:/usr/src/.ccache'
      alwaysPull true
    }
  }
  environment {
    CONAN_REFERENCE = pipelineParams.reference
    JFROG = credentials('3a3e2a63-4509-43c9-a2e9-ea0c50fdcd4c')
    CONAN_USERNAME = "bilke"
    CONAN_CHANNEL = "testing"
    CONAN_UPLOAD = "https://ogs.jfrog.io/ogs/api/conan/conan"
    CONAN_STABLE_BRANCH_PATTERN = "release/*"
    CONAN_GCC_VERSIONS = pipelineParams.compiler_version.toString()
  }
  steps {
    script {
      withEnv(['CONAN_LOGIN_USERNAME=$JFROG_USR', 'CONAN_PASSWORD=$JFROG_PSW']) {
        sh 'sudo ./.travis/install.sh'
        sh 'conan user'
        sh './.travis/run.sh'
      }
    }
  }
}
