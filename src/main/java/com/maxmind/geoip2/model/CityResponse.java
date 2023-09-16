package com.maxmind.geoip2.model;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.record.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a model for the data returned by the City Plus web
 * service and the City database.
 *
 * @see <a href="https://dev.maxmind.com/geoip/docs/web-services?lang=en">GeoIP2 Web
 * Services</a>
 */
public final class CityResponse extends AbstractCityResponse {
    @MaxMindDbConstructor
    public CityResponse(
             @MaxMindDbParameter(name = "city") City city,
             @MaxMindDbParameter(name = "continent") Continent continent,
             @MaxMindDbParameter(name = "country") Country country,
             @MaxMindDbParameter(name = "location") Location location,
             @MaxMindDbParameter(name = "maxmind") MaxMind maxmind,
             @MaxMindDbParameter(name = "postal") Postal postal,
             @MaxMindDbParameter(name = "registered_country") Country registeredCountry,
             @MaxMindDbParameter(name = "represented_country") RepresentedCountry representedCountry,
             @MaxMindDbParameter(name = "subdivisions") ArrayList<Subdivision> subdivisions,
            @MaxMindDbParameter(name = "traits") Traits traits
    ) {
        super(city, continent, country, location, maxmind, postal, registeredCountry,
                representedCountry, subdivisions, traits);
    }

    public CityResponse(
            CityResponse response,
            String ipAddress,
            Network network,
            List<String> locales
    ) {
        super(response, ipAddress, network, locales);
    }
}
