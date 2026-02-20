package com.projects.openlearning.identity.api;

import java.util.UUID;

public interface IdentitySeederApi {
    // This method seeds the database with initial users (one student and one instructor) and returns
    // their IDs and instructor name.
    SeededUsers seedUsers();

    // This record is used to return the IDs of the seeded student and instructor, along with the instructor's name.
    record SeededUsers(UUID studentId, UUID instructorId, String instructorName) {}
}
