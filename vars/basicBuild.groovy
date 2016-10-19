//vars/basicBuild.groovy
def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    //project_name
    //version - null/tag
    stage '[' + config.env + '] ' + config.project_name + ' Build'
    node {
        deleteDir()
        unstash 'source_' + config.project_name
        mvn {
            pom = config.mvn_pom
            goals = config.mvn_goals
            options = config.mvn_options
        }
    }
}
