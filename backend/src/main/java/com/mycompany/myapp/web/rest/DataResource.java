package com.mycompany.myapp.web.rest;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.codahale.metrics.annotation.Timed;
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
	 * @param airport the airport code
	 * @return the ResponseEntity with status 200 (Ok) and with body the data, or with status 400 (Bad Request) if something weird happens
	 */
	@GetMapping("/metar/{airport}")
	@Timed
	public ResponseEntity<String> getMetar(@PathVariable String airport)
	{
		return rest.getForEntity(String.format(METAR_API, airport), String.class);
	}

	/**
	 * GET  /taf : Get TAF data.
	 *
	 * @param airport the airport code
	 * @return the ResponseEntity with status 200 (Ok) and with body the data, or with status 400 (Bad Request) if something weird happens
	 */
	@GetMapping("/taf/{airport}")
	@Timed
	public ResponseEntity<String> getTaf(@PathVariable String airport)
	{
		return rest.getForEntity(String.format(TAF_API, airport), String.class);
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
		String[] array = airports.split(",");

		String query = Arrays.stream(array).map(s -> "\"" + s + "\"").collect(Collectors.joining(","));

		return rest.getForEntity(String.format(NOTAM_API, query), String.class);
	}
}
