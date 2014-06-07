package com.airportvip.app;

public class AVIPUser {

    private String vipName;
    private String airportName;
    private String originName;
    private String arrivalName;
    private String etaDestination;
    private String etaDeparture;
    private String etaFlight;
    private String gateNumber;
    private String seatNumber;
    private String flightNumber;

    // AVIPUser(): Constructor for the AVIPUser class.
    public final static AVIPUser avipUser = new AVIPUser();

    // AVIPUser(): Deconstructor for the AVIPUser class.
    public AVIPUser() {}

    // initializeVIP(): Initializes VIP object.
    public void initializeVIP() {

        // Sets the default variables for the DQMaps class.
        vipName = "Tony Hawk";
        airportName = "SFO";
        originName = "San Francisco, California";
        arrivalName = "Minneapolis-St. Paul, Minnesota";
        gateNumber = "GATE A20";
        seatNumber = "SEAT 1A";
        etaDeparture = "12:45 PM PST";
        etaDestination = "6:00 PM CST";
        etaFlight = "0:45";
        flightNumber = "FLIGHT AA 390";
    }

    /** GET FUNCTIONALITY **/

    public String getVipName() {

        return this.vipName;
    }

    public String getAirportName() {

        return this.airportName;
    }

    public String getDestinationName() {

        return this.originName;
    }

    public String getArrivalName() {

        return this.arrivalName;
    }

    public String getEtaDestination() {

        return this.etaDestination;
    }

    public String getEtaDeparture() {

        return this.etaDeparture;
    }

    public String getEtaFlight() {

        return this.etaFlight;
    }

    public String getGateNumber() {

        return this.gateNumber;
    }

    public String getSeatNumber() {

        return this.seatNumber;
    }

    public String getFlightNumber() {

        return this.flightNumber;
    }

    /** SET FUNCTIONALITY **/

    public void setVipName(String name) {

        this.vipName = name;
    }

    public void setAirportName(String name) {

        this.airportName = name;
    }

    public void setDestinationName(String name) {

        this.originName = name;
    }

    public void setArrivalName(String name) {

        this.arrivalName = name;
    }

    public void setEtaDestination(String name) {

        this.etaDestination = name;
    }

    public void setEtaDeparture(String name) {

        this.etaDeparture = name;
    }

    public void setGateNumber(String name) {

        this.gateNumber = name;
    }

    public void setSeatNumber(String name) {

        this.seatNumber = name;
    }

    public void setFlightNumber(String name) {

        this.flightNumber = name;
    }

    public void setEtaFlight(String name) {

        this.etaFlight = name;
    }
}
