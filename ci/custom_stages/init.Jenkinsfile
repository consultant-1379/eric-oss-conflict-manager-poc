#!/usr/bin/env groovy

def bob = './bob/bob'
def bob_custom = "${bob} -r ruleset2.0.yaml"
def bob_common = "${bob} -r ci/common_ruleset2.0.yaml"

try {
    stage('Custom Init') {
        script {
            FOSSA_ENABLED = sh(returnStdout: true, script: 'git diff origin/master --name-only | grep -q pom.xml && echo true || echo false').trim()
            ci_pipeline_scripts.retryMechanism("${bob_custom} update-pom-version", 3)
            sh '''sed -i 's|- rule: publish-jars||g' ci/common_ruleset2.0.yaml;'''
        }
    }
} catch (e) {
    throw e
}