package com.project.schoolmanagment.service.validator;

import com.project.schoolmanagment.exception.BadRequestException;
import com.project.schoolmanagment.payload.messages.ErrorMessages;

import java.time.LocalTime;

public class TimeValidator {

	//start time should be earlier than stop time
	public static boolean checkTime(LocalTime start, LocalTime stop){
		return start.isAfter(stop) || start.equals(stop);
	}

	public static void checkTimeWithException(LocalTime start,LocalTime stop){
		if (checkTime(start,stop)) {
			throw new BadRequestException(ErrorMessages.TIME_NOT_VALID_MESSAGE);
		}
	}
}
