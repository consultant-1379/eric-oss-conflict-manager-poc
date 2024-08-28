#!/usr/bin/env groovy

def bob = './bob/bob'
def bob_custom = "${bob} -r ruleset2.0.yaml"
def bob_common = "${bob} -r ci/common_ruleset2.0.yaml"

try {
    stage('Helm Install') {
        ci_pipeline_scripts.retryMechanism("${bob_common} helm-dry-run", 3)
        if (env.HELM_UPGRADE == "true") {
            withCredentials([usernamePassword(credentialsId: 'SELI_ARTIFACTORY', usernameVariable: 'SELI_ARTIFACTORY_REPO_USER', passwordVariable: 'SELI_ARTIFACTORY_REPO_PASS')]) {
                ci_pipeline_scripts.retryMechanism("${bob_custom} helm-upgrade", 3)
            }
        } else {
            withCredentials([usernamePassword(credentialsId: 'SELI_ARTIFACTORY', usernameVariable: 'SELI_ARTIFACTORY_REPO_USER', passwordVariable: 'SELI_ARTIFACTORY_REPO_PASS'),
                             usernamePassword(credentialsId: 'SERO_ARTIFACTORY', usernameVariable: 'SERO_ARTIFACTORY_REPO_USER', passwordVariable: 'SERO_ARTIFACTORY_REPO_PASS')]) {
                ci_pipeline_scripts.retryMechanism("${bob_custom} helm-install", 3)
            }
        }
        withCredentials([usernamePassword(credentialsId: 'SELI_ARTIFACTORY', usernameVariable: 'SELI_ARTIFACTORY_REPO_USER', passwordVariable: 'SELI_ARTIFACTORY_REPO_PASS')]) {
            ci_pipeline_scripts.retryMechanism("${bob_common} healthcheck", 3)
        }
    }
} catch (e) {
    withCredentials([usernamePassword(credentialsId: 'SERO_ARTIFACTORY', usernameVariable: 'SERO_ARTIFACTORY_REPO_USER', passwordVariable: 'SERO_ARTIFACTORY_REPO_PASS')]) {
        ci_pipeline_scripts.retryMechanism("${bob_common} collect-k8s-logs || true", 3)
        archiveArtifacts allowEmptyArchive: true, artifacts: "k8s-logs/*"
    }
    ci_pipeline_scripts.retryMechanism("${bob_common} delete-namespace", 3)
} finally {
    ci_pipeline_scripts.retryMechanism("${bob_common} kaas-info || true", 3)
    archiveArtifacts allowEmptyArchive: true, artifacts: 'build/kaas-info.log'
}