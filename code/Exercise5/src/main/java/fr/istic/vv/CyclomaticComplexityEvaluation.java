package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.List;
import java.lang.Class;
import java.util.stream.Collectors;

public class CyclomaticComplexityEvaluation extends VoidVisitorWithDefaults<Void> {
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
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

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {

        java.util.List<Node> childs = declaration.getChildNodes();

        List<IfStmt> ifStmts = declaration.findAll(IfStmt.class);
        List<ForStmt> forStmts = declaration.findAll(ForStmt.class);
        List<WhileStmt> whileStmts = declaration.findAll(WhileStmt.class);
        List<DoStmt> doStmts = declaration.findAll(DoStmt.class);
        List<SwitchEntry> catchStmts = declaration.findAll(SwitchEntry.class).stream()
                .filter(s -> !s.getLabels().isEmpty()) // Ne pas inclure les "default", seulement les "case" avec labels
                .collect(Collectors.toList());
        List<ConditionalExpr> ternaryExprs = declaration.findAll(ConditionalExpr.class);
        List<BinaryExpr> andExprs = declaration.findAll(BinaryExpr.class).stream()
                .filter(f -> f.getOperator() == BinaryExpr.Operator.AND)
                .collect(Collectors.toList());

        List<BinaryExpr> orExprs = declaration.findAll(BinaryExpr.class).stream()
                .filter(f -> f.getOperator() == BinaryExpr.Operator.OR)
                .collect(Collectors.toList());

        int total = ifStmts.size() +
                forStmts.size() +
                whileStmts.size() +
                doStmts.size() +
                catchStmts.size() +
                ternaryExprs.size() +
                andExprs.size() +
                orExprs.size() +
                1;

        System.out.println("  " + declaration.getDeclarationAsString(true, true) + "   Cyclomatic Complexity:  " + total);

    }

}
