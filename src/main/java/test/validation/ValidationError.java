package test.validation;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationError {
    private String code;
    private List<String> arguments;

    public ValidationError() {}

    public ValidationError(String code, List<String> arguments) {
        this.code = code;
        this.arguments = arguments;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "code='" + code + '\'' +
                ", arguments=" + arguments +
                '}';
    }
}
