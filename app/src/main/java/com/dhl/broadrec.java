package com.dhl;

public class broadrec {
	public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE"; 
	public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
	
	public String action()
	{
		
		return ACTION_DATA_AVAILABLE;		
	}
	
	public String rec_date()
	{
		return EXTRA_DATA;
	}
	
}
