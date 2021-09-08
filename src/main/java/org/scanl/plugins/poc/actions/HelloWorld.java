package org.scanl.plugins.poc.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.scanl.plugins.poc.SampleVisitor;
import org.scanl.plugins.poc.common.Util;
import org.scanl.plugins.poc.ui.IdentifierDistributionDialogWrapper;

import java.util.List;
import java.util.Objects;

public class HelloWorld extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        PsiJavaFile psiJavaFile =  Util.getPsiJavaFile(e.getProject());
        if (psiJavaFile == null){
            HandleError("Please open a Java file from the project", e.getProject());
            return;
        }

        IdentifierDistributionDialogWrapper id = new IdentifierDistributionDialogWrapper(e.getProject());
        id.show();

    }

    private void HandleError(String message, Project project){
        Messages.showMessageDialog(project,message,"SCANL POC - ERROR", Messages.getErrorIcon());
    }
}
