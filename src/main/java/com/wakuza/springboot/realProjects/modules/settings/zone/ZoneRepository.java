package com.wakuza.springboot.realProjects.modules.settings.zone;


import com.wakuza.springboot.realProjects.modules.domain.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone,Long> {
    Zone findByCityAndProvince(String cityName,String provinceName);
}
