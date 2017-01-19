package com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models;

/**
 * Created by lanacondio on 20/10/2016.
 */

public class Favorite {

    private Integer Id;
    private Integer LanguageId;
    private String Word;

    public Integer getId(){return this.Id;}
    public void setId(Integer value){this.Id = value;}

    public Integer getLanguageId(){return  this.LanguageId;}
    public void setLanguageId(Integer value){this.LanguageId = value;}

    public String getWordToFind(){return  this.Word;}
    public void setWordToFind(String value){this.Word = value;}

}
