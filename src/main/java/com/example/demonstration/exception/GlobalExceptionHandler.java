package com.example.demonstration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
        @ExceptionHandler(DeviceNotFoundException.class)
        public ResponseEntity<Object> exception(DeviceNotFoundException exception) {
            return new ResponseEntity<>("This device does not exist", HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<Object> exception(UserNotFoundException exception) {
            return new ResponseEntity<>("This user does not exist", HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(GroupNotFoundException.class)
        public ResponseEntity<Object> exception(GroupNotFoundException exception) {
            return new ResponseEntity<>("This device group does not exist", HttpStatus.NOT_FOUND);
        }
}

