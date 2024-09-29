package fr.istic.vv;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import com.github.javaparser.utils.Pair;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class ComputeTCC extends VoidVisitorWithDefaults<Integer> {

    @Override
    public void visit(CompilationUnit unit, Integer arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, arg);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Integer arg) {
        System.out.println("#"+declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();

        List<Pair<String,String>> methodsLink;

        // Add nested Class to visitor
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg + 1);
        }

        // Add methods
        for(MethodDeclaration method : declaration.getMethods()) {
            methods.add(method);
        }

        for(MethodDeclaration method : declaration.getMethods()) {
            List<MethodCallExpr> child = method.findAll(MethodCallExpr.class);
            System.out.println(method.getDeclarationAsString(true,true));
            for (MethodCallExpr call : child) {
                System.out.println(call.getNameAsString());
                System.out.println(call.toString());
                System.out.println(call.getTypeArguments());
                System.out.println(call.getRange());
            }
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Integer arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Integer arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Integer arg) {
    }

}
