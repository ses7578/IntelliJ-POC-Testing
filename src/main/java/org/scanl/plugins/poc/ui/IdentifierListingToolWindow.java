package org.scanl.plugins.poc.ui;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.util.PsiNavigateUtil;
import org.jetbrains.annotations.NotNull;
import org.scanl.plugins.poc.SampleVisitor;
import org.scanl.plugins.poc.model.IdentifierTableModel;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class IdentifierListingToolWindow {

    private static IdentifierListingToolWindow instance = null;
    private JPanel myToolWindowContent;
    private JLabel labelStatus;
    private JTable tableIdentifierData;
    private JLabel lablelSummary;
    private IdentifierTableModel data;

    public IdentifierListingToolWindow() {
        EditorFactory.getInstance().getEventMulticaster().addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                Document document = event.getDocument();
                Project project = ProjectManager.getInstance().getOpenProjects()[0];
                VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
                if (virtualFile != null) {
                    PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
                    if (psiFile instanceof PsiJavaFile)
                        refreshList((PsiJavaFile) psiFile);
                }
            }
        });

        tableIdentifierData.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                handleSelectionEvent(e);
            }
        });
        this.ShowIdentifiers(null);
    }

    public static void refreshList(PsiJavaFile javaFile) {
        if (instance == null) {
            instance = new IdentifierListingToolWindow();
        } else
            instance.ShowIdentifiers(javaFile);
    }

    private void ShowIdentifiers(@Nullable PsiJavaFile javaFile) {
        if (javaFile == null) {
            labelStatus.setText("Select a Java file for analysis!");
            lablelSummary.setText("");
            data = null;
            tableIdentifierData.setVisible(false);
        } else {
            SampleVisitor sv = new SampleVisitor();
            javaFile.accept(sv);

            data = new IdentifierTableModel();
            data.constructTable(sv.getPsiClasses(), sv.getPsiMethods());
            tableIdentifierData.setModel(data);
            labelStatus.setText("Analyzed File: " + javaFile.getName());
            lablelSummary.setText("Total number of methods: " + sv.getPsiMethods().size());
            tableIdentifierData.setVisible(true);
        }

        instance = this;
    }

    protected void handleSelectionEvent(ListSelectionEvent e) {
        if (e.getValueIsAdjusting())
            return;

        PsiElement data = (PsiElement) tableIdentifierData.getValueAt(tableIdentifierData.getSelectedRow(), 2);
        PsiNavigateUtil.navigate(data, true);
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }
}