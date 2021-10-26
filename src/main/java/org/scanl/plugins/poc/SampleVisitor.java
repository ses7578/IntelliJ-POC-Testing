package org.scanl.plugins.poc;

import com.intellij.psi.*;
import org.graalvm.compiler.graph.Node;
import org.jetbrains.annotations.NotNull;
import org.scanl.plugins.poc.model.Class;
import org.scanl.plugins.poc.model.Method;
import org.scanl.plugins.poc.model.SmellType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SampleVisitor extends JavaRecursiveElementVisitor {
    private final List<Method> psiMethods = new ArrayList<>();
    private final List<Class> psiClasses = new ArrayList<>();
    private final List<SmellType> smellTypes = new ArrayList<>();




    @Override
    public void visitMethod(PsiMethod method) {
        boolean methodIssue = false;
        boolean empty = false;
        Method.MethodType methodType = Method.MethodType.Method;
        if (method.isConstructor())
            methodType = Method.MethodType.Constructor;
        if(method.getBody().isEmpty()) {
            empty = true;
            smellTypes.add(SmellType.EMPTY_METHOD);
        }
        PsiStatement @NotNull [] statements = method.getBody().getStatements();
        for(PsiStatement statement: statements) {
            if(statement instanceof PsiExpressionStatement)
            {
                PsiExpressionStatement expressionStatement = (PsiExpressionStatement) statement;
                PsiExpression expression = expressionStatement.getExpression();
                if(expression instanceof PsiMethodCallExpression) {
                    PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expression;
                    if (methodCallExpression.getMethodExpression().getQualifierExpression() != null) {
                        PsiType s = methodCallExpression.getMethodExpression().getQualifierExpression().getType();
                        if(s!=null) {
                            if (hasRedundantIssue(methodCallExpression))
                                methodIssue = true;
                        }
                    }
                }
            }
        }

        if(methodIssue || empty) {
            getPsiMethods().add(new Method(method.getName(), 1, 1, methodType, method, smellTypes));
        }

        super.visitMethod(method);
    }

    private boolean hasRedundantIssue(PsiMethodCallExpression expression)
    {
        PsiType s = Objects.requireNonNull(expression.getMethodExpression().getQualifierExpression()).getType();
        boolean match = s.getCanonicalText().equals("java.io.PrintStream");
        boolean match2 = Objects.equals(expression.getMethodExpression().getReferenceName(), "println");
        if(match || match2)
        {
            smellTypes.add(SmellType.REDUNDANT_PRINT);
            return true;
        }
        return false;
    }
    public List<Method> getPsiMethods() {
        return psiMethods;
    }

    public List<Class> getPsiClasses() {
        return psiClasses;
    }
}
