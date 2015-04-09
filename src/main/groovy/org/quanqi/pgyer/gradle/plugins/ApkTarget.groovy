package org.quanqi.pgyer.gradle.plugins

import org.gradle.api.Named
import org.gradle.api.internal.project.ProjectInternal

class ApkTarget implements Named {
    String name
    ProjectInternal target 
    File sourceFile

    String distributionKey
    /**
     * (ipa上传时为必填) 填写发布范围，值为（1，2，3），1：企业发布，2：直接发布，3：只有我安装
     */
    int publishRange
    /**
     * (选填) 是否发布到广场，值为（1，2），1：发布到广场，2：不发布到广场。默认为不发布到广场
     */
    int isPublishToPublic

    public ApkTarget(String name) {
      super()
      this.name = name
      this.target = target
    }
}
