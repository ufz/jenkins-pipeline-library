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

def getEnv(script, arch = 'x64') {
    node_name = script.env.NODE_NAME
    msvc_version = '2013'
    msvc_number = '12'
    conan_arch = 'x86'
    if (arch == 'x32')
        conan_arch = 'x86_64'

    if (node_name == 'visserv3')
        qtdir = "C:\\libs\\qt\\4.8\\msvc${msvc_version}-${arch}"
    if (node_name == 'win1') {
        if (arch == 'x32')
            qtdir = "C:\\libs\\qt-4.8.7-x86-msvc${msvc_version}\\qt-4.8.7-x86-msvc${msvc_version}"
        else
            qtdir = "C:\\libs\\qt-4.8.7-${arch}-msvc${msvc_version}\\qt-4.8.7-${arch}-msvc${msvc_version}"
    }
    if (node_name == 'winserver1') {
        msvc_version = 2015
        msvc_number = 14
        // TODO qtdir = ...
    }


    return [
        "QTDIR=${qtdir}",
        'Path=$Path;$QTDIR\\bin',
        'CONAN_CMAKE_GENERATOR=Ninja',
        "CONAN_ARCH=${conan_arch}",
        "ARCH=${arch}",
        "MSVC_VERSION=${msvc_version}",
        "MSVC_NUMBER=${msvc_number}"
    ]
}

def notify(message, color = 'good',
    icon = 'http://mirrors.jenkins-ci.org/art/jenkins-logo/favicon.ico') {
    mattermostSend color: color, message: message, icon: icon
}
