package fr.istic.vv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;




// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class MissingGettersPrinter extends VoidVisitorWithDefaults<Integer> {

    private String prettyMethodString(MethodDeclaration method) {
        String method_name = method.getNameAsString(); 
        String return_type = method.getTypeAsString();
        List<Parameter> list_params = method.getParameters();
        String params = "";
        for (Parameter parameters: list_params) {
            params += parameters.getNameAsString() + ",";
        }
        return String.format("%s(%s) -> %s", method_name, params, return_type);
    } 

    @Override
    public void visit(CompilationUnit unit, Integer arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, arg);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Integer arg) {
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Integer arg) {
        String padding = " ".repeat(arg);
        String title_depth = '\n' + "#".repeat(arg+1);

        // Add all other member to visitor
        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg + 1);
        }

        // Field
        List<String> private_field_names = new ArrayList<String>();

        List<String> private_fields_output = new ArrayList<String>();

        for (FieldDeclaration field : declaration.getFields()) {
            // Only private field need getters
            if (!field.isPrivate())
                continue;

            List<VariableDeclarator> variables = field.getVariables();

            for (VariableDeclarator variable : variables) {
                private_field_names.add(variable.getNameAsString());
                private_fields_output.add("- " + field.getVariables().get(0).getNameAsString() + ": " + field.getElementType());
            }
        }

        Map<String,Boolean> map_getter_existence = new HashMap<String,Boolean>();

        // Transform Field name to getter name.
        for (String private_field : private_field_names ) {
            String getter_name = "get" + private_field.substring(0, 1).toUpperCase() + private_field.substring(1);
            map_getter_existence.put(getter_name, false);
        }

        // Getters
        List<String> public_getters_output = new ArrayList<String>();

        for (MethodDeclaration method : declaration.getMethods()) {

            // A getter is public
            if (method.isPrivate())
                continue;

            // A getter does not take any parameters
            if (method.getParameters().size() != 0) 
                continue;

            String method_name = method.getNameAsString();

            // A getter start by get
            if (!method_name.startsWith("get"))
                continue;

            // Mark getter as existing.
            map_getter_existence.replace(method_name, true);
            
            public_getters_output.add(padding+"- " + prettyMethodString(method));
        }

        // Errors
        if (map_getter_existence.containsValue(false)) {
            System.out.println(title_depth +" "+ declaration.getFullyQualifiedName().orElse("[Anonymous]")+"\n");

            if (private_fields_output.size() != 0) {
                System.out.println(title_depth+"# Private Fields\n");
                for (String out : private_fields_output) {
                    System.out.println(out);
                }
            }

            if (public_getters_output.size() != 0) {
                System.out.println(title_depth+"# Public Getters\n");
    
                for (String out : public_getters_output) {
                    System.out.println(out);
                }
            }

            System.out.println(title_depth+"# Missing Getters\n");

            map_getter_existence.forEach((key, exist) -> {
                if (!exist)
                    System.out.println("- " + key + "() is missing");
            });
        }

     
    }

    @Override
    public void visit(EnumDeclaration declaration, Integer arg) {
        visitTypeDeclaration(declaration, arg);
    }
}
