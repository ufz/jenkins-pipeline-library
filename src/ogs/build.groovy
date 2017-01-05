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

    buildString = "cmake --build . --config Release --target ${map.target}"

    vcvarsallParam = "amd64"
    if (map.script.env.ARCH == 'x32')
        vcvarsallParam = "x86"

    bat("""set path=%path:\"=%
           call "%vs${map.script.env.MSVC_NUMBER}0comntools%..\\..\\VC\\vcvarsall.bat" ${vcvarsallParam}
           cd ${map.dir}
           ${buildString}""".stripIndent())
}
