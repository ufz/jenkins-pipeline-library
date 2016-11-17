package ogs.build;

def linux(script, buildDir, target = null, cmd = 'make -j $(nproc)') {
    if (target == null || target == 'package') {
        sh "cd ${buildDir} && bash package.sh"
    }
    else
        sh "cd ${buildDir} && ${cmd} ${target}"
}

def win(script, buildDir, target = null) {
    targetString = ""
    if (target == null || target == 'package')
        buildString = "CALL package.cmd"
    else
        buildString = "cmake --build . --config Release --target ${target}"

    vcvarsallParam = "amd64"
    if (buildDir.endsWith("32"))
        vcvarsallParam = "x86"

    bat("""set path=%path:\"=%
           call "%vs120comntools%..\\..\\VC\\vcvarsall.bat" ${vcvarsallParam}
           cd ${buildDir}
           ${buildString}""".stripIndent())
}
