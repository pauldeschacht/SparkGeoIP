package com.maxmind.geoip2.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract class for records with name maps.
 */
public abstract class AbstractNamedRecord {

    private final Map<String, String> names;
    private final Long geoNameId;
    private final List<String> locales;

    AbstractNamedRecord() {
        this(null, null, null);
    }

    AbstractNamedRecord(List<String> locales, Long geoNameId, Map<String, String> names) {
        this.names = names != null ? names : new HashMap<>();
        this.geoNameId = geoNameId;
        this.locales = locales != null ? locales : new ArrayList<>();
    }

    /**
     * @return The GeoName ID for the city. This attribute is returned by all
     * end points.
     */
    public Long getGeoNameId() {
        return this.geoNameId;
    }

    /**
     * @return The name of the city based on the locales list passed to the
     * {@link com.maxmind.geoip2.WebServiceClient} constructor. This
     * attribute is returned by all end points.
     */
    public String getName() {
        for (String lang : this.locales) {
            if (this.names.containsKey(lang)) {
                return this.names.get(lang);
            }
        }
        return null;
    }

    /**
     * @return A {@link Map} from locale codes to the name in that locale. This
     * attribute is returned by all end points.
     */
    public Map<String, String> getNames() {
        return new HashMap<>(this.names);
    }
}
