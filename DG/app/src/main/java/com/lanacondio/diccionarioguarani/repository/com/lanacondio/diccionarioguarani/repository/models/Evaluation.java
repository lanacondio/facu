package com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models;

/**
 * Created by lanacondio on 20/10/2016.
 */

public class Evaluation {

    private Integer Id;
    private Integer Points;
    private Integer IdTranslation;
    private String DeviceId;

    public Integer getId(){return this.Id;}
    public void setId(Integer value){this.Id = value;}

    public Integer getIdTranslation(){return this.IdTranslation;}
    public void setIdTranslation(Integer value){this.IdTranslation = value;}

    public Integer getPoints(){return  this.Points;}
    public void setPoints(Integer value){this.Points = value;}

    public String getDeviceId(){return  this.DeviceId;}
    public void setDeviceId(String value){this.DeviceId = value;}

}
