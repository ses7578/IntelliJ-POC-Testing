package org.scanl.plugins.poc.listeners;


import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.scanl.plugins.poc.ui.IdentifierListingToolWindow;

import java.util.Date;
//
// THIS CLASS IS NOT IN USE!!!
//
class DocListener implements DocumentListener {


    @Override
    public void beforeDocumentChange(@NotNull DocumentEvent event) {
        DocumentListener.super.beforeDocumentChange(event);
    }

    @Override
    public void documentChanged(@NotNull DocumentEvent event) {
        Document document = event.getDocument();
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
        System.out.println("update :"+ new Date().getTime());
//        if (virtualFile != null) {
//            PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
//            if (psiFile instanceof PsiJavaFile)
//                IdentifierListingToolWindow.refreshList((PsiJavaFile)psiFile);
//        }
    }

    @Override
    public void bulkUpdateStarting(@NotNull Document document) {
        DocumentListener.super.bulkUpdateStarting(document);
    }


    @Override
    public void bulkUpdateFinished(@NotNull Document document) {
        DocumentListener.super.bulkUpdateFinished(document);
    }
}
