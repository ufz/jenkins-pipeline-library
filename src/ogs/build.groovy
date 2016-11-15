package ogs.build;

def linux(script, buildDir, target = null, cmd = 'make -j $(nproc)') {
    if (target == null) {
        target = 'package'
    }
    sh "cd ${buildDir} && ${cmd} ${target}"
}

def win(script, buildDir, target = null) {
    targetString = ""
    if (target == null)
        targetString = "--target package"
    else
        targetString = "--target ${target}"

    vcvarsallParam = "amd64"
    if (buildDir.endsWith("32"))
        vcvarsallParam = "x86"

    bat("""set path=%path:\"=%
           call "%vs120comntools%..\\..\\VC\\vcvarsall.bat" ${vcvarsallParam}
           cd ${buildDir}
           cmake --build . --config Release ${targetString}""".stripIndent())
}
