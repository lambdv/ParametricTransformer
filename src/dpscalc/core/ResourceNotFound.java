package dpscalc.core;

/**
 * Runtime exception leaked when a resource from a database is not found 
 */
public class ResourceNotFound extends RuntimeException{
    public ResourceNotFound(String message){
        super(message);
    }
}
