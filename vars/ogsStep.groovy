def call(Closure body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config

    body()

    def foo = config.configure
    echo foo
    stage(config.name) {
        gitlabCommitStatus(config.name) {
            foo.call()
        }
    }
}
