package com.iarnold.modernmagazine.download;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

/**
 * Author: wyouflf Date: 13-11-10 Time: 上午1:04
 */
public class DownloadService extends Service {

	private static DownloadManager DOWNLOAD_MANAGER;

	public static DownloadManager getDownloadManager(Context appContext) {
		if (!DownloadService.isServiceRunning(appContext)) {
			Intent intent = new Intent();
			intent.setAction("download.service.action");
			Intent intent2 = new Intent(getExplicitIntent(appContext, intent));
			// Intent downloadSvr = new Intent("download.service.action");
			appContext.startService(intent2);
		}
		if (DownloadService.DOWNLOAD_MANAGER == null) {
			DownloadService.DOWNLOAD_MANAGER = new DownloadManager(appContext);
		}
		return DOWNLOAD_MANAGER;
	}

	public DownloadService() {
		super();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public static Intent getExplicitIntent(Context context,
			Intent implicitIntent) {
		// Retrieve all services that can match the given intent
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent,
				0);
		// Make sure only one match was found
		if (resolveInfo == null || resolveInfo.size() != 1) {
			return null;
		}
		// Get component info and create ComponentName
		ResolveInfo serviceInfo = resolveInfo.get(0);
		String packageName = serviceInfo.serviceInfo.packageName;
		String className = serviceInfo.serviceInfo.name;
		ComponentName component = new ComponentName(packageName, className);
		// Create a new intent. Use the old one for extras and such reuse
		Intent explicitIntent = new Intent(implicitIntent);
		// Set the component to be explicit
		explicitIntent.setComponent(component);
		return explicitIntent;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		if (DOWNLOAD_MANAGER != null) {
			try {
				DOWNLOAD_MANAGER.stopAllDownload();
				DOWNLOAD_MANAGER.backupDownloadInfoList();
			} catch (DbException e) {
				LogUtils.e(e.getMessage(), e);
			}
		}
		super.onDestroy();
	}

	public static boolean isServiceRunning(Context context) {
		boolean isRunning = false;

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(Integer.MAX_VALUE);

		if (serviceList == null || serviceList.size() == 0) {
			return false;
		}

		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(
					DownloadService.class.getName())) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}
}
