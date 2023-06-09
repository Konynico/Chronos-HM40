package com.example.chronos_hm40;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CourseDao {
    @Insert
    void insertCourse(Course course);

    @Query("SELECT * FROM courses")
    List<Course> getAllCourses();

    // Autres méthodes d'accès aux données...

    // Exemple de méthode pour récupérer les cours par titre
    @Query("SELECT * FROM courses WHERE title = :title")
    List<Course> getCoursesByTitle(String title);

    @Query("SELECT * FROM courses WHERE day = :dayOfWeekString AND dateBegin <= :currentDate AND dateEnd >= :currentDate AND (frequency = 'Quotidienne' OR (frequency = 'Hebdomadaire' AND (strftime('%d', :currentDate) - strftime('%d', dateBegin)) % 7 = 0) OR (frequency = 'Bimensuelle' AND (strftime('%d', :currentDate) - strftime('%d', dateBegin)) % 14 = 0) OR (frequency = 'Mensuelle' AND (strftime('%d', :currentDate) - strftime('%d', dateBegin)) % 28 = 0) OR (frequency = 'Unique' AND dateBegin = :currentDate))")
    List<Course> getCoursesForDayAndDate(String dayOfWeekString, String currentDate);

}
