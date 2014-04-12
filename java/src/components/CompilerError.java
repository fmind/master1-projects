package components;

/**
 * Exception throws when the internal compiler find an error during compilation
 *
 * @author freaxmind
 */
public class CompilerError extends Error {

    public CompilerError(String msg) {
        super(msg);
    }

    @Override
    public String toString() {
        return "Compiler error: " + this.getMessage();
    }
}
