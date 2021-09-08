package org.scanl.plugins.poc.model;

import com.intellij.psi.PsiElement;

public interface Identifier {
    public String getName();
    public int getLineNumber();
    public int getColumnNumber();
    public String getType();
    public PsiElement getPsiObject();
}
