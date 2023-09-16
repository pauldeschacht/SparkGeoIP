package com.maxmind.geoip2.record;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * City-level data associated with an IP address.
 * </p>
 * <p>
 * This record is returned by all the end points except the Country end point.
 * </p>
 * <p>
 * Do not use any of the city names as a database or map key. Use the value
 * returned by {@link #getGeoNameId} instead.
 * </p>
 */
public final class City extends AbstractNamedRecord {

    private final Integer confidence;

    public City() {
        this(null, null, null, null);
    }

    @MaxMindDbConstructor
    public City(
            @MaxMindDbParameter(name = "locales") List<String> locales,
            @MaxMindDbParameter(name = "confidence") Integer confidence,
            @MaxMindDbParameter(name = "geoname_id") Long geoNameId,
            @MaxMindDbParameter(name = "names") Map<String, String> names
    ) {
        super(locales, geoNameId, names);
        this.confidence = confidence;
    }

    public City(
            City city,
            List<String> locales
    ) {
        this(
                locales,
                city.getConfidence(),
                city.getGeoNameId(),
                city.getNames()
        );
    }

    /**
     * @return A value from 0-100 indicating MaxMind's confidence that the city
     * is correct. This attribute is only available from the Insights
     * end point and the GeoIP2 Enterprise database.
     */
    public Integer getConfidence() {
        return this.confidence;
    }
}
