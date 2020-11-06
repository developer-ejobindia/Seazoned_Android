package com.seazoned.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 17/3/18.
 */

public class BookingHistoryModel {
    HashMap<String,String> serviceDetails;
    ArrayList<HashMap<String, String>> ImageDetails;

    public BookingHistoryModel(HashMap<String, String> serviceDetails, ArrayList<HashMap<String, String>> imageDetails) {
        this.serviceDetails = serviceDetails;
        ImageDetails = imageDetails;
    }

    public HashMap<String, String> getServiceDetails() {
        return serviceDetails;
    }

    public ArrayList<HashMap<String, String>> getImageDetails() {
        return ImageDetails;
    }
}
