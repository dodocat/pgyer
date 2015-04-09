package org.quanqi.pgyer.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.apache.commons.lang.WordUtils

class Pgyer implements Plugin<Project> {
    void apply(Project project) {
        def apks = project.container(ApkTarget) {
            String apkName = WordUtils.capitalize(it.toString())
            def userTask = project.task("uploadPgyer${apkName}", type: PgyerUserUploadTask)
            userTask.group = 'Pgyer'
            userTask.description = "Upload an APK file of ${apkName}" 
            userTask.apkName = apkName

            project.extensions.create(it, ApkTarget, apkName)
        }

        def pgyer = new PgyerExtension(apks)
        project.convention.plugins.deploygate = pgyer
        project.extensions.pgyer = pgyer

        def apkUpload = project.task('uploadPgyer', type: PgyerAllUploadTask)
        apkUpload.group = 'Pgyer'
        apkUpload.description = 'Uploads the APK file. Also updates the distribution specified by distributionKey if configured'
    }
}
