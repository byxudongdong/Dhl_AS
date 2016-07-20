package com.rules;

public class LocIdRules {

	public LocIdRules() {
		// TODO 自动生成的构造函数存根
	}

	public Boolean chackLocIdRules(String loc_id)
	{

		return null;
	}

	public static boolean isNumeric(String str){
		for (int i = 0; i < str.length(); i++){
			System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}
}
