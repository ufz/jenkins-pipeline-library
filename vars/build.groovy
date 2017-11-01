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

  def script = ""

  if (!isUnix) {
    // Win-specific
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
  script += "cd ${map.dir} && ${map.cmd}\n"

  if (isUnix)
    sh "${script}"
  else
    bat "${script}"
}
