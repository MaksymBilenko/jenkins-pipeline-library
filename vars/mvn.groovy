//vars/mvn.groovy
def call(args) {
    sh "${tool 'Maven - 322'}/bin/mvn ${args}"
}
