def call(body) {
  // evaluate the body block, and collect configuration into the object
  def map = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = map
  body()

  // defaults
  if (!map.containsKey('dir'))
    map['dir'] = 'build'
  if (!map.containsKey('cmd'))
    map['cmd'] = 'make -j $(nproc)'
  if (!map.containsKey('target'))
    map['target'] = 'package'

  def script = ""

  if (map.env != null)
    script += ". ogs/scripts/env/${map.env}\n"
  script += "cd ${map.dir} && ${map.cmd} ${map.target}\n"

  sh "${script}"
}
