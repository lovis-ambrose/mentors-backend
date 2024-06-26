package com.servicecops.mentors.permissions;

import com.servicecops.mentors.models.jpahelpers.enums.AppDomains;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    private String code;
    private String name;
    private AppDomains domain;
    private Boolean shipWithAdmin = false;
    public Permission(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Permission(String code, String name, AppDomains domain) {
        this.code = code;
        this.name = name;
        this.domain = domain;
    }
}
