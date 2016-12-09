package ogs.build;

def linuxWithEnv(env, buildDir, target = null, cmd = 'make -j $(nproc)') {
    linux(buildDir, target, cmd, env)
}

def linux(buildDir, target = null, cmd = 'make -j $(nproc)', env = null) {
    def script = ""

    if (env != null)
        script += ". ogs/scripts/env/${env}\n"
    if (target == null || target == 'package')
        script += "cd ${buildDir} && bash package.sh\n"
    else
        script += "cd ${buildDir} && ${cmd} ${target}\n"

    sh "${script}"
}

def win(script, buildDir, target = null) {
    targetString = ""
    if (target == null || target == 'package')
        buildString = "CALL package.cmd"
    else
        buildString = "cmake --build . --config Release --target ${target}"

    vcvarsallParam = "amd64"
    if (script.env.ARCH == 'x32')
        vcvarsallParam = "x86"

    bat("""set path=%path:\"=%
           call "%vs${script.env.MSVC_NUMBER}0comntools%..\\..\\VC\\vcvarsall.bat" ${vcvarsallParam}
           cd ${buildDir}
           ${buildString}""".stripIndent())
}
