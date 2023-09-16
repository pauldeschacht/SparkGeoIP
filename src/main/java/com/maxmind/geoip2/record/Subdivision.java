package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Contains data for the subdivisions associated with an IP address.
 * </p>
 * <p>
 * This record is returned by all the end points except the Country end point.
 * </p>
 * <p>
 * Do not use any of the subdivision names as a database or map key. Use the
 * value returned by {@link #getGeoNameId} or {@link #getIsoCode} instead.
 * </p>
 */
public final class Subdivision extends AbstractNamedRecord {

    private final Integer confidence;
    private final String isoCode;

    public Subdivision() {
        this(null, null, null, null, null);
    }

    @MaxMindDbConstructor
    public Subdivision(
            @JacksonInject("locales") @MaxMindDbParameter(name = "locales") List<String> locales,
             @MaxMindDbParameter(name = "confidence") Integer confidence,
             @MaxMindDbParameter(name = "geoname_id") Long geoNameId,
             @MaxMindDbParameter(name = "iso_code") String isoCode,
             @MaxMindDbParameter(name = "names") Map<String, String> names
    ) {
        super(locales, geoNameId, names);
        this.confidence = confidence;
        this.isoCode = isoCode;
    }

    public Subdivision(
            Subdivision subdivision,
            List<String> locales
    ) {
        this(
                locales,
                subdivision.getConfidence(),
                subdivision.getGeoNameId(),
                subdivision.getIsoCode(),
                subdivision.getNames()
        );
    }

    /**
     * @return This is a value from 0-100 indicating MaxMind's confidence that
     * the subdivision is correct. This attribute is only available from
     * the Insights end point and the GeoIP2 Enterprise database.
     */
    
    public Integer getConfidence() {
        return this.confidence;
    }

    /**
     * @return This is a string up to three characters long contain the
     * subdivision portion of the <a
     * href="https://en.wikipedia.org/wiki/ISO_3166-2">ISO
     * 3166-2code</a>. This attribute is returned by all end points
     * except Country.
     */
    
    public String getIsoCode() {
        return this.isoCode;
    }
}
