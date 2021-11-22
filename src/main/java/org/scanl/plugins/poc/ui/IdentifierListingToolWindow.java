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
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;
import org.scanl.plugins.poc.SampleVisitor;
import org.scanl.plugins.poc.model.IdentifierTableModel;
import org.scanl.plugins.poc.model.Method;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        buttonAnalyze.addActionListener(e -> testList());
    }

    private void testList(){
        Project project = ProjectManager.getInstance().getOpenProjects()[0];

        Collection<VirtualFile> testA = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, JavaFileType.INSTANCE, GlobalSearchScope.projectScope(project));
        data = new IdentifierTableModel();
        ArrayList<String> classNames = new ArrayList<>();
        ArrayList<Method> methodTotal = new ArrayList<>();
        for(VirtualFile vf : testA)
        {
            PsiFile psiFile = PsiManager.getInstance(project).findFile(vf);
            if(psiFile instanceof  PsiJavaFile)
            {
                PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
                PsiClass @NotNull [] classes = psiJavaFile.getClasses();
                for(PsiClass psiClass: classes) {
                    SampleVisitor sv = new SampleVisitor();
                    psiFile.accept(sv);

                    List<Method> methods = sv.getPsiMethods();
                    for (Method m : methods) {
                        classNames.add(psiFile.getName());
                        methodTotal.add(m);
                    }
                }
            }
        }
        data.constructTable3(classNames, methodTotal);
        lablelSummary.setText("Total number of methods: " + methodTotal.size());
        tableIdentifierData.setModel(data);
        tableIdentifierData.setVisible(true);
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }
}
