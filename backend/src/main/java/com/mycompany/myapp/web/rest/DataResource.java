package com.mycompany.myapp.web.rest;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.codahale.metrics.annotation.Timed;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
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
			NOTAM_API = "https://api.autorouter.aero/v1.0/notam?itemas=[%1$s]&offset=0&limit=10";

	private final RestTemplate rest = new RestTemplate();

	/**
	 * GET  /metar : Get METAR data.
	 *
	 * @param airports the airport codes, comma separated
	 * @return the ResponseEntity with status 200 (Ok) and with body the data, or with status 400 (Bad Request) if something weird happens
	 */
	@GetMapping("/metar/{airports}")
	@Timed
	public ResponseEntity<String> getMetar(@PathVariable String airports)
	{
		if(airports == null || airports.isEmpty())
		{
			return ResponseEntity.ok("{}");
		}

		JSONObject o = new JSONObject();

		String[] array = airports.split(",");

		for(String s : array)
		{
			if(!s.isEmpty())
			{
				String res = rest.getForEntity(String.format(METAR_API, s), String.class).getBody();

				try
				{
					o.put(s, res);
				}
				catch(JSONException ignored)
				{
					// res is never null, and if it is it must be ignored
				}
			}
		}

		return ResponseEntity.ok(o.toString());
	}

	/**
	 * GET  /taf : Get TAF data.
	 *
	 * @param airports the airport codes, comma separated
	 * @return the ResponseEntity with status 200 (Ok) and with body the data, or with status 400 (Bad Request) if something weird happens
	 */
	@GetMapping("/taf/{airports}")
	@Timed
	public ResponseEntity<String> getTaf(@PathVariable String airports)
	{
		if(airports == null || airports.isEmpty())
		{
			return ResponseEntity.ok("{}");
		}

		JSONObject o = new JSONObject();

		String[] array = airports.split(",");

		for(String s : array)
		{
			if(!s.isEmpty())
			{
				String res = rest.getForEntity(String.format(TAF_API, s), String.class).getBody();

				try
				{
					o.put(s, res);
				}
				catch(JSONException ignored)
				{
					// res is never null, and if it is it must be ignored
				}
			}
		}

		return ResponseEntity.ok(o.toString());
	}

	/**
	 * GET  /notam : Get NOTAM data.
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

		return rest.getForEntity(String.format(NOTAM_API, query), String.class);
	}
}
