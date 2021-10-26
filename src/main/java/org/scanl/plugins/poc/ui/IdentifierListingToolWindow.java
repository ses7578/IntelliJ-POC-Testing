package org.scanl.plugins.poc.ui;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.PsiNavigateUtil;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;
import org.scanl.plugins.poc.SampleVisitor;
import org.scanl.plugins.poc.model.Class;
import org.scanl.plugins.poc.model.IdentifierTableModel;
import org.scanl.plugins.poc.model.Method;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

public class IdentifierListingToolWindow {

    private static IdentifierListingToolWindow instance = null;
    private JPanel myToolWindowContent;
    private JLabel labelStatus;
    private JTable tableIdentifierData;
    private JLabel lablelSummary;
    private JButton buttonAnalyze;
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
                        testList();
                }
            }
        });

        buttonAnalyze.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testList();
            }
        });

    }

    private void testList(){
        Project project = ProjectManager.getInstance().getOpenProjects()[0];

        Collection<VirtualFile> testA = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, JavaFileType.INSTANCE, GlobalSearchScope.projectScope(project));
        data = new IdentifierTableModel();
        ArrayList<String> classNames = new ArrayList<>();
        ArrayList<Integer> methodAmount = new ArrayList<>();
        ArrayList<Boolean> issues = new ArrayList<>();
        for(VirtualFile vf : testA)
        {
            PsiFile psiFile = PsiManager.getInstance(project).findFile(vf);
            if(psiFile instanceof  PsiJavaFile)
            {
                SampleVisitor sv = new SampleVisitor();
                psiFile.accept(sv);
                classNames.add(psiFile.getName());
                methodAmount.add(sv.getPsiMethods().size());
                issues.add(sv.hasIssues());
            }
        }
        data.constructExtraTable(classNames, methodAmount, issues);
        lablelSummary.setText("Total number of classes: " + classNames.size());
        tableIdentifierData.setModel(data);
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }
}
