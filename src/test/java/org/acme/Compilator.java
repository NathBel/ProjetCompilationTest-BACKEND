package org.acme;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class Compilator {

    public void compile(String filename) {
        try {
            // Lecture du fichier d'entrée
            System.out.println("Compiling file: " + System.getProperty("user.dir"));
            BufferedReader reader = new BufferedReader(new FileReader("src/test/java/org/acme/" + filename));
            StringBuilder code = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                switch (line) {
                    case "droite":
                        code.append("x += 1;\n");
                        break;
                    case "gauche":
                        code.append("x -= 1;\n");
                        break;
                    case "haut":
                        code.append("y += 1;\n");
                        break;
                    case "bas":
                        code.append("y -= 1;\n");
                        break;
                    default:
                        throw new IOException("Erreur de syntaxe dans le fichier input.txt");
                }
            }
            reader.close();

            // Génération du code source de la classe
            String className = "GeneratedClass";
            String methodName = "getCoordinates";
            String fileContent = "public class " + className + " {\n"
                    + "  public static int[] " + methodName + "() {\n"
                    + "    int x = 0;\n"
                    + "    int y = 0;\n"
                    + code.toString()
                    + "    return new int[] {x, y};\n"
                    + "  }\n"
                    + "}";

            // Écriture du code source dans un fichier
            PrintWriter writer = new PrintWriter("src/test/java/org/acme/GeneratedClass.java");
            writer.print(fileContent);
            writer.close();

            // Compilation du code source de la classe générée
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            int result = compiler.run(null, null, null, "GeneratedClass.java");
            if (result != 0) {
                throw new RuntimeException("Erreur de compilation");
            }

            // Chargement de la classe générée
            Class<?> clazz = Class.forName(className);

            // Appel de la méthode getCoordinates de la classe générée
            Method method = clazz.getMethod(methodName);
            int[] coordinates = (int[]) method.invoke(null);

            // Affichage des coordonnées finales
            System.out.println("Coordonnées finales : (" + coordinates[0] + ", " + coordinates[1] + ")");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Compilator compilator = new Compilator();
        compilator.compile("input.txt");
    }

}
