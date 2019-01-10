def call(body) {
    def map = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = map
    body()

    // defaults
    if (!map.containsKey('warnings'))
        map['warnings'] = 0
    if (!map.containsKey('config'))
        map['config'] = 'FEM'
    def build_dir = "build_" + map.config

    recordIssues enabledForFailure: true,
        tools: [gcc4(name: 'GCC ' + map.config, id: 'gcc-' + map.config,
                     pattern: build_dir + '/build.log')],
        unstableTotalAll: map.warnings + 1
    xunit([CTest(pattern: build_dir + '/Testing/**/*.xml')])
    archiveArtifacts allowEmptyArchive: true,
                     artifacts: build_dir + '/benchmarks/**/*.numdiff'
}
