package ogs.build;

//import ogs.helper;

def linux(script, buildDir, target = null, cmd = 'make -j $(nproc)') {
    if (target == null) {
        target = 'all'
        if (isRelease(script))
            target = 'package'
    }
    sh "cd ${buildDir} && ${cmd} ${target}"
}

def win(script, buildDir, target = null) {
    targetString = ""
    if (target == null && isRelease(script))
        targetString = "--target package"
    else if (target != null)
        targetString = "--target ${target}"

    vcvarsallParam = "amd64"
    if (buildDir.endsWith("32"))
        vcvarsallParam = "x86"

    bat("""set path=%path:\"=%
           call "%vs120comntools%..\\..\\VC\\vcvarsall.bat" ${vcvarsallParam}
           cd ${buildDir}
           cmake --build . --config Release ${targetString}""".stripIndent())
}

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
