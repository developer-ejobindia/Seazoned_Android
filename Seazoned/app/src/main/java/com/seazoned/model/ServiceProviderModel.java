package com.seazoned.model;

/**
 * Created by Souvik on 19-02-2018.
 */

public class ServiceProviderModel {
    private String Id, UserId,ServiceId,Name,Description, ProfileImage, ProviderLocation,ratings,min_price,userCount,favouriteStatus;
    String city,state;



    public ServiceProviderModel(String id, String userId, String serviceId, String name, String description, String profileImage, String providerLocation,String city,String state, String ratings,String min_price, String userCount, String favouriteStatus) {
        Id = id;
        UserId = userId;
        ServiceId = serviceId;
        Name = name;
        Description = description;
        ProfileImage = profileImage;
        ProviderLocation = providerLocation;
        this.city=city;
        this.state=state;
        this.ratings = ratings;
        this.min_price = min_price;
        this.userCount = userCount;
        this.favouriteStatus=favouriteStatus;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getMin_price() {
        return min_price;
    }

    public String getFavouriteStatus() {
        return favouriteStatus;
    }

    public void setFavouriteStatus(String favouriteStatus) {
        this.favouriteStatus = favouriteStatus;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setServiceId(String serviceId) {
        ServiceId = serviceId;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public void setProviderLocation(String providerLocation) {
        ProviderLocation = providerLocation;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public String getId() {
        return Id;
    }

    public String getUserId() {
        return UserId;
    }

    public String getServiceId() {
        return ServiceId;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public String getProviderLocation() {
        return ProviderLocation;
    }

    }



