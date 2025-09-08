// This class contains custom exceptions
package exceptions;

public class ExcelExceptions {

    public static class ExcelOperationException extends RuntimeException {
        public ExcelOperationException(String message) {
            super(message);
        }
    }

    // Exception for file-related issues
    public static class FileLoadingException extends ExcelOperationException {
        public FileLoadingException(String message) {
            super(message);
        }
    }

    //Exception for invalid sheet
    public static class SheetNotFoundException extends ExcelOperationException {
        public SheetNotFoundException(String message) {
            super(message);
        }
    }

    // Exception for invalid data
    public static class DataValidationException extends ExcelOperationException {
        public DataValidationException(String message) {
            super(message);
        }
    }

    //Exception for missing columns
    public static class ColumnNotFoundException extends ExcelOperationException {
        public ColumnNotFoundException(String message) {
            super(message);
        }
    }

    // Exception for missing users
    public static class UserNotFoundException extends ExcelOperationException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
