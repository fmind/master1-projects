package components;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * Check an user code and its test case
 *
 * @note error are displayed to the user, exception end the application
 *
 * @author freaxmind
 */
public class CodeChecker {

    /**
     * Create a temporary file with the user and test code
     *
     * @param code user code
     * @param test test code
     * @return a .java file
     * @throws GameException
     */
    private static File file(String code, String test) throws GameException {
        File javaFile;

        try {
            // create a temporary file
            javaFile = File.createTempFile("javanaute", ".java");
            FileWriter out = new FileWriter(javaFile);
            String name = javaFile.getName().replace(".java", "");

            // write the code and close it
            out.write("public class " + name + " {\n");
            out.write("public void test() {\n");    // declaration method test
            out.write(test);                        // method test code
            out.write("\n}");                       // end method test
            out.write(code);                        // user method (with declaration)
            out.write("\n}");

            out.close();

            Logger.getLogger("").log(Level.INFO, "Fichier temporaire: {0}", javaFile.getAbsolutePath());
        } catch (IOException e) {
            throw new GameException("Impossible de créer le fichier temporaire");
        }

        return javaFile;
    }

    /**
     * Compile and return a class file
     *
     * @param file
     * @return a .class file
     * @throws CompilerError
     */
    private static File compile(File file) throws CompilerError {
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        File classFile = new File(file.getAbsolutePath().replace(".java", ".class"));
        int status = compiler.run(null, null, err, file.getAbsolutePath());
        String errStr = err.toString().replace(file.getAbsolutePath(), "");     // cleaner

        if (status != 0 || !classFile.exists()) {
            throw new CompilerError("COMPILATION\n" + errStr);
        }

        return classFile;
    }

    /**
     * Run the test method of a class file
     *
     * @param classFile
     * @throws GameException
     * @throws AssertionError
     */
    @SuppressWarnings("unchecked")
    private static void run(File classFile) throws GameException, AssertionError, RuntimeError {
        // Set the search path for the class loader
        String className = classFile.getName().replace(".class", "");
        String parentDir = classFile.getParent() + File.separator;
        URL[] searchPath = new URL[1];

        try {
            searchPath[0] = new File(parentDir).toURI().toURL();
        } catch (MalformedURLException ex) {
            throw new GameException("Impossible de résoudre le fichier class: " + ex);
        }

        try {
            // Load the class and invoke the "test" method
            ClassLoader cl = new URLClassLoader(searchPath);
            cl.setDefaultAssertionStatus(true);                 // mandatory for assertions
            Class clazz = Class.forName(className, true, cl);
            Object object = clazz.newInstance();
            Method method = clazz.getDeclaredMethod("test");
            method.invoke(object);
        } catch (ClassNotFoundException ex) {
            throw new GameException("Impossible de charger la classe: " + ex);
        } catch (InstantiationException ex) {
            throw new GameException("Impossible d'instancier l'objet" + ex);
        } catch (NoSuchMethodException ex) {
            throw new GameException("Impossible d'invoquer la méthode: " + ex);
        } catch (IllegalAccessException | IllegalArgumentException ex) {
            throw new GameException("Erreur lors de l'appel de la méthode: " + ex);
        } catch (InvocationTargetException ex) {
            // only treat AssertionError
            if (ex.getCause() instanceof AssertionError) {
                Error systemErr = (AssertionError) ex.getCause();
                // don't count the class and test method declaration
                int lineErr = systemErr.getStackTrace()[0].getLineNumber() - 2;
                Error userErr = new AssertionError("ASSERTION\nassert n°" + lineErr + " faux");
                throw userErr;
            } else {
                throw new RuntimeError("RUNTIME\n" + ex.getCause().getMessage());
            }
        }
    }

    /**
     * Check an user code from an assertion test
     *
     * @param code
     * @param test
     * @return result a the code check
     */
    public static CheckResult check(String code, String test) throws GameException {
        if (test == null || test.isEmpty()) {
            return new CheckResult("", new CompilerError("La fonction de test est vide"));
        }

        File javaFile = null;
        File classFile = null;
        Error error = null;

        // redirect system out
        PrintStream oldOut = System.out;
        ByteArrayOutputStream newOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(newOut));

        try {
            javaFile = CodeChecker.file(code, test);
            classFile = CodeChecker.compile(javaFile);
            CodeChecker.run(classFile);
        } catch (Error err) {
            // user error (compilation, runtime, assertion)
            error = err;
        } catch (GameException ex) {
            throw ex;
        } finally {
            // delete temporary file and reset the standard output
            if ((javaFile != null && !javaFile.delete()) || (classFile != null && !classFile.delete())) {
                Logger.getLogger("").log(Level.WARNING, "Impossible de supprimer les fichiers temporaires");
            }
        }

        // set the old System.out back
        System.setOut(oldOut);

        CheckResult result = new CheckResult(newOut.toString(), error);

        return result;
    }
}
