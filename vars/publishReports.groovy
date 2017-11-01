def call(body) {
  // evaluate the body block, and collect configuration into the object
  def map = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = map
  body()

  // defaults
  if (!map.containsKey('dir'))
    map['dir'] = 'build'
  if (!map.containsKey('ctestPattern'))
    map['ctestPattern'] = "${map.dir}/Testing/**/*.xml"
  if (!map.containsKey('gtestPattern'))
    map['gtestPattern'] = "${map.dir}/Tests/testrunner.xml"

  step([$class: 'XUnitPublisher', testTimeMargin: '3000', thresholdMode: 1,
    thresholds: [
      [$class: 'FailedThreshold', failureNewThreshold: '', failureThreshold: '',
        unstableNewThreshold: '', unstableThreshold: ''],
      [$class: 'SkippedThreshold', failureNewThreshold: '', failureThreshold: '',
        unstableNewThreshold: '', unstableThreshold: '']],
    tools: [
      [$class: 'CTestType', deleteOutputFiles: true, failIfNotNew: true, pattern:
        "${map.ctestPattern}", skipNoTestFiles: true, stopProcessingIfError: true],
      [$class: 'GoogleTestType', deleteOutputFiles: true, failIfNotNew: true, pattern:
        "${map.gtestPattern}", skipNoTestFiles: true, stopProcessingIfError: true]]
    ])
}
