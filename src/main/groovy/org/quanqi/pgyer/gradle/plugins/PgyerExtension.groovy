package org.quanqi.pgyer.gradle.plugins

import org.gradle.api.NamedDomainObjectContainer

public class PgyerExtension {
    final private NamedDomainObjectContainer<ApkTarget> deploygateApks
    String _api_key
    /**
     * (必填)应用安装方式，值为(2,3)。2：密码安装，3：邀请安装
     */
    String buildInstallType
    /**
     * (必填) 设置App安装密码
     */
    String buildPassword

    public PgyerExtension(NamedDomainObjectContainer<ApkTarget> apks) {
        deploygateApks = apks
    }

    public apks(Closure closure) {
        deploygateApks.configure(closure)
    }
}
