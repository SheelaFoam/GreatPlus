package com.erp.sheelafoam.utils;

/**
 * Created by admin on 26-02-2018.
 */

public interface URLS {


    String BASE_URL = "http://be.greatplus.com/sheelafoam/rest/";
    String UPLOAD_PHOTO = BASE_URL + "file/upload/{uid}/{token}/{storeId}";
    String SALES_REP = "http://greatplus.com/warranty_log_api/prc_sale_rep_info.php";
    String MTS_URL = "http://greatplus.com/warranty_log_api/mts_report_api.php";
    String NOTIFICATION_URL = "http://greatplus.com/warranty_log_api/norti_api.php";
    String UPDATE_VERSION="https://greatplus.com/warranty_log_api/app_update_alert.php";
    String SALES_REPORT_URL = "https://greatplus.com/api_services/gcard_api.php";

}
