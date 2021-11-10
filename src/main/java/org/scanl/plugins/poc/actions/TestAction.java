package org.scanl.plugins.poc.actions;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.indexing.FileBasedIndex;
import org.graalvm.compiler.lir.LIRInstruction;
import org.jetbrains.annotations.NotNull;
import org.scanl.plugins.poc.SampleVisitor;
import org.scanl.plugins.poc.model.Method;
import org.scanl.plugins.poc.model.SmellType;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestAction extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {

		Project project = e.getProject();
		Collection<VirtualFile> vFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, JavaFileType.INSTANCE, GlobalSearchScope.projectScope(project));

		DefaultMutableTreeNode Root = new DefaultMutableTreeNode("Root");
		List<Method> methods = new ArrayList<>();
		for (VirtualFile vf: vFiles) {
			methods.addAll(getDetails(vf, project));
		}

		for(SmellType smellType:SmellType.values()){
			parseTree(Root, methods, smellType);
		}

		JFrame f = new JFrame();

		JTree jt = new Tree(Root);
		jt.setRootVisible(false);
		f.add(jt);
		f.setSize(600,480);
		f.setVisible(true);

	}

	private List<Method> getDetails(VirtualFile vf, Project project){
		PsiFile psiFile = PsiManager.getInstance(project).findFile(vf);
		List<Method> methods = new ArrayList<>();
		if(psiFile instanceof PsiJavaFile)
		{
			PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
			PsiClass @NotNull [] classes = psiJavaFile.getClasses();
			for(PsiClass psiClass: classes) {
				System.out.println(psiClass.getQualifiedName());
				SampleVisitor sv = new SampleVisitor();
				psiFile.accept(sv);
				methods = sv.getPsiMethods();
			}
		}
		return methods;
	}

	private void parseTree(DefaultMutableTreeNode tn, List<Method> methods, SmellType smellType){
		DefaultMutableTreeNode smellTypeNode = new DefaultMutableTreeNode(smellType);
		for(Method method:methods){
			if(method.getSmellTypeList().contains(smellType)){
				System.out.println(method.getName());
				DefaultMutableTreeNode methodNode = new DefaultMutableTreeNode(method.getName());
				smellTypeNode.add(methodNode);
			}
		}
		tn.add(smellTypeNode);
	}
}

