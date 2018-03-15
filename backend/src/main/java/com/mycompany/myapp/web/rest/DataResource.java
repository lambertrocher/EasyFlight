package com.mycompany.myapp.web.rest;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.codahale.metrics.annotation.Timed;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class DataResource
{
	private static final String
			METAR_API = "https://avwx.rest/api/metar/%1$s",
			TAF_API = "https://avwx.rest/api/taf/%1$s?options=summary",
			NOTAM_API = "https://api.autorouter.aero/v1.0/notam?itemas=[%1$s]&offset=0&limit=10",
			LORAMOTE_API = "https://loramote_ricm.data.thethingsnetwork.org/api/v2/query/%1$s";

	private final RestTemplate standardRest = new RestTemplate();
	private final RestTemplate loraRest = getLoRaRestTemplate();

	private ResponseEntity<String> common(String airports, String api)
	{
		if(airports == null || airports.isEmpty())
		{
			return ResponseEntity.ok("{}");
		}

		JSONObject o = new JSONObject();

		String[] array = airports.split(",");

		Arrays.stream(array).filter(s -> !s.isEmpty()).forEachOrdered(s ->
		{
			String res = standardRest.getForObject(String.format(api, s), String.class);

			try
			{
				JSONObject obj = new JSONObject(res);

				o.put(s, obj);
			}
			catch(JSONException ignored)
			{
				// res is never null, and if it is it must be ignored
			}
		});

		return ResponseEntity.ok(o.toString());
	}

	/**
	 * GET  /metar/:airports : Get METAR data.
	 *
	 * @param airports the airport codes, comma separated
	 * @return the ResponseEntity with status 200 (Ok) and with body the data, or with status 400 (Bad Request) if something weird happens
	 */
	@GetMapping("/metar/{airports}")
	@Timed
	public ResponseEntity<String> getMetar(@PathVariable String airports)
	{
		return common(airports, METAR_API);
	}

	/**
	 * GET  /taf/:airports : Get TAF data.
	 *
	 * @param airports the airport codes, comma separated
	 * @return the ResponseEntity with status 200 (Ok) and with body the data, or with status 400 (Bad Request) if something weird happens
	 */
	@GetMapping("/taf/{airports}")
	@Timed
	public ResponseEntity<String> getTaf(@PathVariable String airports)
	{
		return common(airports, TAF_API);
	}

	/**
	 * GET  /notam/:airports : Get NOTAM data.
	 *
	 * @param airports the airport codes, comma separated
	 * @return the ResponseEntity with status 200 (Ok) and with body the data, or with status 400 (Bad Request) if something weird happens
	 */
	@GetMapping("/notam/{airports}")
	@Timed
	public ResponseEntity<String> getNotam(@PathVariable String airports)
	{
		if(airports == null || airports.isEmpty())
		{
			return ResponseEntity.ok("{\"total\":0,\"rows\":[]}");
		}

		String[] array = airports.split(",");

		String query = Arrays.stream(array).filter(s -> !s.isEmpty()).map(s -> "\"" + s + "\"").collect(Collectors.joining(","));

		return standardRest.getForEntity(String.format(NOTAM_API, query), String.class);
	}

	/**
	 * GET  /loramote/:id : Get LoRaMote data.
	 *
	 * @param id the LoRaMote id, ideally tied to a specific plane
	 * @return the ResponseEntity with status 200 (Ok) and with body the data, or with status 400 (Bad Request) if something weird happens
	 */
	@GetMapping("/loramote/{id}")
	@Timed
	public ResponseEntity<String> getLoramote(@PathVariable Integer id)
	{
		if(id == null || id <= 0)
		{
			return ResponseEntity.badRequest().build();
		}

		if(id == 1)
		{
			String devideId = "loramote_01";

			String data = loraRest.getForObject(String.format(LORAMOTE_API, devideId), String.class);

			if(data != null)
			{
				if(data.isEmpty())
				{
					return ResponseEntity.ok("{}");
				}

				try
				{
					JSONArray a = new JSONArray(data);

					// retrieves the most recent payload, i.e. the last element
					JSONObject o = (JSONObject) a.get(a.length() - 1);

					JSONObject result = new JSONObject();

					JSONObject board = new JSONObject();
					board.put("led", o.get("led"));
					board.put("battery", o.get("battery"));
					result.put("board", board);

					JSONObject env = new JSONObject();
					env.put("temperature", o.get("temperature"));
					env.put("pressure", o.get("pressure"));
					env.put("altitude", o.get("bar_altitude"));
					result.put("env", env);

					JSONObject gps = new JSONObject();
					gps.put("latitude", o.get("latitude"));
					gps.put("longitude", o.get("longitude"));
					gps.put("altitude", o.get("altitude"));
					result.put("gps", gps);

					JSONObject meta = new JSONObject();
					meta.put("device_id", o.get("device_id"));
					meta.put("raw", o.get("raw"));
					meta.put("time", o.get("time"));
					result.put("meta", meta);

					return ResponseEntity.ok(result.toString());
				}
				catch(JSONException | ClassCastException e)
				{
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
				}
			}
		}

		return ResponseEntity.notFound().build();
	}

	private static RestTemplate getLoRaRestTemplate()
	{
		TrustStrategy acceptingTrustStrategy = (x509Certificates, s) -> true;
		SSLContext sslContext;
		try
		{
			sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
		}
		catch(NoSuchAlgorithmException | KeyManagementException | KeyStoreException e)
		{
			throw new RuntimeException(e);
		}
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);

		ClientHttpRequestInterceptor requestInterceptor = (request, body, execution) ->
		{
			String ttnStorageKey = System.getenv("TTN_DATA_KEY");

			if(ttnStorageKey == null)
			{
				throw new RuntimeException("Can't find the TTN key in environment!");
			}

			HttpHeaders headers = request.getHeaders();

			headers.add(HttpHeaders.ACCEPT, "application/json");
			headers.add(HttpHeaders.AUTHORIZATION, "key " + ttnStorageKey);

			return execution.execute(request, body);
		};

		return new RestTemplateBuilder().requestFactory(requestFactory).interceptors(requestInterceptor).build();
	}
}
