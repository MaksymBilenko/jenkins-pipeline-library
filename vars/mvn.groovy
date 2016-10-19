//vars/mvn.groovy
def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    sh "${tool 'Maven - 322'}/bin/mvn -f ${config.pom} ${config.goals} ${config.options}"
}
