package fr.sdv.m22026.lexdossierr.exception;


import  org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleNotException(ResourceNotFoundException ex) {
        Map<String,Object>  body= new HashMap<>();
        body.put("status", 404);
        body.put("error",ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String,Object>  fields= new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((err)->{fields.put(err.getField(),err.getDefaultMessage());});
        Map<String,Object>  body= new HashMap<>();
        body.put("status", 400);
        body.put("error",fields);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String,Object>> handleConflict(ConflictException ex) {
        Map<String,Object>  body= new HashMap<>();
        body.put("status", 409);
        body.put("error",ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String,Object>> handleInvalidCredentials(InvalidCredentialsException ex) {
        Map<String,Object>  body= new HashMap<>();
        body.put("status", 401);
        body.put("error",ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
}
