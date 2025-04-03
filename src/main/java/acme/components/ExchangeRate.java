/*
 * ExchangeRate.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.components;

import java.util.Date;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeRate {

	// Attributes -------------------------------------------------------------

	public String				base;
	public Date					date;
	public Map<String, Double>	rates;
	public boolean				success;
	public long					timestamp;


	public static boolean isValidCurrency(final String currency) {
		try {
			RestTemplate api = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("apikey", "Q8Bzt7rnuqtZiRHLQ2joPothdJaUSuX0");
			HttpEntity<String> parameters = new HttpEntity<>("parameters", headers);

			// Consultar la API para verificar si la moneda es válida
			ResponseEntity<ExchangeRate> response = api.exchange("https://api.apilayer.com/exchangerates_data/latest?base={0}", HttpMethod.GET, parameters, ExchangeRate.class, currency);

			return response != null && response.getBody() != null && response.getBody().getRates() != null;
		} catch (Exception e) {
			return false;
		}
	}

}
