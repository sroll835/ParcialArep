package edu.escuelaing.arep.HttpServer;

public class CurrentServiceInstance {
    private static CurrentServiceInstance _instance = new CurrentServiceInstance();
    private HttpStockService serviceWeather;

    private CurrentServiceInstance(){
        serviceWeather= new WeatherHttpStockService();
    }

    public static CurrentServiceInstance getInstance(){
        return _instance;
    }

    public HttpStockService getServiceAlpha(){
        return serviceWeather;
    }

}
