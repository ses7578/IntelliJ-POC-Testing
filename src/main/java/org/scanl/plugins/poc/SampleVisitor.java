package org.scanl.plugins.poc;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.execution.junit.JUnitUtil;
import com.intellij.psi.*;
import org.graalvm.compiler.graph.Node;
import org.jetbrains.annotations.NotNull;
import org.scanl.plugins.poc.inspections.EmptyMethodInspection;
import org.scanl.plugins.poc.inspections.RedundantPrintInspection;
import org.scanl.plugins.poc.inspections.TestSmellInspectionProvider;
import org.scanl.plugins.poc.model.Method;
import org.scanl.plugins.poc.model.SmellType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SampleVisitor extends JavaRecursiveElementVisitor {
    private final List<Method> psiMethods = new ArrayList<>();
    private final List<SmellType> smellTypes = new ArrayList<>();
    private final RedundantPrintInspection redundantPrintInspection = new RedundantPrintInspection();
    private final TestSmellInspectionProvider provider = new TestSmellInspectionProvider();



    @Override
    public void visitMethod(PsiMethod method) {
        Class<? extends LocalInspectionTool> @NotNull [] classes = provider.getInspectionClasses();
        List<Class<? extends LocalInspectionTool>> classList = Arrays.asList(classes);
        boolean methodIssue = false;
        boolean empty = false;
        Method.MethodType methodType = Method.MethodType.Method;
        if (method.isConstructor())
            methodType = Method.MethodType.Constructor;
        if(classList.contains(EmptyMethodInspection.class)) {
            if (method.getBody().isEmpty()) {
                empty = true;
                smellTypes.add(SmellType.EMPTY_METHOD);
            }
        }
        JUnitUtil.isTestAnnotated(method);
        //JUnitUtil.isTestAnnotated(method) checks if it is an annotated test
        List<PsiMethodCallExpression> methodCallExpressionList = getMethodExpressions(method);
        for(PsiMethodCallExpression methodCallExpression : methodCallExpressionList){
            if(classList.contains(RedundantPrintInspection.class)) {
                if (redundantPrintInspection.validStatement(methodCallExpression)) {
                    if (redundantPrintInspection.hasRedundantIssue(methodCallExpression)) {
                        methodIssue = true;
                        smellTypes.add(SmellType.REDUNDANT_PRINT);
                    }
                }
            }
        }


        if(methodIssue || empty) {
            getPsiMethods().add(new Method(method.getName(), 1, 1, methodType, method, smellTypes));
        }

        super.visitMethod(method);
    }

    private List<PsiMethodCallExpression> getMethodExpressions(PsiMethod method){
        List<PsiMethodCallExpression> methodCallExpressionList = new ArrayList<>();
        PsiStatement @NotNull [] statements = method.getBody().getStatements();
        for(PsiStatement statement: statements) {
            if(statement instanceof PsiExpressionStatement)
            {
                PsiExpressionStatement expressionStatement = (PsiExpressionStatement) statement;
                PsiExpression expression = expressionStatement.getExpression();
                if(expression instanceof PsiMethodCallExpression) {
                    PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expression;
                    methodCallExpressionList.add(methodCallExpression);
                }
            }
        }
        return methodCallExpressionList;
    }

    public List<Method> getPsiMethods() {
        return psiMethods;
    }
}
