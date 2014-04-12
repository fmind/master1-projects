package components;

import au.com.bytecode.opencsv.CSVReader;
import controllers.Parser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import models.Node;
import models.ParseException;
import models.Link;
import models.Link.Direction;

/**
* Implementation of the parser inteface
*
* @author Phongphet
*/
public class CSVParser implements Parser {
    private CSVReader reader;

    @Override
    public void load(InputStream input) throws IOException {
        this.reader = new CSVReader(new InputStreamReader(input), ';');
    }

    /**
     * Convert a String to a Direction
     *
     * @param string direction
     * @return direction enum
     */
    public Direction stringToDirection(String direction) {
        switch (direction) {
            case "both":
                return Direction.BOTH;
            case "in":
                return Direction.IN;
            case "out":
                return Direction.OUT;
            default:
                return Direction.BOTH;
        }
    }

    /**
     * Construct a link from a line read in the file
     *
     * @param t_read a CSVParser line
     * @return link
     */
    public Link getLinkFromFile(String[] t_read) throws ParseException {
        if (t_read.length < 4) {
            throw new ParseException("Not enough arguments in CSV line");
        }

        String name = t_read[1];
        Direction direction = this.stringToDirection(t_read[3]);
        
        if (t_read.length > 4) { // with attributes
            Map<String, String> attrs = new HashMap<>();
            String[] columns = t_read[4].split("[|]");

            for (String col : columns) {
                String[] words = col.split("=");
                
                if (words.length != 2) {
                    throw new ParseException("Attribute is not formatted correctly: " + col);
                }

                attrs.put(words[0], words[1]);
            }

            return new Link(name, direction, attrs);
        } else { // without attributes
            return new Link(name, direction);
        }
    }

    /**
* This function parse a CSV file in to a graph
*/
    @Override
    public Node parse(Node root, Link rootLink) throws ParseException {
        String[] t_read; 
        Node source;
        Node target;
        Link link;

        try {
            this.reader.readNext(); // pass the CSV header

            // for each line in the CSV file
            while ((t_read = this.reader.readNext()) != null) {

                // add source node to root if it doesn't exist
                // if it exists, we recover it by its node name
                if (!root.contains(t_read[0])) {
                    source = new Node(t_read[0]);
                    root.addDirectAndMirrorRelation(rootLink, source);
                } else {
                    source = root.getByChildName(t_read[0]);
                }

                // idem for target
                if (!root.contains(t_read[2])) {
                    target = new Node(t_read[2]);
                    root.addDirectAndMirrorRelation(rootLink, target);
                } else {
                    target = root.getByChildName(t_read[2]);
                }

                // add relation to the graph
                link = this.getLinkFromFile(t_read);
                source.addDirectAndMirrorRelation(link, target);
            }
        } catch (IOException ex) {
            throw new ParseException("Error while reading CSV file: " + ex.getMessage());
        }

        return root;
    }
}
