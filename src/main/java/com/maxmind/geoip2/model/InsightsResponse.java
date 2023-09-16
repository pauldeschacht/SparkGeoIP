package com.maxmind.geoip2.model;

import com.maxmind.geoip2.record.*;

import java.util.List;

/**
 * This class provides a model for the data returned by the Insights web
 * service.
 *
 * @see <a href="https://dev.maxmind.com/geoip/docs/web-services?lang=en">GeoIP2 Web
 * Services</a>
 */
public class InsightsResponse extends AbstractCityResponse {
    public InsightsResponse(
             City city,
             Continent continent,
             Country country,
             Location location,
             MaxMind maxmind,
             Postal postal,
             Country registeredCountry,
             RepresentedCountry representedCountry,
             List<Subdivision> subdivisions,
            Traits traits
    ) {
        super(city, continent, country, location, maxmind, postal, registeredCountry,
                representedCountry, subdivisions, traits);
    }
}
