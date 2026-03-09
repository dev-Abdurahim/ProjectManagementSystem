package org.example.projectmanagementsystem.exception;

import org.springframework.http.HttpStatus;

public class ProjectNotFoundException extends ApiException{

    public ProjectNotFoundException(Long id) {
        super(
                ErrorCode.PROJECT_NOT_FOUND,
                "Project not found with id: " + id,
                HttpStatus.NOT_FOUND
        );
    }
}
