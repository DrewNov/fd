
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String doctorsApiUrl = "https://helsi.me/api/healthy/v2/doctors?cityDistrictId=25&limit=50&page=1&propertyTypes=state&ratings=good%2Cexcellent&settlement=1&sort=ratingDesc&specialityId=specialityId_46";
        String docApiUrl = "https://helsi.me/api/healthy/doctors/";
        String docUrl = "https://helsi.me/doctor/";

        HttpResponse resp1 = client.execute(new HttpGet(doctorsApiUrl));
        JSONArray doctors = new JSONObject(inputStreamToString(resp1.getEntity().getContent())).getJSONArray("data");

        for (Object doctor : doctors) {
            String resourceId = new JSONObject(doctor.toString()).getString("resourceId");
            HttpResponse resp2 = client.execute(new HttpGet(docApiUrl + resourceId));
            JSONObject doc = new JSONObject(inputStreamToString(resp2.getEntity().getContent()));

            if (!"Unavailable".equalsIgnoreCase(doc.getString("declarationSignStatus"))) {
                System.out.println(docUrl + resourceId);
            }
        }
    }

    private static String inputStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        inputStream.close();

        return result.toString("UTF-8");
    }
}
