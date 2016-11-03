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
    if (script.env.NODE_NAME == 'visserv3')
        qtdir = "C:\\libs\\qt\\4.8\\msvc2013-${arch}"
    if (script.env.NODE_NAME == 'win1') {
        if (arch == 'x32')
            qtdir = "C:\\libs\\qt-4.8.7-x86-msvc2013\\qt-4.8.7-x86-msvc2013"
        else
            qtdir = "C:\\libs\\qt-4.8.7-${arch}-msvc2013\\qt-4.8.7-${arch}-msvc2013"
    }

    return [
        "QTDIR=${qtdir}",
        'Path=$Path;$QTDIR\\bin',
        'CONAN_CMAKE_GENERATOR=Ninja',
        "ARCH=${arch}"
    ]
}
