package ogs.configure;

def linuxWithEnv(env, buildDir, cmakeOptions, generator = 'Unix Makefiles', conan_args = null, keepBuildDir = false) {
    linux(buildDir, cmakeOptions, generator, conan_args, keepBuildDir, env)
}

def linux(buildDir, cmakeOptions, generator = 'Unix Makefiles', conan_args = null, keepBuildDir = false, env = null) {
    def script = ""

    if (env != null)
        script += ". ogs/scripts/env/${env}\n"
    if (keepBuildDir == false)
        script += "rm -rf ${buildDir} && mkdir ${buildDir}\n"
    if (conan_args != null)
        script += "cd ${buildDir} && conan install ../ogs -u ${conan_args}\n"

    script += "cd ${buildDir} && cmake ../ogs -G \"${generator}\" ${cmakeOptions}\n"

    sh "${script}"
}

def win(buildDir, cmakeOptions, generator, conan_args = null, keepBuildDir = false) {
    if (keepBuildDir == false)
        bat("""rd /S /Q ${buildDir}
               mkdir ${buildDir}""".stripIndent())

    if (conan_args != null)
        bat("""cd ${buildDir}
               conan install ../ogs -u ${conan_args}""".stripIndent())

    vcvarsallParam = "amd64"
        if (buildDir.endsWith("32"))
            vcvarsallParam = "x86"

    bat """set path=%path:\"=%
           call "%vs120comntools%..\\..\\VC\\vcvarsall.bat" ${vcvarsallParam}
           cd ${buildDir}
           cmake ../ogs -G "${generator}" ${cmakeOptions}"""
}
