package components;

import controllers.Interpreter;
import models.QueryException;
import models.Request;

/**
 * Concret interpreter of custom SQL queries
 * @author freaxmind
 */
public class CustomSQLInterpreter implements Interpreter {

    @Override
    public Request interpret(String query) throws QueryException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
