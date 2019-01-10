def call(body) {
    def options = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = options
    body()

    // defaults
    if (!options.containsKey('config'))
        options['config'] = 'FEM'
    if (!options.containsKey('cmakeOptions'))
        options['cmakeOptions'] = ''
    options['cmakeOptions'] = options['cmakeOptions'] +
        " -DOGS_CONFIG=${options.config}"
        " -DNUMDIFF_TOOL_PATH=/usr/local/numdiff/5.8.1-1/bin/numdiff" +
        " -DOGS_CPU_ARCHITECTURE=generic"
    def build_dir = "build_" + options.config
    def local_env = "envinf1/cli.sh"

    dir('benchmarks') { git 'https://github.com/ufz/ogs5-benchmarks.git' }
    dir('benchmarks_ref') { git 'https://github.com/ufz/ogs5-benchmarks_ref.git' }
    configure {
        cmakeOptions = options.cmakeOptions
        dir=build_dir
        env=local_env
        source_dir='.'
    }
    build {
        dir=build_dir
        env=local_env
        log="build.log"
    }
    build {
        dir=build_dir
        env=local_env
        target="benchmarks-short-normal-long"
    }
}
