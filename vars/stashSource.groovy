//vars/stashSource.groovy
def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    //project_name - string
    //version - null/tag
    //repo - repo name
    stage '[' + ENVIRONMENT + '] checkout ' + config.project_name
    node {
        deleteDir()
        if (config.version == null) {
            git branch: 'develop', url: config.repo
        } else {
            git tag: config.version, url: config.repo
        }
        stash excludes: 'target/', name: 'source_' + config.project_name
        stash includes: 'performance/**/*', name: 'performance_' + config.project_name
    }
}