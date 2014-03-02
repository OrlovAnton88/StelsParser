package entities;

import java.util.LinkedList;

public class Bicycle {


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    private int price;
    private LinkedList<String> colors = new LinkedList<String>();
    private String trademark;
    private String model;
    private String wheelsSize;
    private int frameSize;
    private String frame;
    private int speedsNum;
    private String frontFork;
    private String steeringTube;
    /* Каретка */
    private String bottomBracket;
    /* шатуны */
    private String Crankset;
    private String rearHub;
    private String frontHub;
    /*Трешетка*/
    private String freeWheel;

    private String frontDerailleur;
    private String rearDerailleur;
    private String shifters;
    private String breaks;
    /*обода*/
    private String rims;
    private String tyres;
    /*Крылья*/
    private String fenders;
    private String pedals;
    private String saddle;
    /*багажник*/
    private String rack;

    public  String getRack(){
        return  rack;
    }
    public void setRack(String rack){
        this.rack = rack;
    }

    public LinkedList<String> getColors(){
        return colors;
    }
    public void setColors(String str){
        colors.add(str);
    }

    public String getTrademark() {
        return trademark;
    }

    public void setTrademark(String trademark) {
        this.trademark = trademark;
    }

    public String getModel() {
        return model;
    }

    /**
     * Converts to lover case and sets first letter UpperCase
     * @param model
     */
    public void setModel(String model) {
        //todo: refactor
//        String result = model.toLowerCase();
//        result = model.substring(0,1).toUpperCase() + result.substring(1,model.length());
//        result = result.replace("lady", "Lady");
//        if(result.contains("Energy")){
//            result = result.replace(" iii", " III");
//            result = result.replace(" i", " I");
//        }
//        result = result.replace("boy", "Boy");
//        result = result.replace("girl", "Girl");

        this.model = model;
    }

    public String getWheelsSize() {
        return wheelsSize;
    }

    public void setWheelsSize(String wheelsSize) {
        this.wheelsSize = wheelsSize;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public void setFrameSize(int frameSize) {
        this.frameSize = frameSize;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public int getSpeedsNum() {
        return speedsNum;
    }

    public void setSpeedsNum(int speedsNum) {
        this.speedsNum = speedsNum;
    }

    public String getFrontFork() {
        return frontFork;
    }

    public void setFrontFork(String frontFork) {
        this.frontFork = frontFork;
    }

    public String getSteeringTube() {
        return steeringTube;
    }

    public void setSteeringTube(String steeringTube) {
        this.steeringTube = steeringTube;
    }

    public String getBottomBracket() {
        return bottomBracket;
    }

    public void setBottomBracket(String bottomBracket) {
        this.bottomBracket = bottomBracket;
    }

    public String getCrankset() {
        return Crankset;
    }

    public void setCrankset(String crankset) {
        Crankset = crankset;
    }

    public String getRearHub() {
        return rearHub;
    }

    public void setRearHub(String rearHub) {
        this.rearHub = rearHub;
    }

    public String getFrontHub() {
        return frontHub;
    }

    public void setFrontHub(String frontHub) {
        this.frontHub = frontHub;
    }

    public String getFreeWheel() {
        return freeWheel;
    }

    public void setFreeWheel(String freeWheel) {
        this.freeWheel = freeWheel;
    }

    public String getFrontDerailleur() {
        return frontDerailleur;
    }

    public void setFrontDerailleur(String frontDerailleur) {
        this.frontDerailleur = frontDerailleur;
    }

    public String getRearDerailleur() {
        return rearDerailleur;
    }

    public void setRearDerailleur(String rearDerailleur) {
        this.rearDerailleur = rearDerailleur;
    }

    public String getShifters() {
        return shifters;
    }

    public void setShifters(String shifters) {
        this.shifters = shifters;
    }

    public String getBreaks() {
        return breaks;
    }

    public void setBreaks(String breaks) {
        this.breaks = breaks;
    }

    public String getRims() {
        return rims;
    }

    public void setRims(String rims) {
        this.rims = rims;
    }

    public String getTyres() {
        return tyres;
    }

    public void setTyres(String tyres) {
        this.tyres = tyres;
    }

    public String getFenders() {
        return fenders;
    }

    public void setFenders(String fenders) {
        this.fenders = fenders;
    }

    public String getPedals() {
        return pedals;
    }

    public void setPedals(String pedals) {
        this.pedals = pedals;
    }

    public String getSaddle() {
        return saddle;
    }

    public void setSaddle(String saddle) {
        this.saddle = saddle;
    }


    public String toString(){
        StringBuffer s = new StringBuffer("Bicycle  - model[");
        s.append(getModel());
        s.append("] wheel size[");
        s.append(getWheelsSize());
        s.append("] number of speed [");
        s.append(getSpeedsNum());
        s.append("] frame [");
        s.append(getFrame());
        s.append("] breaks [");
        s.append(getBreaks());
        //todo: finish
        s.append(']');

        return  s.toString();

    }


}
