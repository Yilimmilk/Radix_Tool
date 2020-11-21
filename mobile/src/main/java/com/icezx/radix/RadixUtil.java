package com.icezx.radix;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;


public class RadixUtil {
	private static BigInteger mBigData, mIntegerOfRadix;
	private static String digits = "0123456789ABCDEF";

	/**
	 * @param data
	 * @param fromRadix
	 * @return
	 */
	public static String integerConvertTo10(BigInteger data, int fromRadix){
		String result = "";
		mIntegerOfRadix =new BigInteger(String.valueOf(fromRadix));
		while(!data.toString().equals("0")){
			result = digits.charAt(data.remainder(mIntegerOfRadix).intValue()) + result;
			data = data.divide(mIntegerOfRadix);
		}
		if(TextUtils.isEmpty(result)){
			result="0";
		}
		return result;
	}


	/**
	 * @see {@link Integer#parseInt(String, int)}
	 * @param data
	 * @param toRadix
	 * @param fromRadix
	 * @return
	 */
	public static String integerConverter(String data, int toRadix, int fromRadix){
		if(fromRadix == toRadix){
			return data;
		}
		char[] chars = data.toCharArray();
		int len = chars.length;
		if(toRadix != 10){
			data = integerConverter(data, 10, fromRadix);
			return integerConvertTo10(new BigInteger(data), toRadix);
		}else{
			mBigData =new BigInteger("0");
			for(int i = len - 1; i >=0; i--){
				mIntegerOfRadix =new BigDecimal(digits.indexOf(chars[i])* Math.pow(fromRadix, len - i - 1)).toBigInteger();
				mBigData = mBigData.add(mIntegerOfRadix);
			}
			return mBigData.toString();
		}
	}


	/**
	 *
	 * @param data
	 * @param toRadix
	 * @param formRadix
	 * @return the decimals represented by the string argument in the specified radix.
	 * Supports maximum of 10 decimal places.
	 */
	public static String decimalsConverter(String data, int toRadix, int formRadix, int digit) {

		if(toRadix == formRadix){
			return data;
		}
		//if specified radix(toRadix) is not base 10, covert decimals in base 10
		if(formRadix != 10){
			data = decimalsConvertTo10(data, formRadix);
			if(toRadix == 10){
				return data;
			}
		}
		char[] chars = digits.toCharArray();
		int integer;
		BigDecimal bigDecimal=new BigDecimal("0." + data);
		String result = "";
		//covert decimals(base 10) in specified radix(toRadix)
		while(bigDecimal.compareTo(new BigDecimal("0")) != 0){
			bigDecimal =bigDecimal.multiply(new BigDecimal(toRadix));
			integer=bigDecimal.intValue();
			result += chars[integer];
			bigDecimal =new BigDecimal("0."+bigDecimal.toPlainString().split("\\.")[1]);
			//if length greater then 9(equal to 10) break
			if(result.length() > digit){
				break;
			}
		}

		if(result.length() == 0){
			result = "0";
		}
		return result;

	}

	/**
	 *
	 * @param data
	 * @param fromRadix
	 * @return decimal system fractional part of the given data in the specified radix
	 */
	public static String decimalsConvertTo10(String data, int fromRadix) {

        char[] chars = data.toCharArray();
        BigDecimal sum=new BigDecimal("0");
        for(int i=0;i<data.length();i++){
			int index = digits.indexOf(chars[i]);
			int power =  -i-1;
            sum =sum.add(new BigDecimal(index * Math.pow(fromRadix,power)));
        }
        String[] ss=sum.toString().split("\\.");
        try {
            return  ss[1];
        }catch (Exception e){
            return  "0";
        }
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static int getIdByResourceName(String s){
		int resourceId = 0;
        try {
        Field field = R.id.class.getField(s);
        field.setAccessible(true);
        try {
                resourceId = field.getInt(null);
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {}
        } catch (NoSuchFieldException e) {}
        return resourceId;
	}


	/**
	 *
	 * @param data
	 * @param radix
	 * @return weather the data's format match the given radix.
	 */
	public static boolean checkData(String data, int radix){
		data = data.replaceAll(" ", "");
		String digits=".0123456789ABCDEF";
		//can only contains one point
		if(data.split("\\.").length > 2){
			return false;
		}
		for (int i = 0; i < data.length(); i++) {
			char digit = data.charAt(i);
			int index = digits.indexOf(digit);
			if (index == -1 || index > radix) {
				return false;
			}
		}
		return true;
	}

	/*
	 * 	第二种方法模拟人工算术
	 *  用来将正数或者负数十进制转换为其他进制数
	 * */
	public static String secDecToBin(int num) {
		//定义字符串，用来存放计算出来的二进制数据
		StringBuffer sb = new StringBuffer();
		while (num != 0) {
			//向字符串中添加计算出来的二进制数
			sb.append(num & 1);
			//对num进行无符号位运算，类似于除2运算，具体的区别还需要读者自己查找
			num = num >>> 1;
		}
		//将字符串反序返回
		return sb.reverse().toString();
	}

	public static String secDecToOctal(int num) {
		StringBuffer sb = new StringBuffer();
		while (num != 0) {
			sb.append(num & 7);
			num = num >>> 3;
		}
		return sb.reverse().toString();
	}

	public static String secDecToHex(int num) {
		StringBuffer sb = new StringBuffer();
		while (num != 0) {
			sb.append(num & 15);
			num = num >>> 4;
		}
		return sb.reverse().toString();
	}
}
