package com.projects.openlearning.common.security.api;

import java.util.UUID;

public interface AuthenticatedUser {

    UUID getUserId();

    String getEmail();

    String getRole();

}
