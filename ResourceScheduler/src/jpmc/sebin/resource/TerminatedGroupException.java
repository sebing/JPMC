package jpmc.sebin.resource;

/**
 * @author Sebin
 * An Exception class if further Messages belonging to that group are received
 */
public class TerminatedGroupException extends RuntimeException {

    public TerminatedGroupException(String message) {
        super(message);
    }

}