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

class NotifyParams{
    String msg       // Required
    String title     // Defaults to 'Jenkins'
    String url       // Defaults to BUILD_URL
    String url_title // Defaults to JOB_NAME + BUILD_NUMBER
    int priority     // Defaults to 0
    String sound     // See https://pushover.net/api#sounds
    Script script    // Required, pass 'this' pointer
}

def notification(Map userMap = [:]) {
    def map = [
        title: null,
        url: userMap.script.env.BUILD_URL,
        url_title: userMap.script.env.JOB_NAME + "#" +
                   userMap.script.env.BUILD_NUMBER,
        priority: 0,
        sound: null
    ]
    new NotifyParams(map << userMap)

    def body = ""
    node {
        def secrets = [
            [$class: 'VaultSecret', path: 'jenkins/pushover', secretValues: [
                [$class: 'VaultSecretValue', envVar: 'PUSHOVER_TOKEN', vaultKey: 'token'],
                [$class: 'VaultSecretValue', envVar: 'PUSHOVER_GROUP', vaultKey: 'group_devs']]],
        ]

        wrap([$class: 'VaultBuildWrapper', vaultSecrets: secrets]) {
            body += "token=${this.env.PUSHOVER_TOKEN}"
            body += "&user=${this.env.PUSHOVER_GROUP}"
        }
    }

    body += "&message=${map.msg}"
    body += "&priority=${map.priority}"
    body += "&url=${map.url}"
    body += "&url_title=${map.url_title}"

    if (map.title != null)
        body += "&title=${map.title}"
    if (map.sound != null)
        body += "&sound=${map.sound}"

    httpRequest(
        url: 'https://api.pushover.net/1/messages.json',
        httpMode: 'POST',
        requestBody: body
    )
}
