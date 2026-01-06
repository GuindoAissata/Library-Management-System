package Model;

public class ArgumentException extends Exception {
    private String message;
    public ArgumentException(String m){ message = m ; }
    public String getMessage(){return message;}
    
}
