package com.termux.api;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.termux.api.util.ResultReturner;

import java.io.PrintWriter;
import java.util.List;

/**
 * @author : chedim (chedim@couchbaser)
 * @file : AppManagerApi
 * @created : Wednesday Jun 23, 2021 20:52:10 EDT
 */
public class PackageManagerApi {

    static void onReceive(TermuxApiReceiver apiReceiver, final Context context, Intent intent) {

        final String LOG_TAG = "termux-applist";
        final String APPLIST_CACHE_FILE = ".apps";
        try {
            ResultReturner.returnData(context, intent, out -> {
                    final PackageManager pm = context.getPackageManager();
                    List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
                    for (ApplicationInfo packageInfo : packages) {
                        final String packageName = packageInfo.packageName;
                        final String appName = packageInfo.loadLabel(pm).toString();
                        final String sourceDir = packageInfo.sourceDir;
                        final Intent LaunchActivity = pm.getLaunchIntentForPackage(packageName);
                        final Boolean isSystemApp = (packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1;

                        if (LaunchActivity == null) {
                            continue;
                        }

                        final String LaunchComponent = LaunchActivity.getComponent().flattenToShortString();
                        out.print(appName + "|" + LaunchComponent + "|" + packageName + "|" + isSystemApp + "\n");
                    } // for package in packages
            });

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error setting up applist-cache", e);
        }
    }

}


