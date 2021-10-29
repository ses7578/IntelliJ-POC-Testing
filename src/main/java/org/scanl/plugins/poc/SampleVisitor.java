package org.scanl.plugins.poc;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.execution.junit.JUnitUtil;
import com.intellij.psi.*;
import org.graalvm.compiler.graph.Node;
import org.jetbrains.annotations.NotNull;
import org.scanl.plugins.poc.inspections.EmptyMethodInspection;
import org.scanl.plugins.poc.inspections.RedundantPrintInspection;
import org.scanl.plugins.poc.inspections.SmellInspection;
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
        List<SmellInspection> inspections = provider.getInspections();
        boolean issues = false;
        for(SmellInspection inspection:inspections){
            boolean helperIssue = inspection.hasSmell(method);
            if(helperIssue){
                issues = true;
                smellTypes.add(inspection.getSmellType());
            }
        }
        JUnitUtil.isTestAnnotated(method);
        //JUnitUtil.isTestAnnotated(method) checks if it is an annotated test
        if(issues) {
            getPsiMethods().add(new Method(method.getName(), 1, 1, method, smellTypes));
        }
        super.visitMethod(method);
    }

    public List<Method> getPsiMethods() {
        return psiMethods;
    }
}
