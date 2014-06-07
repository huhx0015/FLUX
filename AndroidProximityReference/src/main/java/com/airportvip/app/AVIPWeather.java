package com.airportvip.app;

/**
 * Created by Michael Yoon Huh on 6/7/2014.
 */
public class AVIPWeather {

    private String destinationName;
    private String arrivalName;
    private String destCurrentWeather;
    private String arriveCurrentWeather;
    private String currentTemp;
    private String arrivalTemp;

    public final static AVIPWeather avipWeather = new AVIPWeather();

    public AVIPWeather() {}

    // initializeWeather(): Initializes AVIPWeather object.
    public void initializeWeather() {

        // Sets the default variables for the DQMaps class.
        destinationName = "San Francisco, CA";
        arrivalName = "Minneapolis-St. Paul, Minnesota";
        destCurrentWeather = "Sunny";
        arriveCurrentWeather = "Partly Cloudy";
        currentTemp = "63° F";
        arrivalTemp = "56° F";
    }

    /** GET METHODS **/

    public String getDestinationName() {

        return this.destinationName;
    }

    public String getArrivalName() {

        return this.arrivalName;
    }

    public String getDestCurrentWeather() {

        return this.destCurrentWeather;
    }

    public String getArriveCurrentWeather() {

        return this.arriveCurrentWeather;
    }

    public String getCurrentTemp() {

        return this.currentTemp;
    }

    public String getArrivalTemp() {

        return this.arrivalTemp;
    }

    /** SET METHODS **/


    public void setDestinationName(String name) {

        this.destinationName = name;
    }

    public void setArrivalName(String name) {

        this.arrivalName = name;
    }

    public void setDestCurrentWeather(String name) {

        this.destCurrentWeather = name;
    }

    public void setCurrentTemp(String name) {

        this.currentTemp = name;
    }

    public void setArrivalTemp(String name) {

        this.arrivalTemp = name;
    }

    public void setArriveCurrentWeather(String name) {

        this.arriveCurrentWeather = name;
    }
}
