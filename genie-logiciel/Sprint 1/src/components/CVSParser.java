package components;

import controllers.Parser;
import java.io.IOException;
import java.io.InputStream;
import models.Node;
import models.ParseException;
import models.Relation;

/**
 * Concrete CVS parser
 * @author freaxmind
 */
public class CVSParser implements Parser {

    @Override
    public void load(InputStream input) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Node parse(Relation rootRelation) throws ParseException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
