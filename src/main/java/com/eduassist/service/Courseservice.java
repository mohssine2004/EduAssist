package com.eduassist.service;

import com.eduassist.dao.CourseDAO;
import com.eduassist.dao.UserDAO;
import com.eduassist.model.Course;
import com.eduassist.model.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class CourseService {

    @Inject
    private CourseDAO courseDAO;

    @Inject
    private UserDAO userDAO;

    public void create(String titre, String description, int userId) {
        User user = userDAO.findById(userId);
        Course course = new Course(titre, description, user);
        courseDAO.save(course);
    }

    public List<Course> getCoursesForUser(int userId) {
        return courseDAO.findByUserId(userId);
    }

    public Course findById(int id) {
        return courseDAO.findById(id);
    }

    public void update(Course course) {
        courseDAO.update(course);
    }

    public void delete(int id) {
        courseDAO.delete(id);
    }

    public long countCourses(int userId) {
        return courseDAO.countByUser(userId);
    }
}