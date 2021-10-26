package org.scanl.plugins.poc;

import com.intellij.psi.*;
import org.scanl.plugins.poc.model.Class;
import org.scanl.plugins.poc.model.Method;
import org.scanl.plugins.poc.model.SmellType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SampleVisitor extends JavaRecursiveElementVisitor {
    private final List<Method> psiMethods = new ArrayList<>();
    private final List<Class> psiClasses = new ArrayList<>();
    private final List<SmellType> smellTypes = new ArrayList<>();
    private boolean issues = false;

    boolean methodIssue = false;


    @Override
    public void visitMethod(PsiMethod method) {
        Method.MethodType methodType = Method.MethodType.Method;
        if (method.isConstructor())
            methodType = Method.MethodType.Constructor;
        if(method.getBody().isEmpty())
            smellTypes.add(SmellType.EMPTY_METHOD);
        if(methodIssue || method.getBody().isEmpty()) {
            issues = true;
            getPsiMethods().add(new Method(method.getName(), 1, 1, methodType, method, smellTypes));
        }
        super.visitMethod(method);
    }

    @Override
    public void visitMethodCallExpression(PsiMethodCallExpression expression) {
        super.visitMethodCallExpression(expression);
        if (expression.getMethodExpression().getQualifierExpression() == null)
            return;
        PsiType s = expression.getMethodExpression().getQualifierExpression().getType();
        if (s == null)
            return;
        methodIssue = hasRedundantIssue(expression);
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

    public List<SmellType> getSmellTypes(){
        return smellTypes;
    }

    public boolean hasIssues(){
        return issues;
    }
}
