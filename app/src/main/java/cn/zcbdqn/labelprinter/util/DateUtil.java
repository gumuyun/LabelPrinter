package cn.zcbdqn.labelprinter.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	

	public static String format(String pattern,Date date){
		if(pattern==null){
			pattern="yyyy-MM-dd HH:mm:ss";
		}
		return new SimpleDateFormat(pattern).format(date);
	}
}
