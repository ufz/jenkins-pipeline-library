package ogs.build;

class BuildParams{
    String cmd
    String dir
    String env
    Script script
    String target
}

def linux(Map userMap = [:]) {
    def map = [
        cmd: 'make -j $(nproc)',
        dir: 'build',
        env: null,
        target: 'package'
    ]
    new BuildParams(map << userMap)

    def script = ""

    if (map.env != null)
        script += ". ogs/scripts/env/${map.env}\n"
    script += "cd ${map.dir} && ${map.cmd} ${map.target}\n"

    sh "${script}"
}

def win(Map userMap = [:]) {
    def map = [
        dir: 'build',
        target: 'package'
    ]
    new BuildParams(map << userMap)

    env = map.script.env

    buildString = "cmake --build . --config Release --target ${map.target}"

    vcvarsllDir = "%vs${env.MSVC_NUMBER}0comntools%..\\..\\VC"
    if ((env.MSVC_NUMBER as Integer) >= 15)
        vcvarsllDir = "C:\\Program Files (x86)\\Microsoft Visual Studio\\${env.MSVC_VERSION}\\Community\\VC\\Auxiliary\\Build"

    vcvarsallParam = "amd64"
    if (env.ARCH == 'x32')
        vcvarsallParam = "x86"

    bat("""set path=%path:\"=%
           call "${vcvarsllDir}\\vcvarsall.bat" ${vcvarsallParam}
           cd ${map.dir}
           ${buildString}""".stripIndent())
}
