//vars/sendEmailNotification.groovy 
@NonCPS
def call(attachmentsPattern) {
    def color_name
    def color_code
    def build_status
    def result = currentBuild.rawBuild.getResult()

    if (result == null) { // Build is ongoing
        build_status = 'In Progress'
        color_name = 'YELLOW'
        color_code = '#FFFF00'
    } else if (result.isBetterOrEqualTo(Result.SUCCESS)) {
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

    def full_display_name = currentBuild.getFullDisplayName()
    def subject = "[Jenkins] ${full_display_name} : ${build_status}"
    def body = """<p><span style="color: ${color_code}">${build_status}</span>: ${full_display_name}:</p>
    <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${full_display_name}</a>&QUOT;</p>"""

    // Send an email notification
    emailext(attachLog: true,
             attachmentsPattern: attachmentsPattern
             body: body,
             recipientProviders: [[$class: 'CulpritsRecipientProvider'],
                                  [$class: 'FirstFailingBuildSuspectsRecipientProvider'],
                                  [$class: 'RequesterRecipientProvider']],
             subject: subject)        
}
