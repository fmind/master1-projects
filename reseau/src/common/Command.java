package common;

/**
 * Command instruction for a programm
 * command params1, params2 ...
 * @author freaxmind
 */
public class Command {
    private String action;
    private String[] params;
    
    public Command(String action, String[] params) {
        this.action = action;
        this.params = params;
    }
    
    public String getAction() {
        return this.action;
    }
    
    public String[] getParams() {
        return this.params;
    }
    
    @Override
    public String toString() {
        return "Command " + this.action + ": " + params;
    }
}
