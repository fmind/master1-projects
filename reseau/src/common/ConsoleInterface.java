package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Command line interface
 * use patterns to valid input
 * @author freaxmind
 */
public class ConsoleInterface {
    private Scanner scanner;
    private ArrayList<Pattern> patterns;
    private String prompt;
    
    public ConsoleInterface(ArrayList<Pattern> patterns) {
        this(" > ", patterns);
    }
    
    public ConsoleInterface(String prompt, ArrayList<Pattern> patterns) {
        this.scanner = new Scanner(System.in);
        this.patterns = patterns;
        this.prompt = prompt;
    }
    
    /**
     * Return a valid command (action/params)
     * @return 
     */
    public Command getCommand() {
        String line = "";
        System.err.println("");
        System.out.println("");
        
        boolean valid = false;
        do {
            System.out.print(this.prompt);
            line = this.scanner.nextLine();
            valid = this.match(line);
        
            if (!valid) {
                System.err.println("Commande non reconnue (utiliser la commande help)");
            }
        } while (!valid);
        
        return this.split(line);
    }
    
    /**
     * Test if a line match one pattern
     * @param line
     * @return true on match
     */
    public boolean match(String line) {
        for (Pattern pattern : this.patterns) {
            if (Pattern.matches(pattern.pattern(), line)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Split a line in action and params (optionnal)
     * @param line
     * @return 
     */
    public Command split(String line) {
        String[] args = line.split(" ");
        String action = args[0];
        String[] params = Arrays.copyOfRange(args, 1, args.length);
        
        Command command = new Command(action, params);
        
        return command;
    }
} 
