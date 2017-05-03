package ogs.post;

def publishTestReports(ctestPattern, gtestPattern) {
    step([$class: 'XUnitPublisher', testTimeMargin: '3000', thresholdMode: 1,
        thresholds: [
            [$class: 'FailedThreshold', failureNewThreshold: '', failureThreshold: '',
                unstableNewThreshold: '', unstableThreshold: ''],
            [$class: 'SkippedThreshold', failureNewThreshold: '', failureThreshold: '',
                unstableNewThreshold: '', unstableThreshold: '']],
        tools: [
            [$class: 'CTestType', deleteOutputFiles: true, failIfNotNew: true, pattern:
                "${ctestPattern}", skipNoTestFiles: true, stopProcessingIfError: true],
            [$class: 'GoogleTestType', deleteOutputFiles: true, failIfNotNew: true, pattern:
                "${gtestPattern}", skipNoTestFiles: true, stopProcessingIfError: true]]
    ])
}

def cleanup(directory = 'build') {
    dir(directory) {
        deleteDir()
    }
}
