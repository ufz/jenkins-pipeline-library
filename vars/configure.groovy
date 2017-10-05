def call(body) {
  // evaluate the body block, and collect configuration into the object
  def map = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = map
  body()

  // defaults
  if (!map.containsKey('dir'))
    map['dir'] = 'build'
  if (!map.containsKey('sourceDir'))
    map['sourceDir'] = 'ogs'
  if (!map.containsKey('keepDir'))
    map['keepDir'] = false

  if (!map.containsKey('generator')) {
    if (isUnix)
      map['generator'] = 'Unix Makefiles'
    else
      map['generator'] = 'Ninja'
  }

  def script = ""
  if (map.env != null)
    script += ". ${map.sourceDir}/scripts/env/${map.env}\n"
  if (map.keepDir == false)
    script += "rm -rf ${map.dir} && mkdir ${map.dir}\n"

  script += "(cd ${map.dir} && cmake ../${map.sourceDir} -G \"${map.generator}\" ${map.cmakeOptions})\n"

  sh "${script}"
}
