package com.project.repository.business;


import com.project.entity.business.LessonProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface LessonProgramRepository extends JpaRepository<LessonProgram, Long> {

    //:myProperty yerine :?1 de diyebiliriz. birtane oldugunda :?1 demeye gerek kalmiyor.
    @Query("SELECT l FROM LessonProgram l WHERE l.id IN :myProperty")
    Set<LessonProgram> getLessonProgamByLessonProgramIdList(Set<Long> myProperty);
}