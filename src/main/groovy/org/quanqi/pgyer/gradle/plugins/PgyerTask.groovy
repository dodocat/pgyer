package org.quanqi.pgyer.gradle.plugins

import org.gradle.api.GradleException
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.apache.http.protocol.HTTP
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.impl.conn.ProxySelectorRoutePlanner
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.content.StringBody
import org.apache.http.entity.mime.content.FileBody
import org.json.JSONObject
import java.nio.charset.Charset

class PgyerTask extends DefaultTask {
    private final String API_END_POINT = "http://www.pgyer.com/apiv1"

    private void upload(Project project, List<Apk> apks) {
        String endPoint = getEndPoint(project)

        HashMap<String, JSONObject> result = httpPost(endPoint, apks)
        for(Apk apk in apks) {
            JSONObject json = result.get(apk.name)
            errorHandling(apk, json)
            println "${apk.name} result: ${json.toString()}"
        }
    }

    private void errorHandling(Apk apk, JSONObject json) {

        print(json)
    }


    private String getEndPoint(Project project) {

        String uKey = project.pgyer.uKey
        String _api_key = project.pgyer._api_key
        if (uKey == null || _api_key == null) {
            throw new GradleException("uKey or apiKey is missing")
        }
        String endPoint = API_END_POINT + "/app/upload"
        return endPoint
    }

    private static HttpClient getHttpClient() {
        HttpClient httpClient = new DefaultHttpClient()

        ProxySelectorRoutePlanner routePlanner =
            new ProxySelectorRoutePlanner(httpClient.getConnectionManager().getSchemeRegistry(), ProxySelector.getDefault());
        httpClient.setRoutePlanner(routePlanner);

        return httpClient;
    }

    private HashMap<String, JSONObject> httpPost(String endPoint, List<Apk> apks) {
        HashMap<String, JSONObject> result = new HashMap<String, JSONObject>()
        for(Apk apk in apks) {
            HttpClient httpClient = getHttpClient()
            HttpPost httpPost = new HttpPost(endPoint)

            MultipartEntity request_entity = new MultipartEntity()
            Charset charset = Charset.forName(HTTP.UTF_8)

            File file = apk.file
            request_entity.addPart("file", new FileBody(file.getAbsoluteFile()))
            request_entity.addPart("_api_key", new StringBody(project.pgyer._api_key, charset))
            request_entity.addPart("uKey", new StringBody(project.pgyer.uKey))

            HashMap<String, String> params = apk.getParams()
            for (String key : params.keySet()) {
                println("add part key: " + key + " value: " + params.get(key))
                request_entity.addPart(key, new StringBody(params.get(key), charset))
            }



            httpPost.setEntity(request_entity)

            HttpResponse response = httpClient.execute(httpPost)
            HttpEntity entity = response.getEntity()

            if (entity != null) {
                InputStream is = entity.getContent()
                BufferedReader reader = new BufferedReader(new InputStreamReader(is))
                JSONObject json = new JSONObject(reader.readLine())
                result.put(apk.name, json)
                try {
                } finally {
                    is.close()
                }
            }
        }
        return result
    }
}
