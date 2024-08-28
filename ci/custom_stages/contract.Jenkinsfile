#!/usr/bin/env groovy

def bob = './bob/bob'
def bob_custom = "${bob} -r ruleset2.0.yaml"
def bob_common = "${bob} -r ci/common_ruleset2.0.yaml"

stage('Custom Generate Contract Test Coverage Report') {
    try{
        withCredentials([usernamePassword(credentialsId: 'SELI_ARTIFACTORY', usernameVariable: 'SELI_ARTIFACTORY_REPO_USER', passwordVariable: 'SELI_ARTIFACTORY_REPO_PASS')]) {
           sh "${bob_custom} contract-test-coverage:generate-report"
           sh "${bob_custom} contract-test-coverage:verify-coverage"
        }
    } catch (e) {
        throw e
    } finally {
        archiveArtifacts allowEmptyArchive: true, artifacts: 'build/swagger-coverage-results/*'
        publishHTML (target: [
            allowMissing: false,
            alwaysLinkToLastBuild: false,
            keepAll: true,
            reportDir: 'build/swagger-coverage-results',
            reportFiles: 'swagger-coverage-report-cm.html',
            reportName: 'Contract Test Coverage Report'
        ])
    }
}