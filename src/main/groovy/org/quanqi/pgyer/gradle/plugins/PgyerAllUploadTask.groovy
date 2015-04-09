package org.quanqi.pgyer.gradle.plugins

import org.gradle.api.tasks.TaskAction

class PgyerAllUploadTask extends PgyerTask {
    @TaskAction
    def uploadDeployGate() {
        List<Apk> apks = Apk.getApks(project)
        super.upload(project, apks)
    }
}
