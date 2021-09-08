package edu.escuelaing.arep.HttpServer;

public class WeatherHttpStockService extends HttpStockService{

    String stock="london";

    @Override
    public String getURL() {
        return "api.openweathermap.org/data/2.5/weather?q="+stock+"65d3dc12b4e7da8442638198b0e9f716M";
    }

    @Override
    public void setStock(String stock) {
        this.stock=stock;
    }

    @Override
    public String getStock() {
        return stock;
    }
}