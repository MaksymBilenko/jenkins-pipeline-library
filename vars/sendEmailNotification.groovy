//vars/sendEmailNotification.groovy 
def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    def full_display_name = currentBuild.getFullDisplayName()
    def subject = "[Jenkins] ${full_display_name} : ${config.result}"
    
    // Send an email notification
    emailext(attachLog: true,
             attachmentsPattern: config.attachments,
             body: "",
             to: config.recipients,
             subject: subject)
}
