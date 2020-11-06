package com.seazoned.app;

import com.seazoned.model.SelectImageModel;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 3/2/18.
 */

public class AppData {
    public static String sToken="";
    public static String sUserId="";
    public static String sServiceId="";
    public static String sServiceName="";
    public static ArrayList<HashMap<String, String>> sRecurringServiceList;
    public static String sTotalPrice="0.0";
    public static ArrayList<SelectImageModel> sImageList=null;
    public static HashMap<String,String> finalBookingParams;
    public static boolean _paypalLibraryInit=false;
    public static String sSearchAddress="";
    public static double sSearchLatitiude=0.0;
    public static double sSearchLongitiude=0.0;
    public static String sPercentageRate ="20";
}
