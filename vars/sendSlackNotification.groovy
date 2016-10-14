//vars/sendSlackNotification.groovy
import hudson.model.Result

@NonCPS
def call(String channel = null) {
	def color_name
	def color_code
	def build_status
	def msg
	def full_display_name = currentBuild.getFullDisplayName()
	def job = jenkins.model.Jenkins.instance.getItemByFullName(env.JOB_NAME)
	def build = job.getBuildByNumber(Integer.parseInt(env.BUILD_NUMBER))

	def result = currentBuild.rawBuild.getResult()

	if (result == null) {
		//if build in progress - send message about starting
		build_status = 'In Progress'
		color_name = 'YELLOW'
		color_code = '#FFFF00'
		def items = build.getChangeSets()
		files = 0
		for(int i = 0 ; i < items.size(); i++) {
			for (Iterator iterator = items[i].iterator(); iterator.hasNext();) {
				gitChangeSet = iterator.next()
				files += gitChangeSet.getAffectedFiles().size()
			}
		}
		msg = build.fullDisplayName + " " + build.getCause(hudson.model.Cause.UserIdCause.class).shortDescription + " (" + files + " file(s) changed)"
		items = null
		build = null
		job = null
		gitChangeSet = null
	} else {
		if (result.isBetterOrEqualTo(Result.SUCCESS)) {
			build_status = 'Success'
			color_name = 'GREEN'
			color_code = '#00FF00'
		} else if (result.isBetterOrEqualTo(Result.FAILURE)) {
			build_status = 'Failed'
			color_name = 'RED'
			color_code = '#FF0000'
		} else if (result.isBetterOrEqualTo(Result.UNSTABLE)) {
			build_status = 'Unstable'
			color_name = 'YELLOW'
			color_code = '#FFFF00'
		} else {
			build_status = 'Error'
			color_name = 'RED'
			color_code = '#FF0000'
		}
		msg = build.fullDisplayName + " " + build_status + " after " + build.getDurationString() + " second <" + env.BUILD_URL + "|open>"
		build = null
		job = null
	}
	if ( channel != null ) {
		channel = (channel.startsWith('#')) ? channel : "#${channel}"
	}
	slackSend(channel: channel, color: color_code, message: msg)
}
