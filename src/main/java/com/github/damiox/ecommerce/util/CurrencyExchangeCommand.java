package com.github.damiox.ecommerce.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

/**
 * Interface to define the currency exchange contract.
 *
 * @author dnardelli
 */
public interface CurrencyExchangeCommand {

    double convert(String from, String to, double amount);

    /**
     * API Client for https://fixer.io (free-tier access)
     * Currency 'from' must be EUR.
     * In the future we could perform some operations to support other 'from' values.
     *
     * @author dnardelli
     */
    @Component
    class FixerIOClient implements CurrencyExchangeCommand {
        private static final String CURRENCY_EUR = "EUR";
        private static final RestTemplate REST_TEMPLATE = new RestTemplate();

        @Value("${fixerio.url}")
        private String fixerIOUrl;
        @Value("${fixerio.apiaccesskey}")
        private String fixerIOApiAccessKey;

        @Override
        public double convert(String from, String to, double amount) {
            if (fixerIOApiAccessKey == null
                || fixerIOApiAccessKey.isEmpty()
                || "${fixerio.apiaccesskey}".equals(fixerIOApiAccessKey)) {
                throw new IllegalArgumentException("FixerIO Api Access Key is not set");
            }

            if (!CURRENCY_EUR.equals(to)) {
                throw new IllegalArgumentException("Currently only supported conversion is " + CURRENCY_EUR);
            }

            final FixerIOResponse fixerIOResponse =
                REST_TEMPLATE.getForEntity(URI.create(fixerIOUrl + fixerIOApiAccessKey  ), FixerIOResponse.class).getBody();

            if (fixerIOResponse.getRates() == null) {
                throw new IllegalStateException("No rates were retrieved from Fixer IO");
            }

            return amount * (1.0 / fixerIOResponse.getRates().get(from));
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class FixerIOResponse {
            private Map<String, Double> rates;

            public Map<String, Double> getRates() {
                return rates;
            }

            public void setRates(Map<String, Double> rates) {
                this.rates = rates;
            }
        }
    }

}
