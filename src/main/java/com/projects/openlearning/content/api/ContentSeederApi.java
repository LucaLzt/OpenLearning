package com.projects.openlearning.content.api;

import java.util.UUID;

public interface ContentSeederApi {

    void seedRandomCourses(UUID instructorId, String instructorName, int numberOfCourses);

}
