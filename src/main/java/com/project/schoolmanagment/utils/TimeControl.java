package com.project.schoolmanagment.utils;

import java.time.LocalTime;

public class TimeControl {

	//start time should be earlier than stop time
	public static boolean checkTime(LocalTime start, LocalTime stop){
		return start.isAfter(stop) || start.equals(stop);
	}
}
