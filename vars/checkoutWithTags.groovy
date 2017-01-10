def call(String url = 'https://github.com/ufz/ogs.git') {
    def cmd = "git fetch ${url} --tags"
	checkout scm
    if(isUnix())
        sh "${cmd}"
    else
        bat "${cmd}"
}
