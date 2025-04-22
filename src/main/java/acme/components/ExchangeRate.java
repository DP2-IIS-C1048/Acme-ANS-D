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

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeRate {

	public static boolean isValidCurrency(final String currency) {
		List<String> monedas = Arrays.asList("USD", "EUR", "JPY", "GBP", "CHF", "CAD", "AUD", "CNY", "MXN", "BRL", "RUB", "INR", "KRW", "ZAR", "SAR", "ARS", "COP", "CLP", "TRY", "EGP");
		return monedas.contains(currency.toUpperCase());
	}

}
