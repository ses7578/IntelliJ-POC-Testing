package org.scanl.plugins.poc.ui;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;
import org.scanl.plugins.poc.SampleVisitor;
import org.scanl.plugins.poc.model.IdentifierTableModel;
import org.scanl.plugins.poc.model.Method;
import org.scanl.plugins.poc.model.SmellType;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SampleActionWindow {
	private JPanel panel1;
	private JTree tree1;
	private JButton button1;

	public SampleActionWindow() {
		EditorFactory.getInstance().getEventMulticaster().addDocumentListener(new DocumentListener() {
			@Override
			public void documentChanged(@NotNull DocumentEvent event) {
				Document document = event.getDocument();
				Project project = ProjectManager.getInstance().getOpenProjects()[0];
				VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
				if (virtualFile != null) {
					PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
					if (psiFile instanceof PsiJavaFile)
						testList();
				}
			}
		});
		button1.addActionListener(e -> testList());
	}

	private void testList(){
		Project project = ProjectManager.getInstance().getOpenProjects()[0];

		Collection<VirtualFile> vFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, JavaFileType.INSTANCE, GlobalSearchScope.projectScope(project));

		DefaultTreeModel model = (DefaultTreeModel)tree1.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
		root.removeAllChildren();

		for (VirtualFile vf: vFiles) {
			parseTree(root, vf, project);
		}

		model.reload(root);
	}

	private void parseTree(DefaultMutableTreeNode tn, VirtualFile vf, Project project) {
		DefaultMutableTreeNode vftn = new DefaultMutableTreeNode(vf.getName());

		if (vf.isDirectory()) {
			for (VirtualFile child : vf.getChildren()) {
				parseTree(vftn, child, project);
			}
		}
		PsiFile psiFile = PsiManager.getInstance(project).findFile(vf);
		if(psiFile instanceof PsiJavaFile)
		{
			PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
			PsiClass @NotNull [] classes = psiJavaFile.getClasses();
			for(PsiClass psiClass: classes) {
				SampleVisitor sv = new SampleVisitor();
				psiFile.accept(sv);

				List<Method> methods = sv.getPsiMethods();
				for (Method m : methods) {
					DefaultMutableTreeNode methodNode = new DefaultMutableTreeNode(m.getName());
					for(SmellType smellType:m.getSmellTypeList())
					{
						DefaultMutableTreeNode smellNode = new DefaultMutableTreeNode(smellType);
						methodNode.add(smellNode);
					}
					vftn.add(methodNode);
				}
			}
		}

		tn.add(vftn);
	}

	public JPanel getContent() {
		return panel1;
	}

	private void createUIComponents() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("SmellTypes");
		tree1 = new JTree(node);
	}
}
