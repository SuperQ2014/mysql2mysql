package io.uve.feedreports;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public enum DateGenerator {
	
	REQUIREDDATE;
	
	private String startDate;
	private String endDate;
	private String date;
	
	{
		Date dNow = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dNow);
		calendar.add(Calendar.DAY_OF_MONTH, -2);
		Date dBefore = calendar.getTime();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		startDate = sdf.format(dBefore) + "T16:00:00";
		
		calendar.setTime(dNow);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		dBefore = calendar.getTime();
		date = sdf.format(dBefore);
		endDate = date + "T16:00:00";
	}
	
	public String getDate() {
		return date;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}
}
