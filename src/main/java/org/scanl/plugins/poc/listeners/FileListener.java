package org.scanl.plugins.poc.listeners;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.scanl.plugins.poc.ui.IdentifierListingToolWindow;

import java.util.List;

public class FileListener implements BulkFileListener {
    private final Project project;

    public FileListener(Project project) {
        this.project = project;
    }

//    @Override
//    public void before(@NotNull List<? extends @NotNull VFileEvent> events) {
//        IdentifierListingToolWindow.refreshList(null);
//
//        for (VFileEvent event:events) {
//            @Nullable VirtualFile virtualFile = event.getFile();
//            if (virtualFile != null) {
//                PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
//                if (psiFile instanceof PsiJavaFile)
//                    IdentifierListingToolWindow.refreshList((PsiJavaFile)psiFile);
//            }
//        }
//    }
}
