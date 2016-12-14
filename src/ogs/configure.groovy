package ogs.configure;

class ConfigParams{
    String arch
    String config
    String cmakeOptions
    String dir
    String env
    String generator
    boolean keepDir
    Script script
    boolean useConan
}

def linux(Map userMap = [:]) {
    def map = [
        arch: 'x86_64',
        config: 'Release',
        cmakeOptions: '',
        dir: 'build',
        env: null,
        generator: 'Unix Makefiles',
        keepDir: false,
        useConan: false
    ]
    new ConfigParams(map << userMap)

    def script = ""

    if (map.env != null)
        script += ". ogs/scripts/env/${map.env}\n"
    if (map.keepDir == false)
        script += "rm -rf ${map.dir} && mkdir ${map.dir}\n"
    if (map.useConan) {
        def conan_args =
            "-u " +
            "-s build_type=${map.config} " +
            "-s arch=${map.arch}"
        script += "(cd ${map.dir} && conan install ../ogs ${conan_args})\n"
    }

    script += "(cd ${map.dir} && cmake ../ogs -G \"${map.generator}\" ${map.cmakeOptions})\n"

    sh "${script}"
}

def win(Map userMap = [:]) {
    def map = [
        arch: 'x86_64',
        config: 'Release',
        cmakeOptions: '',
        dir: 'build',
        generator: 'Ninja',
        keepDir: false,
        useConan: true
    ]
    new ConfigParams(map << userMap)

    if (map.keepDir == false)
        bat "rd /S /Q ${map.dir} 2>nul & mkdir ${map.dir}"

    if (map.useConan) {
        def conan_args =
            "-u " +
            "-s compiler=\"Visual Studio\" " +
            "-s compiler.version=${map.script.env.MSVC_NUMBER} " +
            "-s build_type=${map.config} " +
            "-s arch=${map.arch}"

        bat "cd ${map.dir} && conan install ../ogs ${conan_args}"
    }

    vcvarsallParam = "amd64"
    if (map.arch == "x86")
        vcvarsallParam = "x86"

    bat """set path=%path:\"=%
           call "%vs${map.script.env.MSVC_NUMBER}0comntools%..\\..\\VC\\vcvarsall.bat" ${vcvarsallParam}
           cd ${map.dir}
           cmake ../ogs -G "${map.generator}" -DCMAKE_BUILD_TYPE=${map.config} ${map.cmakeOptions}""".stripIndent()
}
