def call(body) {
  // evaluate the body block, and collect configuration into the object
  def map = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = map
  body()

  def isUnix = isUnix()

  // defaults
  if (!map.containsKey('dir'))
    map['dir'] = 'build'
  if (!map.containsKey('source_dir'))
    map['source_dir'] = '.'
  if (!map.containsKey('config'))
    map['config'] = 'Release'
  if (!map.containsKey('target'))
    map['target'] = 'package'
  if (!map.containsKey('cmd'))
    map['cmd'] = "cmake --build . --config ${map.config} --target ${map.target}"

  if (map.containsKey('threads'))
    map['cmd'] = "${map.cmd} -j ${map.threads}"
  else if (env.NUM_THREADS)
    map['cmd'] = "${map.cmd} -j ${env.NUM_THREADS}"


  if (map.containsKey('cmd_args'))
    map['cmd'] = "${map.cmd} -- ${map.cmd_args}"

  def script = ""
  def tee_cmd = "tee"

  if (isUnix) {
    // Fail early and with pipes | too
    // https://vaneyckt.io/posts/safer_bash_scripts_with_set_euxo_pipefail/
    // Does not work in /bin/sh
    script += 'case $BASH in *bash* ) set -eo pipefail ;;esac\n'
  }
  else {
    // Win-specific
    tee_cmd = "\"C:\\Program Files\\Git\\usr\\bin\\tee\""
    vcvarsllDir = "%vs${env.MSVC_NUMBER}0comntools%..\\..\\VC"
    if ((env.MSVC_NUMBER as Integer) >= 15)
      vcvarsllDir = "C:\\Program Files (x86)\\Microsoft Visual Studio\\${env.MSVC_VERSION}\\Community\\VC\\Auxiliary\\Build"

    vcvarsallParam = "amd64"
    if (map.arch == "x86")
      vcvarsallParam = "x86"
    script += ":: set path=%path:\"=%\n"
    script += "call \"${vcvarsllDir}\\vcvarsall.bat\" ${vcvarsallParam}\n"
  }

  if (map.env != null)
    script += ". ${map.source_dir}/scripts/env/${map.env}\n"
  script += "cd ${map.dir}\n"
  script += "${map.cmd}"
  if (map.log != null)
    script += " 2>&1 | ${tee_cmd} ${map.log}"
  script += "\n"

  if (isUnix)
    sh "${script}"
  else
    bat "${script}"
}
