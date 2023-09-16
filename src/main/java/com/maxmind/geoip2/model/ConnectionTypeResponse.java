package com.maxmind.geoip2.model;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;

/**
 * This class provides the GeoIP2 Connection-Type model.
 */
public class ConnectionTypeResponse {

    /**
     * The enumerated values that connection-type may take.
     */
    public enum ConnectionType {
        DIALUP("Dialup"), CABLE_DSL("Cable/DSL"), CORPORATE("Corporate"), CELLULAR(
                "Cellular");

        private final String name;

        ConnectionType(String name) {
            this.name = name;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return this.name;
        }

        public static ConnectionType fromString(String s) {
            if (s == null) {
                return null;
            }

            switch (s) {
                case "Dialup":
                    return ConnectionType.DIALUP;
                case "Cable/DSL":
                    return ConnectionType.CABLE_DSL;
                case "Corporate":
                    return ConnectionType.CORPORATE;
                case "Cellular":
                    return ConnectionType.CELLULAR;
                default:
                    return null;
            }
        }
    }

    private final ConnectionType connectionType;
    private final String ipAddress;
    private final Network network;

    public ConnectionTypeResponse(
             ConnectionType connectionType,
           String ipAddress,
            Network network
    ) {
        this.connectionType = connectionType;
        this.ipAddress = ipAddress;
        this.network = network;
    }

    @MaxMindDbConstructor
    public ConnectionTypeResponse(
            @MaxMindDbParameter(name = "connection_type") String connectionType,
            @MaxMindDbParameter(name = "ip_address") String ipAddress,
            @MaxMindDbParameter(name = "network") Network network
    ) {
        this(
                ConnectionType.fromString(connectionType),
                ipAddress,
                network
        );
    }

    public ConnectionTypeResponse(
            ConnectionTypeResponse response,
            String ipAddress,
            Network network
    ) {
        this(
                response.getConnectionType(),
                ipAddress,
                network
        );
    }

    /**
     * @return The connection type of the IP address.
     */
    
    public ConnectionType getConnectionType() {
        return this.connectionType;
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
