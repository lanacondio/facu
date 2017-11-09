package com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanacondio on 20/10/2016.
 */

public class Translation {

    private Integer _id;
    private Integer language_id;
    private String word;
    private String translation;
    private String context;
    private String type;
    private String device_id;
    private Integer web_id;
    private List<Evaluation> evaluations;

    public Integer getId(){return this._id;}
    public void setId(Integer value){this._id = value;}

    public Integer getWebId(){return this.web_id;}
    public void setWebId(Integer value){this.web_id = value;}

    public Integer getCountryId(){return  this.language_id;}
    public void setCountryId(Integer value){this.language_id = value;}

    public String getWordToFind(){return  this.word;}
    public void setWordToFind(String value){this.word = value;}

    public String getTranslationResult(){return  this.translation;}
    public void setTranslationResult(String value){this.translation = value;}

    public String getContext(){return  this.context;}
    public void setContext(String value){this.context = value;}

    public String getType(){return  this.type;}
    public void setType(String value){this.type = value;}

    public String getDeviceId(){return  this.device_id;}
    public void setDeviceId(String value){this.device_id = value;}

    public List<Evaluation> getEvaluations(){if(this.evaluations == null){
        this.evaluations = new ArrayList<Evaluation>();
    }
        return this.evaluations;
    }

    public void setEvaluations(List<Evaluation> eval){this.evaluations= eval;}

    public Integer getPositiveEvaluationsAverage()
    {
        int result = 0;
        for (int i=0; i< getEvaluations().size(); i++){
            int point = getEvaluations().get(i).getPoints();
            if(point>0){
                result ++;
            }
        }
        return result;
    }

    public Integer getNegativeEvaluationsAverage()
    {
        int result = 0;
        for (int i=0; i< getEvaluations().size(); i++){
            int point = getEvaluations().get(i).getPoints() * -1;
            if(point>0){
                result ++;
            }

        }
        return result;
    }
}
