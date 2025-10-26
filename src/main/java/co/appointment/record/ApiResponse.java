package co.appointment.record;

public record ApiResponse<T>(boolean success, T data, String message) {
    public ApiResponse(final String message) {
        this(false, null, message);
    }
    public ApiResponse(final boolean success,
                       final String message) {
        this(success, null, message);
    }
    public ApiResponse(final T data) {
        this(true, data, "");
    }
}
