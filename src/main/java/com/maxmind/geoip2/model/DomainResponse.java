package com.maxmind.geoip2.model;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;

/**
 * This class provides the GeoIP2 Domain model.
 */
public class DomainResponse {

    private final String domain;
    private final String ipAddress;
    private final Network network;

    @MaxMindDbConstructor
    public DomainResponse(
             @MaxMindDbParameter(name = "domain") String domain,
            @MaxMindDbParameter(name = "ip_address") String ipAddress,
            @MaxMindDbParameter(name = "network") Network network
    ) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.network = network;
    }

    public DomainResponse(
            DomainResponse response,
            String ipAddress,
            Network network
    ) {
        this(response.getDomain(), ipAddress, network);
    }

    /**
     * @return The second level domain associated with the IP address. This
     * will be something like "example.com" or "example.co.uk", not
     * "foo.example.com".
     */
    public String getDomain() {
        return this.domain;
    }

    /**
     * @return The IP address that the data in the model is for.
     */
    
    public String getIpAddress() {
        return this.ipAddress;
    }

    /**
     * @return The network associated with the record. In particular, this is
     * the largest network where all the fields besides IP address have the
     * same value.
     */
    public Network getNetwork() {
        return this.network;
    }
}
