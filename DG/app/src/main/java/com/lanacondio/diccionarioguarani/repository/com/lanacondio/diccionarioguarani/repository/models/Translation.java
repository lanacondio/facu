package com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models;

/**
 * Created by lanacondio on 20/10/2016.
 */

public class Translation {

    private Integer Id;
    private Integer CountryId;
    private String Word;
    private String Translation;
    private String Context;
    private String Type;

    public Integer getId(){return this.Id;}
    public void setId(Integer value){this.Id = value;}

    public Integer getCountryId(){return  this.CountryId;}
    public void setCountryId(Integer value){this.CountryId = value;}

    public String getWordToFind(){return  this.Word;}
    public void setWordToFind(String value){this.Word = value;}

    public String getTranslationResult(){return  this.Translation;}
    public void setTranslationResult(String value){this.Translation = value;}

    public String getContext(){return  this.Context;}
    public void setContext(String value){this.Context = value;}

    public String getType(){return  this.Type;}
    public void setType(String value){this.Type = value;}

}
