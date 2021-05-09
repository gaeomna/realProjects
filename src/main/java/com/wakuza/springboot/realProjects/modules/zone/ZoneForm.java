package com.wakuza.springboot.realProjects.modules.zone;


import lombok.Data;

@Data
public class ZoneForm {

    //TODO Seoul(서울)/None
    //     cityName/localName/provinceName

    private String zoneName;

    public String getCityName(){
        return zoneName.substring(0,zoneName.indexOf("("));
        //Seoul(서울)/None -> Seoul
    }

    public String getProvinceName(){
        return zoneName.substring(zoneName.indexOf("/") + 1);
        //Seoul(서울)/None -> None
    }
    public String getLocalNameOfCity(){
        return zoneName.substring(zoneName.indexOf("(")+1,zoneName.indexOf(")"));
        //Seoul(서울)/None -> 서울
    }

    public Zone getZone(){
        return Zone.builder()
                .city(this.getCityName())
                .localNameOfCity(this.getLocalNameOfCity())
                .province(this.getProvinceName())
                .build();
    }

}
