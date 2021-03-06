package com.mycompany.myapp.web.rest;

import javax.net.ssl.SSLContext;
import java.io.IOException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class DataResource
{
	private static final String
			METAR_API = "https://avwx.rest/api/metar/%1$s",
			TAF_API = "https://avwx.rest/api/taf/%1$s?options=summary",
			NOTAM_API = "https://api.autorouter.aero/v1.0/notam?itemas=[%1$s]&offset=0&limit=10",
			LORAMOTE_API = "https://loramote_ricm.data.thethingsnetwork.org/api/v2/query/%1$s?last=";

	private final Logger log = LoggerFactory.getLogger(DataResource.class);

	private final RestTemplate standardRest = getStandardTemplate();
	private final RestTemplate loraRest = getLoRaRestTemplate();

	private ResponseEntity<String> common(String airports, String api)
	{
		if(airports == null || airports.isEmpty())
		{
			return ResponseEntity.ok("[]");
		}

		JSONArray a = new JSONArray();

		String[] array = airports.split(",");

		Arrays.stream(array).filter(s -> !s.isEmpty()).forEachOrdered(s ->
		{
			ResponseEntity<String> res = standardRest.getForEntity(String.format(api, s), String.class);

			if(res.getStatusCode() == HttpStatus.OK && res.hasBody())
			{
				try
				{
					JSONObject obj = new JSONObject(res.getBody());

					obj.put("oaci", s);

					a.put(obj);
				}
				catch(JSONException ignored)
				{
					// res is never null, and if it is it must be ignored
				}
			}
		});

		return ResponseEntity.ok(a.toString());
	}

	/**
	 * GET  /metar/:airports : Get METAR data.
	 *
	 * @param airports the airport codes, comma separated
	 * @return the ResponseEntity with status 200 (Ok) and with body the data, or with status 400 (Bad Request) if something weird happens
	 */
	@GetMapping(value = "/metar/{airports}", produces = MediaType.APPLICATION_JSON_VALUE)
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
	@GetMapping(value = "/taf/{airports}", produces = MediaType.APPLICATION_JSON_VALUE)
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
	@GetMapping(value = "/notam/{airports}", produces = MediaType.APPLICATION_JSON_VALUE)
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
	 * @param deviceId the LoRaMote id, ideally tied to a specific plane
	 * @return the ResponseEntity with status 200 (Ok) and with body the data, or with status 400 (Bad Request) if something weird happens
	 */
	@GetMapping(value = "/loramote/{deviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<String> getLoramote(@PathVariable String deviceId, @RequestParam(required = false, value = "n") String n)
	{
		int id;

		try
		{
			id = Integer.parseInt(deviceId);
		}
		catch(NumberFormatException e)
		{
			return ResponseEntity.badRequest().body("The given devide ID is not a number.");
		}

		if(id <= 0)
		{
			return ResponseEntity.badRequest().body("The devide ID must be positive.");
		}

		int sampleSize;

		if(n == null)
		{
			sampleSize = 1;
		}
		else
		{
			try
			{
				sampleSize = Integer.parseInt(n);

				if(sampleSize <= 0)
				{
					log.debug("Received invalid sample size, value will be set to default (1)");

					sampleSize = 1;
				}
			}
			catch(NumberFormatException e)
			{
				log.debug("Received invalid sample size, value will be set to default (1)");

				sampleSize = 1;
			}
		}

		if(id == 1)
		{
			String devideId = "loramote_01";

			String data = loraRest.getForObject(String.format(LORAMOTE_API, devideId), String.class);

			if(data == null || data.isEmpty())
			{
				return ResponseEntity.noContent().build();
			}

			try
			{
				JSONArray a = new JSONArray(data);
				int size = a.length();

				JSONArray result = new JSONArray();

				// retrieves the most recent payloads, i.e. the last elements
				for(int i = 1; i <= size && i <= sampleSize; i++)
				{
					JSONObject payload = (JSONObject) a.get(size - i);

					JSONObject converted = new JSONObject();

					JSONObject board = new JSONObject();
					board.put("led", payload.get("led"));
					board.put("battery", payload.get("battery"));
					converted.put("board", board);

					JSONObject env = new JSONObject();
					env.put("temperature", payload.get("temperature"));
					env.put("pressure", payload.get("pressure"));
					env.put("altitude", payload.get("bar_altitude"));
					converted.put("env", env);

					JSONObject gps = new JSONObject();
					gps.put("latitude", payload.get("latitude"));
					gps.put("longitude", payload.get("longitude"));
					gps.put("altitude", payload.get("altitude"));
					converted.put("gps", gps);

					JSONObject meta = new JSONObject();
					meta.put("device_id", payload.get("device_id"));
					meta.put("raw", payload.get("raw"));
					meta.put("time", payload.get("time"));
					converted.put("meta", meta);

					result.put(converted);
				}

				return ResponseEntity.ok(result.toString());
			}
			catch(JSONException | ClassCastException e)
			{
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
			}
		}

		return ResponseEntity.notFound().build();
	}

	private static RestTemplate getStandardTemplate()
	{
		ResponseErrorHandler errHandler = new DefaultResponseErrorHandler()
		{
			@Override
			public void handleError(ClientHttpResponse response) throws IOException
			{
				HttpStatus statusCode = getHttpStatusCode(response);

				switch(statusCode.series())
				{
					case CLIENT_ERROR:
					case SERVER_ERROR:
						return;
					default:
						throw new RestClientException("Unknown status code [" + statusCode + "]");
				}
			}
		};

		return new RestTemplateBuilder().errorHandler(errHandler).build();
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

			headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
			headers.add(HttpHeaders.AUTHORIZATION, "key " + ttnStorageKey);

			return execution.execute(request, body);
		};

		return new RestTemplateBuilder().requestFactory(requestFactory).interceptors(requestInterceptor).build();
	}
}
