package com.wakuza.springboot.realProjects.modules.account;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity @Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Tags {

    @Id@GeneratedValue
    private Long id;

    @Column(unique = true,nullable = false)
    private String title;
}
