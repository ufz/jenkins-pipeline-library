package ogs.helper;

def isRelease(script) {
    if (script == null)
        return false;
    if (script.env == null)
        return false;
    if (script.env.BRANCH_NAME == null)
        return false;
    if (script.env.BRANCH_NAME == 'master' || script.env.BRANCH_NAME.contains('release'))
        return true;
    return false;
}

def isOriginMaster(script) {
    if (script == null)
        return false;
    if (script.env == null)
        return false;
    if (script.env.BRANCH_NAME == null)
        return false;
    if (script.env.BRANCH_NAME == 'master' && script.env.JOB_URL.contains('ufz'))
        return true;
    return  false;
}

def getTag(directory = "./") {
    def tagName
    dir(directory) {
        hasTag = sh(returnStatus: true, script: 'git describe --exact-match --tags HEAD')
        if (hasTag == 0) {
            // echo 'has tag!'
            return sh(returnStdout: true, returnStatus: false, script: 'git describe --exact-match --tags HEAD').trim()
        }
        else
            return ''
    }
}

def getEnv(script, arch = 'x64', msvc_number = '15') {
    node_name = script.env.NODE_NAME

    conan_arch = 'x86'
    if (arch == 'x32')
        conan_arch = 'x86_64'

    if (msvc_number == '12')
        msvc_version = '2013'
    if (msvc_number == '14')
        msvc_version = '2015'
    if (msvc_number == '15')
        msvc_version = '2017'

    msvc_sdk = '8.1'
    if (msvc_number == '15')
        msvc_sdk = '10'

    return [
        'CONAN_CMAKE_GENERATOR=Ninja',
        "CONAN_ARCH=${conan_arch}",
        "ARCH=${arch}",
        "MSVC_VERSION=${msvc_version}",
        "MSVC_NUMBER=${msvc_number}",
        "MSVC_SDK=${msvc_sdk}"
    ]
}

getEnvLinux(script) {
    node_name = script.env.NODE_NAME

    array = [:]

    if (node_name == 'envinf1' || node_name == 'vismac02')
        array.plus(["CCACHE_BASEDIR=${script.env.WORKSPACE}"])

    return array
}

def notify(message, color = 'good',
    icon = 'http://mirrors.jenkins-ci.org/art/jenkins-logo/favicon.ico') {
    mattermostSend color: color, message: message, icon: icon
}
