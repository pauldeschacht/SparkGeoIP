package com.maxmind.geoip2.model;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.record.*;

import java.util.List;

/**
 * This class provides a model for the data returned by the Country web service
 * and the Country database.
 *
 * @see <a href="https://dev.maxmind.com/geoip/docs/web-services?lang=en">GeoIP2 Web
 * Services</a>
 */
public final class CountryResponse extends AbstractCountryResponse {
    @MaxMindDbConstructor
    public CountryResponse(
             @MaxMindDbParameter(name = "continent") Continent continent,
             @MaxMindDbParameter(name = "country") Country country,
             @MaxMindDbParameter(name = "maxmind") MaxMind maxmind,
             @MaxMindDbParameter(name = "registered_country") Country registeredCountry,
             @MaxMindDbParameter(name = "represented_country") RepresentedCountry representedCountry,
            @MaxMindDbParameter(name = "traits") Traits traits
    ) {
        super(continent, country, maxmind, registeredCountry, representedCountry, traits);
    }

    public CountryResponse(
            CountryResponse response,
            String ipAddress,
            Network network,
            List<String> locales
    ) {
        super(response, ipAddress, network, locales);
    }
}
