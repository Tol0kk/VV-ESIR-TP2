package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.*;

import java.nio.file.*;

public class ClassCohesionCalculator extends VoidVisitorWithDefaults<Void> {
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        String declarationName = declaration.getFullyQualifiedName().orElseGet(declaration::getNameAsString);


        // clé : attribut
        // valeur : ensemble des méthodes qui accèdent à cet attribut
        // pour chaque clé : les méthodes pour un attribut sont deux à deux liées directement.
        HashMap<String, HashSet<String>> attribute_access = new HashMap<>();

        // Construction du graph
        // clé : noeud
        // valeur : liste de voisins
        HashMap<String, HashSet<String>> graph = new HashMap<>();
        HashMap<String, String> connexions_label = new HashMap<>();

        List<FieldDeclaration> fields = declaration.getFields();
        for (FieldDeclaration f : fields) {
            for (VariableDeclarator var : f.getVariables()) {
                attribute_access.put(var.getNameAsString(), new HashSet<>());
            }
        }

        for(MethodDeclaration method : declaration.getMethods()) {
            String method_name = method.getNameAsString();
            graph.put(method_name, new HashSet<>());

            for (NameExpr name : method.findAll(NameExpr.class)) {
                String var_name = name.getNameAsString();
                if (attribute_access.containsKey(var_name)) {
                    attribute_access.get(var_name).add(method_name);
                }
            }

            for (FieldAccessExpr f : method.findAll(FieldAccessExpr.class)) {
                if (f.getScope().isThisExpr()) {
                    String var_name = f.getNameAsString();
                    if (attribute_access.containsKey(var_name)) {
                        attribute_access.get(var_name).add(method_name);
                    }
                }
            }
        }


        for (String key : attribute_access.keySet()) {
            // création des liens
            ArrayList<String> voisins_direct = new ArrayList<>(attribute_access.get(key));
            int i = 0;

            while (i < voisins_direct.size()) {
                String courant = voisins_direct.get(i);
                int j = i+1;
                while (j < voisins_direct.size()) {
                    String voisin_courant = voisins_direct.get(j);


                    if (courant.compareTo(voisin_courant) < 0) {
                        graph.get(courant).add(voisin_courant);
                        connexions_label.put(courant + voisin_courant, key);
                    } else {
                        graph.get(courant).add(voisin_courant);
                        connexions_label.put(voisin_courant + courant, key);
                    }
                    j++;
                }
                i++;
            }
        }

        double TCC = computeTCC(graph);
        double LCC = computeLCC(graph);

        System.out.println(declarationName + ";" + TCC + ";" + LCC);

        if (TCC > 0) {
            exportGraphViz(graph, connexions_label, declarationName);
        }

        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration) {
                member.accept(this, arg);
            }
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    private double computeTCC(HashMap<String, HashSet<String>> graph) {
        int n = graph.keySet().size();
        double nbPairesPossible = combinaison(n, 2); // total de paires possibles

        if (nbPairesPossible == 0.0) {
            return 0.0;
        }

        double nbConnectionsDirectes = compteConnexions(graph);

        return nbConnectionsDirectes / nbPairesPossible;
    }

    private double computeLCC(HashMap<String, HashSet<String>> graph) {
        int n = graph.keySet().size();
        double nbPairesPossible = combinaison(n, 2); // total de paires possibles

        if (nbPairesPossible == 0.0) {
            return 0.0;
        }

        // comptage des liens
        double nbConnexions = compteConnexions(fermeture(graph));

        // calcul
        return nbConnexions / nbPairesPossible;
    }

    private double factorial(int n) {
        double i = n;
        double prod = 1;
        while (i > 1) {
            prod *= i;
            i--;
        }
        return prod;
    }

    private double combinaison(int n, int p) {
        return factorial(n) / (factorial(p) * factorial(n - p));
    }

    public static HashMap<String, HashSet<String>> fermeture(HashMap<String, HashSet<String>> graph) {
        // Créer un nouveau graphe pour stocker la fermeture transitive
        HashMap<String, HashSet<String>> fermetureGraph = new HashMap<>();

        HashMap<String, HashSet<String>> voisins = new HashMap<>();
        for (String cle : graph.keySet()) {
            if (!voisins.containsKey(cle)) {
                voisins.put(cle, new HashSet<>());
            }
            for (String voisin : graph.get(cle)) {
                voisins.get(cle).add(voisin);
                if (!voisins.containsKey(voisin)) {
                    voisins.put(voisin, new HashSet<>());
                }
                voisins.get(voisin).add(cle);
            }
        }

        for (String node : voisins.keySet()) {
            // Initialiser un ensemble vide pour le noeud dans le nouveau graphe
            fermetureGraph.put(node, new HashSet<>());
            // Faire une recherche pour trouver tous les noeuds accessibles depuis 'node'
            HashSet<String> visited = new HashSet<>();
            dfs(node, voisins, visited);

            // Ajouter les voisins au graphe de fermeture en respectant l'ordre lexicographique
            for (String neighbor : visited) {
                if (!node.equals(neighbor) && node.compareTo(neighbor) < 0) {
                    fermetureGraph.get(node).add(neighbor);
                }
            }
        }

        return fermetureGraph;
    }

    // Fonction DFS pour explorer tous les voisins d'un noeud
    private static void dfs(String node, HashMap<String, HashSet<String>> graph, HashSet<String> visited) {
        visited.add(node);
        for (String neighbor : graph.getOrDefault(node, new HashSet<>())) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, graph, visited);
            }
        }
    }

    private int compteConnexions(HashMap<String, HashSet<String>> graph) {
        int nbConnectionsDirectes = 0;
        for (String key : graph.keySet()) {
            nbConnectionsDirectes += graph.get(key).size();
        }

        return nbConnectionsDirectes;
    }

    private void exportGraphViz(HashMap<String, HashSet<String>> graph, HashMap<String, String> labels, String className) {
        // creation of folder in current directory (has the name of the curent time
        Path folderPath = Paths.get("GraphViz");
        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectory(folderPath);

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Impossible d'avoir accès au dossier GraphViz " + className);
                return;
            }
        }

        File file = new File("GraphViz/" + className + ".dot");

        try {
            file.createNewFile();
            FileWriter writter = new FileWriter(file, false);
            writter.write("graph {\n");

            // description of all edges
            for (String key : graph.keySet()) {
                HashSet<String> voisins = graph.get(key);

                if (voisins.isEmpty()) {
                    writter.write("    " + key + ";\n");
                    continue;
                }

                for (String voisin : voisins) {
                    String label;
                    if (key.compareTo(voisin) < 0) {
                        label = labels.get(key+voisin);
                    } else {
                        label = labels.get(voisin+key);
                    }

                    writter.write("    " + key + " -- " + voisin + "[label=\"" + label +"\"];\n");
                }
            }
            writter.write("}");
            writter.close();

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }
}
