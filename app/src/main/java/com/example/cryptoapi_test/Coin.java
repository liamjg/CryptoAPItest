package com.example.cryptoapi_test;

import java.util.Map;

class Coin {
    private Map<String, Price> name;

    public Map<String, Price> getName() {
        return name;
    }

    public void setName(Map name){
        this.name = name;
    }
}
class Price {
    private double USD;
    private double EUR;

    public double getUSD(){
        return this.USD;
    }
    public void setUSD(double value){
        this.USD = value;
    }

    public double getEUR(){
        return this.EUR;
    }
    public void setEUR(double value){
        this.EUR = value;
    }

}

