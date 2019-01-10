def call(body) {
    def map = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = map
    body()

    // defaults
    if (!map.containsKey('config'))
        map['config'] = 'FEM'
    def build_dir = "build_" + map.config

    archiveArtifacts allowEmptyArchive: true,
                     artifacts: build_dir + '/*.tar.gz'

}
