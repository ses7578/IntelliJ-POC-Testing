package org.scanl.plugins.poc.inspections;

import com.intellij.psi.PsiMethod;
import org.scanl.plugins.poc.model.SmellType;

public interface SmellInspection {
	boolean hasSmell(PsiMethod method);
	SmellType getSmellType();
}
