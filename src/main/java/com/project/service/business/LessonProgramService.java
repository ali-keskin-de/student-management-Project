package com.project.service.business;


import com.project.entity.business.LessonProgram;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.messages.ErrorMessages;
import com.project.repository.business.LessonProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class LessonProgramService {

    private final LessonProgramRepository lessonProgramRepository;

    public Set<LessonProgram> getLessonProgramById(Set<Long> lessonIdSet){

       Set<LessonProgram>  lessonPrograms = lessonProgramRepository.getLessonProgamByLessonProgramIdList(lessonIdSet);

       if(lessonPrograms.isEmpty()){
           throw new ResourceNotFoundException(ErrorMessages.NOT_FOUND_LESSON_PROGRAM_WITHOUT_ID_MESSAGE);
       }

       return  lessonPrograms;

    }
}
