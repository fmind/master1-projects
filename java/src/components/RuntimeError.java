package components;

/**
 * Exception throws when the internal compiler find an error during runtime
 *
 * @author freaxmind
 */
public class RuntimeError extends Error {

    public RuntimeError(String msg) {
        super(msg);
    }

    @Override
    public String toString() {
        return "Runtime error: " + this.getMessage();
    }
}
