package com.wakuza.springboot.realProjects.modules.zone;


import com.wakuza.springboot.realProjects.modules.domain.Zone;
import lombok.Data;

@Data
public class ZoneForm {

    //TODO Seoul(서울)/None
    private String zoneName;

    public String getCityName(){return zoneName.substring(0,zoneName.indexOf("("));}

    public String getProvinceName(){
        return zoneName.substring(zoneName.indexOf("/")+1);
    }
    public String getLocalNameOfCity(){
        return zoneName.substring(zoneName.indexOf("/")+1,zoneName.indexOf(")"));
    }

    public Zone getZone(){
        return Zone.builder().city(this.getCityName())
                .localNameOfcity(this.getLocalNameOfCity())
                .province(this.getProvinceName())
                .build();
    }

}