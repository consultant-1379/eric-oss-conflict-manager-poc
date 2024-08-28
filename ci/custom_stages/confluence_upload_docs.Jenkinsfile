#!/usr/bin/env groovy

def bob = './bob/bob'
def bob_custom = "${bob} -r ruleset2.0.yaml"
def bob_common = "${bob} -r ci/common_ruleset2.0.yaml"

if (env.RELEASE == "true") {
  stage('Upload Documentation') {
    withCredentials([usernamePassword(credentialsId: 'SELI_ARTIFACTORY', usernameVariable: 'SELI_ARTIFACTORY_REPO_USER', passwordVariable: 'SELI_ARTIFACTORY_REPO_PASS'),
                     usernamePassword(credentialsId: 'eridoc-user', usernameVariable: 'CONFLUENCE_USERNAME', passwordVariable: 'CONFLUENCE_PASSWORD')]) {
      sh "${bob_custom} confluence-docs-upload || true"
    }
  }
}
