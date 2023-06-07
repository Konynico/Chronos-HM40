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
}
