package org.scanl.plugins.poc;

import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.scanl.plugins.poc.model.Class;
import org.scanl.plugins.poc.model.Method;

import java.util.ArrayList;
import java.util.List;

public class SampleVisitor extends JavaRecursiveElementVisitor {
    private final List<Method> psiMethods = new ArrayList<>();
    private final List<Class> psiClasses = new ArrayList<>();


    @Override
    public void visitClass(PsiClass aClass) {
        Class.ClassType classType = Class.ClassType.Class;
        if (aClass.isEnum())
            classType = Class.ClassType.Enum;
        else if (aClass.isInterface())
            classType = Class.ClassType.Interface;
        else if (aClass.isAnnotationType())
            classType = Class.ClassType.Annotation;
        else if (aClass.isRecord())
            classType = Class.ClassType.Record;

        getPsiClasses().add(new Class(aClass.getName(),1,1, classType, aClass));
        super.visitClass(aClass);
    }

    @Override
    public void visitMethod(PsiMethod method) {
        Method.MethodType methodType = Method.MethodType.Method;
        if (method.isConstructor())
            methodType = Method.MethodType.Constructor;

        getPsiMethods().add(new Method(method.getName(),1,1, methodType, method));
        super.visitMethod(method);
    }

    public List<Method> getPsiMethods() {
        return psiMethods;
    }

    public List<Class> getPsiClasses() {
        return psiClasses;
    }
}
