package cn.zcbdqn.labelprinter.util;

import android.content.ContentValues;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;


/**
 * 把对象封装成ContentValues对象
 * @author gumuyun
 *
 */
public class Object2Values {
	
	public static ContentValues object2Values(Object object){
		ContentValues values=null;
		Class<? extends Object> clazz = object.getClass();
		
		Field[] declaredFields = clazz.getDeclaredFields();
		
		AccessibleObject.setAccessible(declaredFields, true);
		if(declaredFields!=null){
			values=new ContentValues();
			for (int i = 0; i < declaredFields.length; i++) {
				int modifiers = declaredFields[i].getModifiers();
				//非静态属性,非瞬时
				if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
					//属性数据类型
					Type fieldType=declaredFields[i].getGenericType();
					//判断是元数据类型
					if(isMetaData(fieldType)){
						try {
							//值不为空
							Object value = declaredFields[i].get(object);
							if(value!=null){
								values.put(declaredFields[i].getName(), value.toString());
							}
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		return values;
	}
	/**
	 * 判断是不是元数据类型
	 * @param type
	 * @return
	 */
	public static boolean isMetaData(Type type){
		return type==Number.class || type==Boolean.class || type==Character.class || type==String.class 
				|| type==Integer.class	|| type==Byte.class || type==Short.class|| type==Long.class || type==Float.class
				|| type==Double.class ||type==int.class	|| type==byte.class || type==short.class|| type==long.class || type==float.class
				|| type==double.class || type==char.class|| type==boolean.class;
	}

}
