
package task_manager.exception;

import java.util.List;

public class ValidationErrorResponse {

    private List<String> message;
    private int status;

    public ValidationErrorResponse(List<String> message, int status) {
        this.message = message;
        this.status = status;
    }

    public List<String> getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
