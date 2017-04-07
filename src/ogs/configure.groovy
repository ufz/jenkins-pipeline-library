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
    String sourceDir
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
        sourceDir: 'ogs',
        useConan: false
    ]
    new ConfigParams(map << userMap)

    def script = ""

    if (map.env != null)
        script += ". ${map.sourceDir}/scripts/env/${map.env}\n"
    if (map.keepDir == false)
        script += "rm -rf ${map.dir} && mkdir ${map.dir}\n"
    if (map.useConan) {
        def conan_args =
            "-u " +
            "-s build_type=${map.config} " +
            "-s arch=${map.arch}"
        script += "(cd ${map.dir} && conan install ../${map.sourceDir} ${conan_args})\n"
    }

    script += "(cd ${map.dir} && cmake ../${map.sourceDir} -G \"${map.generator}\" ${map.cmakeOptions})\n"

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
        sourceDir: 'ogs',
        useConan: true
    ]
    new ConfigParams(map << userMap)

    env = map.script.env

    if (map.keepDir == false)
        bat "rd /S /Q ${map.dir} 2>nul & mkdir ${map.dir}"

    if (map.useConan) {
        conanCompilerVersion = env.MSVC_NUMBER
        if (env.MSVC_NUMBER == '15') // 14 and 15 are binary compatible
            conanCompilerVersion = '14'
        def conan_args =
            "-u " +
            "-s compiler=\"Visual Studio\" " +
            "-s compiler.version=${conanCompilerVersion} " +
            "-s build_type=${map.config} " +
            "-s arch=${map.arch}"

        bat "cd ${map.dir} && conan install ../${map.sourceDir} ${conan_args}"
    }

    generator = "Visual Studio " + env.MSVC_NUMBER + " " + env.MSVC_VERSION
    if (map.arch == "x86_64")
        generator = generator + " Win64"
    if (map.generator == 'Ninja')
        generator = 'Ninja'

    vcvarsllDir = "%vs${env.MSVC_NUMBER}0comntools%..\\..\\VC"
    if ((env.MSVC_NUMBER as Integer) >= 15)
        vcvarsllDir = "C:\\Program Files (x86)\\Microsoft Visual Studio\\${env.MSVC_VERSION}\\Community\\VC\\Auxiliary\\Build"

    vcvarsallParam = "amd64"
    if (map.arch == "x86")
        vcvarsallParam = "x86"

    bat """:: set path=%path:\"=%
           call "${vcvarsllDir}\\vcvarsall.bat" ${vcvarsallParam}
           cd ${map.dir}
           cmake ../${map.sourceDir} -G "${generator}" -DCMAKE_BUILD_TYPE=${map.config} ${map.cmakeOptions}""".stripIndent()
}
