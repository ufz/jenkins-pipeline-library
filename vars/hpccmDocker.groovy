def call(map, Closure body) {

  // is given to `pip install`
  if (!map.containsKey('hpccm_version'))
    map['hpccm_version'] = 'hpccm' // latest

  // hpccm --userarg options, see
  // https://github.com/NVIDIA/hpc-container-maker/blob/master/docs/tutorial.md#user-arguments
  if (!map.containsKey('options'))
    map['options'] = 'jenkins=True'
  else
    // Always append jenkins=True
    map['options'] = map['options'] + ' jenkins=True'

  // hpccm --recipe argument, see
  // https://github.com/NVIDIA/hpc-container-maker/blob/master/docs/recipes.md
  if (!map.containsKey('recipe'))
    map['recipe'] = 'recipe.py'

  // Working directory for creating the virtualenv and Dockerfile
  if (!map.containsKey('build_dir'))
    map['build_dir'] = 'build'

  // Should this build OGS?
  if (!map.containsKey('ogs'))
    map['ogs'] = false

  // Set a custom PYTHON_PATH
  if (!map.containsKey('python_path'))
    map['python_path'] = "$WORKSPACE"

  def build_dir = "$WORKSPACE/${map.build_dir}"

  sh "mkdir -p ${build_dir}"

  if (map.ogs) {
    sh 'git submodule update --init ThirdParty/container-maker'
    map['python_path'] = "$WORKSPACE/ThirdParty/container-maker"
    map['recipe'] = 'ThirdParty/container-maker/ogs-builder.py'
  }
  dir (build_dir) {
    withPythonEnv('python') {
      sh """pip install ${map.hpccm_version}
            PYTHONPATH=${map.python_path} hpccm --single-stage --recipe $WORKSPACE/${map.recipe} \
            --userarg ${map.options} > Dockerfile.gcc.jenkins
         """.stripIndent()
    }
    // Multi stage Dockerfiles crash Jenkins
    // https://issues.jenkins-ci.org/browse/JENKINS-44609
    sh 'sed -e "s/ AS stage0//g" -i Dockerfile.gcc.jenkins'
  }
  def image = docker.build("ogs6/gcc:jenkins-${env.GIT_COMMIT}",
                           "-f ${build_dir}/Dockerfile.gcc.jenkins ${build_dir}")
  image.inside('-v /home/jenkins/cache:/home/jenkins/cache') {
    body()
  }
}
