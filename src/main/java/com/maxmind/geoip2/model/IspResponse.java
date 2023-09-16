package com.maxmind.geoip2.model;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;

/**
 * This class provides the GeoIP2 ISP model.
 */
public class IspResponse extends AsnResponse {

    private final String isp;
    private final String organization;
    private final String mobileCountryCode;
    private final String mobileNetworkCode;

    @MaxMindDbConstructor
    public IspResponse(
             @MaxMindDbParameter(name = "autonomous_system_number") Long autonomousSystemNumber,
             @MaxMindDbParameter(name = "autonomous_system_organization") String autonomousSystemOrganization,
            @MaxMindDbParameter(name = "ip_address") String ipAddress,
             @MaxMindDbParameter(name = "isp") String isp,
             @MaxMindDbParameter(name = "mobile_country_code") String mobileCountryCode,
             @MaxMindDbParameter(name = "mobile_network_code") String mobileNetworkCode,
             @MaxMindDbParameter(name = "organization") String organization,
             @MaxMindDbParameter(name = "network") Network network
    ) {
        super(autonomousSystemNumber, autonomousSystemOrganization, ipAddress, network);
        this.isp = isp;
        this.mobileCountryCode = mobileCountryCode;
        this.mobileNetworkCode = mobileNetworkCode;
        this.organization = organization;
    }

    public IspResponse(
            IspResponse response,
            String ipAddress,
            Network network
    ) {
        this(
                response.getAutonomousSystemNumber(),
                response.getAutonomousSystemOrganization(),
                ipAddress,
                response.getIsp(),
                response.getMobileCountryCode(),
                response.getMobileNetworkCode(),
                response.getOrganization(),
                network
        );
    }

    /**
     * @return The name of the ISP associated with the IP address.
     */
    public String getIsp() {
        return this.isp;
    }

    /**
     * @return The <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
     * mobile country code (MCC)</a> associated with the IP address and ISP.
     * This property is available from the City and Insights web services and
     * the GeoIP2 Enterprise database.
     */
    
    public String getMobileCountryCode() {
        return this.mobileCountryCode;
    }

    /**
     * @return The <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
     * mobile network code (MNC)</a> associated with the IP address and ISP.
     * This property is available from the City and Insights web services and
     * the GeoIP2 Enterprise database.
     */
    
    public String getMobileNetworkCode() {
        return this.mobileNetworkCode;
    }

    /**
     * @return The name of the organization associated with the IP address.
     */
    public String getOrganization() {
        return this.organization;
    }
}
