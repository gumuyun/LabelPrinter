package cn.zcbdqn.labelprinter.context;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class MyApplication extends LitePalApplication {

	private static Stack<Activity> activityStack;
	private List<Activity> activityList = new ArrayList<Activity>();
	private static MyApplication singleton;
	public final static String CONF_FRIST_START = "isFristStart";
	public static boolean flag = false;
	public static Context mContext;

	public static Map<String,Object> applicationMap=new HashMap<String, Object>();
	// 单例模式,加锁
	public synchronized static MyApplication getInstance() {

		if (singleton == null) {
			singleton = new MyApplication();
		}
		return singleton;
	}


	public void setFileMapstate(boolean flag) {
		
	}

	public void setFileMaplive(boolean flag) {
		
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this.getApplicationContext();
//		AppExceptionUncatch.getInstance().init(mContext);
		/*try {
			ReaderHelper.setContext(getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		singleton = this;
		// Thread.currentThread().setUncaughtExceptionHandler(MyCrashHandler.getInstance(getApplicationContext()));
		// loggerM=LoggerManager.getLoggerInstance();
		// providertool tool = new providertool(getApplicationContext());
		// tool.cleandatabase();
		// if (fileMap.isEmpty())
		// {
		// filemaple = new HashMap<String, downinfo>();
		// filemaple = tool.getallinfo();
		// Iterator it = filemaple.keySet().iterator();
		//
		// while (it.hasNext()){
		// String key;
		// key=(String)it.next();
		// fileMap.put(key, filemaple.get(key));
		//
		// }
		//
		//
		// }
	}

/*	public Map<String, downinfo> cleanmap() {
		ArrayList<String> delMap = new ArrayList<String>();
		Iterator it = fileMap.keySet().iterator();
		while (it.hasNext()) {
			String key;
			key = (String) it.next();
			// downResult.add(filemap.get(key));
			FileUtility flie = new FileUtility();
			if (!flie.isFileExist(fileMap.get(key).filename,
					DrugSystemConst.download_dir)) {
				// fileMap.remove(key);
				// delMap.put(key, fileMap.get(key));
				delMap.add(key);
			}

		}
		for (int i = 0; i < delMap.size(); i++) {
			fileMap.remove(delMap.get(i));
		}
		return fileMap;
	}*/

	/**
	 * add Activity
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * get current Activity
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
     * 
     */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
     * 
     */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
     * 
     */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
     *
     */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
     * 
     */
	public void AppExit() {
		try {
			finishAllActivity();
		} catch (Exception e) {
		}
	}

	/**
	 * 获取App安装包信息
	 *
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	public List<Activity> getActivityList() {
		if (activityList != null) {
			return activityList;
		}
		return activityList;
	}

	public void delActivityList(Activity activity) {
		if (activityList != null) {
			activity.finish();
			activityList.remove(activity);
			activity = null;
		}
	}

	public void AddActivity(Activity activity) {

		if (activity != null) {
			activityList.add(activity);
		}
	}

	// 彻底退出程序
	public void exitActivity() {
		try {

			for (Activity activity : activityList) {
				if (activity != null) {
					activity.finish();
				}
			}

			android.os.Process.killProcess(android.os.Process.myPid());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			System.gc();
			System.exit(0);
		}
	}

}
