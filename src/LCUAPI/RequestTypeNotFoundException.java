package LCUAPI;

public class RequestTypeNotFoundException extends RuntimeException {
    public RequestTypeNotFoundException(String msg){
        super(msg);
    }

    public RequestTypeNotFoundException(RequestMethodType methodType){
        super(methodType.toString());
    }
}
