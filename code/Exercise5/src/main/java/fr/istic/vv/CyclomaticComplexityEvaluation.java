package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.List;
import java.util.stream.Collectors;

public class CyclomaticComplexityEvaluation extends VoidVisitorWithDefaults<Integer> {
    private String prettyMethodString(MethodDeclaration method) {
        String method_name = method.getNameAsString(); 
        String return_type = method.getTypeAsString();
        List<Parameter> list_params = method.getParameters();
        String params = "";
        for (Parameter parameters: list_params) {
            params += parameters.getTypeAsString() + ",";
        }
        return String.format("%s(%s) -> %s", method_name, params, return_type);
    } 

    @Override
    public void visit(CompilationUnit unit, Integer arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, arg);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Integer arg) {
        for(MethodDeclaration method : declaration.getMethods()) {
            List<IfStmt> ifStmts = method.findAll(IfStmt.class);
            List<ForStmt> forStmts = method.findAll(ForStmt.class);
            List<WhileStmt> whileStmts = method.findAll(WhileStmt.class);
            List<DoStmt> doStmts = method.findAll(DoStmt.class);
            List<SwitchEntry> catchStmts = method.findAll(SwitchEntry.class).stream()
                    .filter(s -> !s.getLabels().isEmpty()) // Ne pas inclure les "default", seulement les "case" avec labels
                    .collect(Collectors.toList());
            List<ConditionalExpr> ternaryExprs = method.findAll(ConditionalExpr.class);
            List<BinaryExpr> andExprs = method.findAll(BinaryExpr.class).stream()
                    .filter(f -> f.getOperator() == BinaryExpr.Operator.AND)
                    .collect(Collectors.toList());
    
            List<BinaryExpr> orExprs = method.findAll(BinaryExpr.class).stream()
                    .filter(f -> f.getOperator() == BinaryExpr.Operator.OR)
                    .collect(Collectors.toList());
    
            int cc_total = ifStmts.size() +
                    forStmts.size() +
                    whileStmts.size() +
                    doStmts.size() +
                    catchStmts.size() +
                    ternaryExprs.size() +
                    andExprs.size() +
                    orExprs.size() +
                    1;
    
            // name class, name method, types parameters, the value of CC.
    
            String class_name = declaration.getFullyQualifiedName().orElse("[Anonymous]");
            String method_name = prettyMethodString(method);
            System.out.printf("%s;%s;%d\n", class_name, method_name, cc_total);
        }
        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg + 1);
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
}
