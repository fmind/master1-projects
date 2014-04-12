package components;

import au.com.bytecode.opencsv.CSVReader;
import controllers.Parser;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import models.Node;
import models.ParseException;
import models.Link;
import models.Link.Direction;
import models.Relation;

/**
* Implementation of the parser inteface
*
* @author phongphong
*/
public class CSVParser implements Parser {

    private CSVReader reader;

    @Override
    public void load(InputStream input) throws IOException {
        this.reader = new CSVReader(new InputStreamReader(input), ';');
    }

    /**
* Convert a string to a Direction
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
* This function construct a link from a line read in the file
*
* @param t_read CSVParser transforms each line read into a table of String
* @return link
*/
    public Link getLinkFromFile(String[] t_read) throws ParseException {
        if (t_read.length < 4) {
            throw new ParseException("Not enough arguments in CSV line");
        }

        String name = t_read[1];
        Direction direction = this.stringToDirection(t_read[3]);
        //System.out.println(t_read.length);
        //System.out.println(t_read[0]+" "+t_read[1]+" "+t_read[2]+" "+t_read[3]);
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

    public void addRelationToChildOfRoot(Node root, String source, String target, Link link) {
        LinkedList<Node> listChild = (LinkedList<Node>) root.getChilds();
        for (int i = 0; i < listChild.size(); i++) {
            if (listChild.get(i).toString().compareTo(source) == 0) {
                listChild.get(i).addDirectAndMirrorRelation(link, new Node(target));
            }
        }
    }

    /**
* This function parse a CSV file in to a graph
*/
    @Override
    public Node parse(Node root, Link rootLink) throws ParseException {
        Link link;
        String[] t_read;
        Node source;
        Node target;
        int indexSource;
        int indexTarget;
        LinkedList<Node> listChild;
        try {
            reader.readNext(); // pass the CSV header

            // for each line in the CSV file
            while ((t_read = reader.readNext()) != null) {

                // add source node to root if it doesn't exist, the index of the node will be the size of the list
                // if it's existed, we recover the index of this node in the root
                if (root.getByChildName(t_read[0]) == null) {
                    source = new Node(t_read[0]);
                    root.addDirectAndMirrorRelation(rootLink, source);
                }

                // add target node to root if it doesn't exist, the index of the node will be the size of the list
                // if it's existed, we recover the index of this node in the root
                if (root.getByChildName(t_read[2]) == null) {
                    target = new Node(t_read[2]);
                    root.addDirectAndMirrorRelation(rootLink, target);
                }

                // add the relation to source
                link = this.getLinkFromFile(t_read);
                // source.addDirectAndMirrorRelation(link, target);

                root.getByChildName(t_read[0]).addDirectAndMirrorRelation(link, root.getByChildName(t_read[2]));

            }
        } catch (IOException ex) {
            throw new ParseException("Error while reading CSV file: " + ex.getMessage());
        }

        return root;
    }
}