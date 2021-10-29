package org.scanl.plugins.poc.model;

import com.intellij.psi.PsiElement;

import java.util.ArrayList;
import java.util.List;

public class Method implements Identifier {

    private final String name;
    private final int lineNumber;
    private final int columnNumber;
    private final PsiElement psiObject;
    private final List<SmellType> smellTypeList = new ArrayList<>();

    public Method(String name, int lineNumber, int columnNumber, PsiElement psiObject, List<SmellType> smellTypes){
        this.name = name;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.psiObject = psiObject;
        this.smellTypeList.addAll(smellTypes);
    }

    @Override
    public PsiElement getPsiObject() {
        return psiObject;
    }

    public String getName() {
        return name;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    @Override
    public String getType() {
        return null;
    }

    public List<SmellType> getSmellTypeList()
    {
        return smellTypeList;
    }

    public enum MethodType{
        Method,
        Constructor
    }
}
