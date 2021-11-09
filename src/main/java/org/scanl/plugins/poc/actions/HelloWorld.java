package org.scanl.plugins.poc.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;
import org.scanl.plugins.poc.SampleVisitor;
import org.scanl.plugins.poc.model.Method;
import org.scanl.plugins.poc.model.SmellType;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

public class HelloWorld extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        Project project = e.getProject();
        VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();

        DefaultMutableTreeNode Root = new DefaultMutableTreeNode("Root");
        for (VirtualFile vf: vFiles) {
            parseTree(Root, vf, project);
        }

        JFrame f = new JFrame();

        JTree jt = new Tree(Root);
        f.add(jt);
        f.setSize(600,480);
        f.setVisible(true);

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

}
