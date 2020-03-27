package org.quanqi.pgyer.gradle.plugins

import com.squareup.okhttp.MediaType
import com.squareup.okhttp.MultipartBuilder
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import com.squareup.okhttp.Response
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.json.JSONObject

import java.util.concurrent.TimeUnit

class PgyerTask extends DefaultTask {
    private final String API_END_POINT = "http://www.pgyer.com/apiv2"

    void upload(Project project, List<Apk> apks) {
        String endPoint = getEndPoint(project)

        HashMap<String, JSONObject> result = httpPost(endPoint, apks)
        for (Apk apk in apks) {
            JSONObject json = result.get(apk.name)
            errorHandling(apk, json)
            println "${apk.name} result: ${json.toString()}"
        }
    }

    private void errorHandling(Apk apk, JSONObject json) {
        print(json)
    }

    private String getEndPoint(Project project) {
        String _api_key = project.pgyer._api_key
        String buildInstallType = project.pgyer.buildInstallType
        String buildPassword = project.pgyer.buildPassword
        if (_api_key == null
                || buildInstallType == null
                || buildPassword == null) {
            throw new GradleException("apiKey, buildInstallType or buildPassword is missing")
        }
        String endPoint = API_END_POINT + "/app/upload"
        return endPoint
    }

    private HashMap<String, JSONObject> httpPost(String endPoint, List<Apk> apks) {
        HashMap<String, JSONObject> result = new HashMap<String, JSONObject>()
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        client.setReadTimeout(60, TimeUnit.SECONDS);

        for (Apk apk in apks) {
            MultipartBuilder multipartBuilder = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)


            multipartBuilder.addFormDataPart("_api_key", new String(project.pgyer._api_key))
            multipartBuilder.addFormDataPart("buildInstallType", new String(project.pgyer.buildInstallType))
            multipartBuilder.addFormDataPart("buildPassword", new String(project.pgyer.buildPassword))


            multipartBuilder.addFormDataPart("file",
                    apk.file.name,
                    RequestBody.create(
                            MediaType.parse("application/vnd.android.package-archive"),
                            apk.file)
            )

            HashMap<String, String> params = apk.getParams()
            for (String key : params.keySet()) {
                println("add part key: " + key + " value: " + params.get(key))
                multipartBuilder.addFormDataPart(key, params.get(key))
            }

            Request request = new Request.Builder().url(getEndPoint(project)).
                    post(multipartBuilder.build()).
                    build()

            Response response = client.newCall(request).execute();

            if (response == null || response.body() == null) return null;
            InputStream is = response.body().byteStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is))
            JSONObject json = new JSONObject(reader.readLine())
            result.put(apk.name, json)
            is.close()
        }
        return result
    }
}
