package common;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Utility methods
 * @author freaxmind
 */
public class Utils {

    /**
     * Shortcut for String.join
     * delimiter is \n
     * @param lines
     * @return 
     */
    static public String textFromLines(String[] lines) {
        Iterator<String> it = Arrays.asList(lines).iterator();
        StringBuilder builder = new StringBuilder("");
        
        while (it.hasNext()) {
            builder.append(it.next());
            if (!it.hasNext()) {
              break;                  
            }
            builder.append("\n");
        }
        
        return builder.toString();
    }
}
