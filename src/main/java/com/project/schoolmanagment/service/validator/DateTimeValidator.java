package com.project.schoolmanagment.service.validator;

import com.project.schoolmanagment.entity.concretes.businnes.LessonProgram;
import com.project.schoolmanagment.exception.BadRequestException;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
@Component
public class DateTimeValidator {

	//TODO we have to validate also old lesson program and new lesson program time schedule matching.

	// 09:00 - 12:00
	// 11:00 - 13:00
	public boolean checkTime(LocalTime start, LocalTime stop){
		return start.isAfter(stop) || start.equals(stop);
	}

	public void checkTimeWithException(LocalTime start,LocalTime stop){
		if (checkTime(start,stop)) {
			throw new BadRequestException(ErrorMessages.TIME_NOT_VALID_MESSAGE);
		}
	}


	public void checkLessonPrograms(Set<LessonProgram> existLessonProgram, Set<LessonProgram> lessonProgramRequest){
		if(existLessonProgram.isEmpty() && lessonProgramRequest.size()>1){
			checkDuplicateLessonPrograms(lessonProgramRequest);
		} else {
			checkDuplicateLessonPrograms(lessonProgramRequest);
			checkDuplicateLessonPrograms(existLessonProgram,lessonProgramRequest);
		}
	}

	private void checkDuplicateLessonPrograms(Set<LessonProgram> lessonPrograms){
		Set<String>uniqueLessonProgramKeys = new HashSet<>();

		for (LessonProgram lessonProgram : lessonPrograms){
			String lessonProgramKey = lessonProgram.getDay().name() + lessonProgram.getStartTime();
			if(uniqueLessonProgramKeys.contains(lessonProgramKey)){
				throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_EXIST_MESSAGE);
			}
			uniqueLessonProgramKeys.add(lessonProgramKey);
		}
	}

	private void checkDuplicateLessonPrograms (Set<LessonProgram> existLessonProgram, Set<LessonProgram> lessonProgramRequest){
		for(LessonProgram requestLessonProgram : lessonProgramRequest){
			if (existLessonProgram.stream().anyMatch(lessonProgram ->
					lessonProgram.getStartTime().equals(requestLessonProgram.getStartTime() )
					&& 	lessonProgram.getDay().name().equals(requestLessonProgram.getDay().name()))){
				throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_EXIST_MESSAGE);
			}
		}
	}

}
