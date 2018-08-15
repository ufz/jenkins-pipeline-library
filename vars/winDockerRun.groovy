def call(body) {
  // evaluate the body block, and collect configuration into the object
  def map = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = map
  body()

  // defaults
  if (!map.containsKey('image'))
    map['image'] = 'registry.opengeosys.org/ogs/ogs/msvc/2017:latest'

  bat """
    docker run --rm -v %cd%:C:\\jenkins-ws -w C:\\jenkins-ws \
    --mount source=cache-conan-short,destination=C:\\.conan \
    --mount source=cache-conan,destination=C:\\conan-cache \
    ${map.image} \
    powershell -NoLogo -ExecutionPolicy Bypass; \
    \$env:CONAN_USER_HOME = 'C:\\conan-cache'; \
    ${map.cmd}
    """.stripIndent()
}
