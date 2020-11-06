package com.seazoned.service.api;

/**
 * Created by root on 3/2/18.
 */

public class Api {

    /*public static String sHost = "http://192.168.1.2:8080/dev/seazonedapp/public/api";
    public static String sTransactionApproved = "http://192.168.1.2:8080/dev/seazonedapp/APP_HTML/transaction.html";
    public static String sTransactionDeclined = "http://192.168.1.2:8080/dev/seazonedapp/APP_HTML/transactiondeciline.html";*/


     /*public static String sHost = "http://103.230.103.142:8080/dev/seazonedapp/public/api";
     public static String sTransactionApproved = "http://103.230.103.142:8080/dev/seazonedapp/APP_HTML/transaction.html";
     public static String sTransactionDeclined = "http://103.230.103.142:8080/dev/seazonedapp/APP_HTML/transactiondeciline.html";*/

    public static String sHost = "http://seazoned.com/api";
    public static String sTransactionApproved = "http://seazoned.com/APP_HTML/transaction.html";
    public static String sTransactionDeclined = "http://seazoned.com/APP_HTML/transactiondeciline.html";

    public static String sCountryList = sHost + "/country-list";
    public static String sUserRegistration = sHost + "/user-registration?";//first_name=Sauvik&last_name=Dey&email=svkde@gmail.com&tel=9874563210&dob=01/16/2018&street=12BAsutosh&city=Kolkata&state=WB&country=80&password=1234";
    public static String sUserLogin = sHost + "/authenticate?";//username=svkde@gmail.com&password=1234";
    public static String sUserInfo = sHost + "/userinfo";//token
    public static String sUserInfoEdit = sHost + "/userinfo-edit";//first_name=test&last_name=landscaper&tel=9874563210&dob=01/16/2018
    public static String sServiceList = sHost + "/service-list";
    public static String sProviderListByServiceLocation = sHost + "/provider-list-by-service-location";
    public static String sAddProfileImage = sHost + "/change-profile-picture?";
    public static String sFaceBookLogin = sHost + "/user-fb-login";
    public static String sGooglePlusLogin = sHost + "/landscaper-google-login";
    public static String sChangePassword = sHost + "/change-password";
    public static String sSearchDetails = sHost + "/lanscaper-details?";
    public static String sViewService = sHost + "/view-service";
    public static String sAddressList = sHost + "/address-book-list";
    public static String sAddAddress = sHost + "/add-address-book";
    public static String sEditAddress = sHost + "/edit-address-book";
    public static String sConfirmBooking = sHost + "/confirm-booking";
    public static String sAddServiceImage = sHost + "/upload-service-image?";
    public static String sBookingHistory = sHost + "/booking-history";
    public static String sBookingHistoryDetails = sHost + "/booking-history-details";
    public static String sAddcard = sHost + "/add-card";
    public static String sCardList = sHost + "/view-card-list";
    public static String sDeleteCard = sHost + "/delete-card";
    public static String sAddFavorite = sHost + "/add-favorite";
    public static String sRemoveFavorite = sHost + "/remove-favorite";
    public static String sGiveReview = sHost + "/give-rate-review";
    public static String sFavouriteList = sHost + "/view-favorite-list";
    public static String sSaveDeviceToken = sHost + "/subscribe";
    public static String sLandscaperAccountInfo = sHost + "/landscaper-paypal-details";
    public static String sAdminAccountInfo = sHost + "/admin-paypal-details";
    public static String sPayUsingPaypal = sHost + "/pay-using-paypal";
    public static String sPayUsingCard = sHost + "/pay-using-card";
    public static String sNotificationList = sHost + "/notification-list-user";
    public static String sContactUs = sHost + "/contact-us-user";
    public static String sChatList = sHost + "/user-chat-list";
    public static String sFaq = sHost + "/get_faq";
    public static String sForgotPass = sHost + "/emailCheck";
    public static String sOTP = sHost + "/otpCheck";
    public static String sNewPass = sHost + "/new-password";
    public static String sEditCard = sHost + "/edit-card";
    public static String sGetNotificationStatus = sHost + "/get-notification-status";


    public static String sSetPrimaryCard=sHost+"/set-primary-card";
    public static String sCheckPrimaryCard=sHost+"/check-primary-card";
    public static String sConfirm=sHost+"/confirm-job";
}
