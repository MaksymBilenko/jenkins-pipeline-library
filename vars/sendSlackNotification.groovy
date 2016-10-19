//vars/sendSlackNotification.groovy
def call(body) {
	def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    def msg = ""
    def job = jenkins.model.Jenkins.instance.getItemByFullName(env.JOB_NAME)
    def build = job.getBuildByNumber(Integer.parseInt(env.BUILD_NUMBER))
    if (config.type == "start") {
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
        msg = build.fullDisplayName + " " + type + " after " + build.getDurationString() + " second <" + env.BUILD_URL + "|open>"
        build = null
        job = null
    }
    slackSend(channel: config.channel, message: msg)
}
