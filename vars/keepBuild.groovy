def call(boolean keep = true){
	currentBuild.rawBuild.keepLog(keep)
}
