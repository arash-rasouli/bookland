package org.example.dto;

import java.util.*;

public class ResponseInfo {
    private Object value;
    private ArrayList<String> errors;
    private String message;
    private boolean success;

    public ResponseInfo() {
        value = null;
        message = null;
        success = true;
        errors = new ArrayList<>();
    }

    public ResponseInfo(Object _value, boolean _success) {
        value = _value;
        message = "No Message";
        errors = new ArrayList<>();
        success = _success;
    }

    public ResponseInfo(Object _value, boolean _success, String _message) {
        value = _value;
        message = _message.isBlank() ? "No Message" : _message;
        errors = new ArrayList<>();
        success = _success;
    }

    public void addError(String error) {
        errors.add(error);
    }

    public void addErrors(ArrayList<String> _errors) {
        errors.addAll(_errors);
    }

    public String getMessage() { return message; }
    public ArrayList<String> getErrors() { return errors; }
    public Object getValue() { return value; }
    public boolean getSuccess() { return success; }

    public void setSuccess(boolean _success) { success = _success; }
    public void setMessage(String _message) { message = _message; }
    public void setErrors(ArrayList<String> _errors) { errors = _errors; }
    public void setValue(Object _value) { value = _value; }
}
