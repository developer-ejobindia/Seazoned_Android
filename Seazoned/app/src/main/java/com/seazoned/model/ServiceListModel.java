package com.seazoned.model;

/**
 * Created by Souvik on 20-02-2018.
 */

public class ServiceListModel {
   private String  Id,ServiceName,Description, LogoName;

    public ServiceListModel(String id, String serviceName, String description, String logoName) {
        Id = id;
        ServiceName = serviceName;
        Description = description;
        LogoName = logoName;
    }

    public ServiceListModel(String id, String serviceName, String description) {
        Id = id;
        ServiceName = serviceName;
        Description = description;
    }

    public String getId() {
        return Id;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public String getDescription() {
        return Description;
    }

    public String getLogoName() {
        return LogoName;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setLogoName(String logoName) {
        LogoName = logoName;
    }
}
