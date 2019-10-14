

package com.revolut.transferservice.api.currencies;

import lombok.EqualsAndHashCode;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.revolut.transferservice.api.classes.Validator;
import com.revolut.transferservice.api.regions.UKRegion;
import com.revolut.transferservice.api.regions.USRegion;

@EqualsAndHashCode
public class BaseCurrency implements Currency {
	
	private static final String USD = "USD";
	private static final String GBP = "GBP";

	private static final ConcurrentMap<String, Currency> CURRENCIES = new ConcurrentHashMap<>();

	private final String isoCode;
	private final String country;

	BaseCurrency(String isoCode, String country) {
		this.isoCode = isoCode;
		this.country = country;
	}

	@Override
	public boolean isValid() {
		return isoCode.length() == ISO_CODE_LENGTH;
	}

	@Override
	public String getISOCode() {
		return isoCode;
	}
	
	public String getCountry() {
		return country;
	}

	public static Currency valueOf(String isoCode) {
		Objects.requireNonNull(isoCode, "Currency code cannot be null");
		Validator.validateCurrencyCode(isoCode);

		final String code = isoCode.toUpperCase();
		return CURRENCIES.get(code);
	}

	public static Currency getInvalid() {
		return InvalidCurrency.getInstance();
	}

	public static Currency getDefault() {
		return valueOf("GBP");
	}

	@Override
	public String toString() {
		return "BaseCurrency(isoCode=" + this.isoCode + ", country=" + country + ")";
	}
	
	static {
		
		// Register supported currencies
		CURRENCIES.put(GBP, new BaseCurrency(GBP, UKRegion.ISO_CODE));
		CURRENCIES.put(USD, new BaseCurrency(USD, USRegion.ISO_CODE));
	}
}
