package com.maxmind.geoip2.model;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;

/**
 * This class provides the GeoLite2 ASN model.
 */
public class AsnResponse {

    private final Long autonomousSystemNumber;
    private final String autonomousSystemOrganization;
    private final String ipAddress;
    private final Network network;

    @MaxMindDbConstructor
    public AsnResponse(
             @MaxMindDbParameter(name = "autonomous_system_number") Long autonomousSystemNumber,
             @MaxMindDbParameter(name = "autonomous_system_organization") String autonomousSystemOrganization,
            @MaxMindDbParameter(name = "ip_address") String ipAddress,
             @MaxMindDbParameter(name = "network") Network network
    ) {
        this.autonomousSystemNumber = autonomousSystemNumber;
        this.autonomousSystemOrganization = autonomousSystemOrganization;
        this.ipAddress = ipAddress;
        this.network = network;
    }

    public AsnResponse(
            AsnResponse response,
            String ipAddress,
            Network network
    ) {
        this(
                response.getAutonomousSystemNumber(),
                response.getAutonomousSystemOrganization(),
                ipAddress,
                network
        );
    }

    /**
     * @return The autonomous system number associated with the IP address.
     */
    
    public Long getAutonomousSystemNumber() {
        return this.autonomousSystemNumber;
    }

    /**
     * @return The organization associated with the registered autonomous system
     * number for the IP address
     */
    
    public String getAutonomousSystemOrganization() {
        return this.autonomousSystemOrganization;
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
