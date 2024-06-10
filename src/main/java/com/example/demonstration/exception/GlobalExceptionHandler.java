package com.example.demonstration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

        //if device is not found in database
        @ExceptionHandler(DeviceNotFoundException.class)
        public ResponseEntity<Object> exception(DeviceNotFoundException exception) {
            return new ResponseEntity<>("This device does not exist", HttpStatus.NOT_FOUND);
        }

        //if user is not found in database
        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<Object> exception(UserNotFoundException exception) {
            return new ResponseEntity<>("This user does not exist", HttpStatus.NOT_FOUND);
        }

        //if device group is not found in database
        @ExceptionHandler(GroupNotFoundException.class)
        public ResponseEntity<Object> exception(GroupNotFoundException exception) {
            return new ResponseEntity<>("This device group does not exist", HttpStatus.NOT_FOUND);
        }

        //if the user trying to create a device group that already exists
        @ExceptionHandler(GroupExistsException.class)
        public ResponseEntity<Object> exception(GroupExistsException exception) {
            return new ResponseEntity<>("This device group already exists", HttpStatus.CONFLICT);
        }

        //if the user trying to create a user that already exists
        @ExceptionHandler(UserExistsException.class)
        public ResponseEntity<Object> exception(UserExistsException exception) {
            return new ResponseEntity<>("This user already exists", HttpStatus.CONFLICT);
        }

        //if user tries to delete a user that already has devices assigned to them
        @ExceptionHandler(UserHasDevicesException.class)
        public ResponseEntity<Object> exception(UserHasDevicesException exception) {
            return new ResponseEntity<>("This user has devices still", HttpStatus.CONFLICT);
        }
}

