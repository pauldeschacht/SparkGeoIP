package com.maxmind.geoip2.model;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;

/**
 * This class provides the GeoIP2 Anonymous IP model.
 */
public class AnonymousIpResponse {

    private final boolean isAnonymous;
    private final boolean isAnonymousVpn;
    private final boolean isHostingProvider;
    private final boolean isPublicProxy;
    private final boolean isResidentialProxy;
    private final boolean isTorExitNode;
    private final String ipAddress;
    private final Network network;

    public AnonymousIpResponse(
           String ipAddress,
             boolean isAnonymous,
             boolean isAnonymousVpn,
             boolean isHostingProvider,
             boolean isPublicProxy,
             boolean isResidentialProxy,
             boolean isTorExitNode,
            Network network
    ) {
        this.isAnonymous = isAnonymous;
        this.isAnonymousVpn = isAnonymousVpn;
        this.isHostingProvider = isHostingProvider;
        this.isPublicProxy = isPublicProxy;
        this.isResidentialProxy = isResidentialProxy;
        this.isTorExitNode = isTorExitNode;
        this.ipAddress = ipAddress;
        this.network = network;
    }

    @MaxMindDbConstructor
    public AnonymousIpResponse(
            @MaxMindDbParameter(name = "ip_address") String ipAddress,
            @MaxMindDbParameter(name = "is_anonymous") Boolean isAnonymous,
            @MaxMindDbParameter(name = "is_anonymous_vpn") Boolean isAnonymousVpn,
            @MaxMindDbParameter(name = "is_hosting_provider") Boolean isHostingProvider,
            @MaxMindDbParameter(name = "is_public_proxy") Boolean isPublicProxy,
            @MaxMindDbParameter(name = "is_residential_proxy") Boolean isResidentialProxy,
            @MaxMindDbParameter(name = "is_tor_exit_node") Boolean isTorExitNode,
            @MaxMindDbParameter(name = "network") Network network
    ) {
        this(
                ipAddress,
                isAnonymous != null ? isAnonymous : false,
                isAnonymousVpn != null ? isAnonymousVpn : false,
                isHostingProvider != null ? isHostingProvider : false,
                isPublicProxy != null ? isPublicProxy : false,
                isResidentialProxy != null ? isResidentialProxy : false,
                isTorExitNode != null ? isTorExitNode : false,
                network
        );
    }

    public AnonymousIpResponse(
            AnonymousIpResponse response,
            String ipAddress,
            Network network
    ) {
        this(
                ipAddress,
                response.isAnonymous(),
                response.isAnonymousVpn(),
                response.isHostingProvider(),
                response.isPublicProxy(),
                response.isResidentialProxy(),
                response.isTorExitNode(),
                network
        );
    }

    /**
     * @return whether the IP address belongs to any sort of anonymous network.
     */
    
    public boolean isAnonymous() {
        return isAnonymous;
    }

    /**
     * @return whether the IP address is registered to an anonymous VPN
     * provider. If a VPN provider does not register subnets under names
     * associated with them, we will likely only flag their IP ranges using
     * isHostingProvider.
     */
    
    public boolean isAnonymousVpn() {
        return isAnonymousVpn;
    }

    /**
     * @return whether the IP address belongs to a hosting or VPN provider
     * (see description of isAnonymousVpn).
     */
    
    public boolean isHostingProvider() {
        return isHostingProvider;
    }

    /**
     * @return whether the IP address belongs to a public proxy.
     */
    
    public boolean isPublicProxy() {
        return isPublicProxy;
    }

    /**
     * @return whether the IP address is on a suspected anonymizing network and
     * belongs to a residential ISP.
     */
    
    public boolean isResidentialProxy() {
        return isResidentialProxy;
    }

    /**
     * @return whether the IP address is a Tor exit node.
     */
    
    public boolean isTorExitNode() {
        return isTorExitNode;
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
