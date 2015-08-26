package com.dsv.tourism.azure;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.dsv.tourism.R;
import com.dsv.tourism.adapter.OfficeAdapter;
import com.dsv.tourism.model.Office;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;


/**
 * Created by Vince on 25.08.2015.
 */
public class DataHelper {

    private static AzureServiceHelper mAzureServiceHelper = null;

    /**
     * Microsoft Azure mobile service client
     */
    private static MobileServiceClient mClient;

    private DataHelper() {}

    public static void init(Context context) {

        if (mAzureServiceHelper == null){
            mAzureServiceHelper = new AzureServiceHelper(context);
        }
    }

    public static MobileServiceList<Office> getOffices() throws MobileServiceException, ExecutionException, InterruptedException {
        MobileServiceTable<Office> mOfficeTable = mClient.getTable("office", Office.class);
        MobileServiceList<Office> result = null;
            //final MobileServiceList<Office> result = mOfficeTable.where().field("complete").eq(false).execute().get();
            result = mOfficeTable.execute().get();

        return result;
    }

    private static class AzureServiceHelper {
        private Context context;

        public AzureServiceHelper(Context context) {
            this.context = context;
            this.getClient();
        }

        private MobileServiceClient getClient() {
            try {
                mClient = new MobileServiceClient(
                        "https://mobile-tourism.azure-mobile.net/",
                        "zebzFMYsdHnqQwVokGWaemXzWXSwFR40",
                        context
                );

                return mClient;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
