package connection;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClient {

	public String getHTTPRequest(String url){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		String response = null;
		
		try {
			ResponseHandler<String> handler = new BasicResponseHandler();
			response = httpClient.execute(httpGet , handler);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
		
	}
}
