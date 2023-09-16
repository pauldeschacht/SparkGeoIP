package com.maxmind.geoip2.model;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.record.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class provides a model for the data returned by the GeoIP2 Enterprise
 * database
 * </p>
 */
public final class EnterpriseResponse extends AbstractCityResponse {

    @MaxMindDbConstructor
    public EnterpriseResponse(
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

    public EnterpriseResponse(
            EnterpriseResponse response,
            String ipAddress,
            Network network,
            List<String> locales
    ) {
        super(response, ipAddress, network, locales);
    }
}
