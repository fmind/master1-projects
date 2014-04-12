package components;

/**
 * Result of a compilation and an execution
 *
 * @author freaxmind
 */
public class CheckResult {

    private String output;
    private Error error;
    private boolean success;

    public CheckResult() {
        this.output = "";
        this.error = null;
        this.success = false;
    }

    public CheckResult(String output, Error error) {
        this.output = output;
        this.error = error;
        this.success = (this.error == null);
    }

    public String getOutput() {
        return this.output;
    }

    public Error getError() {
        return this.error;
    }

    public Boolean isSuccess() {
        return this.success;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("");
        str.append("Résultat: ").append(this.isSuccess());

        if (this.error != null) {
            str.append("[error = ").append(this.error.getClass()).append("] ");
        }

        return str.toString();
    }
}
