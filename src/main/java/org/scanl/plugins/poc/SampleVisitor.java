package org.scanl.plugins.poc;

import com.intellij.psi.*;
import org.scanl.plugins.poc.model.Class;
import org.scanl.plugins.poc.model.Method;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SampleVisitor extends JavaRecursiveElementVisitor {
    private final List<Method> psiMethods = new ArrayList<>();
    private final List<Class> psiClasses = new ArrayList<>();
    private boolean issues = false;

    boolean methodIssue = false;


    @Override
    public void visitMethod(PsiMethod method) {
        Method.MethodType methodType = Method.MethodType.Method;
        if (method.isConstructor())
            methodType = Method.MethodType.Constructor;
        if(methodIssue || method.getBody().isEmpty()) {
            issues = true;
            getPsiMethods().add(new Method(method.getName(), 1, 1, methodType, method));
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
        boolean match = s.getCanonicalText().equals("java.io.PrintStream");
        boolean match2 = Objects.equals(expression.getMethodExpression().getReferenceName(), "println");
        if (match || match2) {
            methodIssue = true;
        }
    }

    public List<Method> getPsiMethods() {
        return psiMethods;
    }

    public List<Class> getPsiClasses() {
        return psiClasses;
    }

    public boolean hasIssues(){
        return issues;
    }
}
