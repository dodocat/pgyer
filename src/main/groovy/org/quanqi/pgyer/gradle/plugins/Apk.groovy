package org.quanqi.pgyer.gradle.plugins

import org.gradle.api.Project

class Apk {
    String name
    File file

    /**
     * (ipa上传时为必填) 填写发布范围，值为（1，2，3），1：企业发布，2：直接发布，3：只有我安装
     */
    int publishRange
    /**
     * (选填) 是否发布到广场，值为（1，2），1：发布到广场，2：不发布到广场。默认为不发布到广场
     */
    int isPublishToPublic

    /**
     * (当用户上传的文件为开发者签名的ipa并且publishRange为1时，为必填。否则为选填) 设置App安装密码，为空或不传则应用无下载密码
     */
    String password

    Apk(String name, File file, int publishRange, int isPublishToPublic, String password) {
        this.name = name
        this.file = file
        this.publishRange = publishRange
        this.isPublishToPublic = isPublishToPublic
        this.password = password
    }


    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<String, String>()
        if (publishRange > 0) {
            params.put("publishRange", publishRange as String)
        }
        if(isPublishToPublic > 0) {
            params.put("isPublishToPublic", isPublishToPublic as String)
        }

        return params
    }

    public static List<Apk> getApks(Project project, String searchApkName = "") {
        List<Apk> apks = []
        for (_apk in project.deploygateApks) {
            String name = _apk.name
            if(searchApkName != "" && searchApkName != name) continue

            File file = null

            Apk apk = new Apk(name, file, 0, 0, null);

            if(_apk.hasProperty("sourceFile") && _apk.sourceFile != null) {
                apk.file = _apk.sourceFile
            }
            if (_apk.hasProperty("publishRange") && _apk.publishRange > 0) {
                apk.publishRange = _apk.publishRange
            }
            if(_apk.hasProperty("isPublishToPublic")) {
                apk.isPublishToPublic = _apk.isPublishToPublic
            }
            apks.add(apk)
        }
        return apks
    }
}
