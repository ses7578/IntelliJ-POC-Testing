package org.scanl.plugins.poc.ui;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RefineryUtilities;
import org.scanl.plugins.poc.SampleVisitor;
import org.scanl.plugins.poc.model.ClassModel;
import org.scanl.plugins.poc.model.Identifier;
import org.scanl.plugins.poc.model.Method;
import org.scanl.plugins.poc.ui.controls.PieChart;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class IdentifierDistributionDialogWrapper extends DialogWrapper {
    Project project;
    PsiJavaFile psiJavaFile;

    public IdentifierDistributionDialogWrapper(@Nullable Project project, PsiJavaFile psiJavaFile) {
        super(project);
        this.project = project;
        this.psiJavaFile = psiJavaFile;
        setTitle("Chart Example");
        init();
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {
        SampleVisitor sv = new SampleVisitor();
        Project project = ProjectManager.getInstance().getOpenProjects()[0];

        Collection<VirtualFile> testA = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, JavaFileType.INSTANCE, GlobalSearchScope.projectScope(project));
        ArrayList<String> classNames = new ArrayList<>();
        ArrayList<ClassModel> classesArray = new ArrayList<>();
        ArrayList<Method> methodTotal = new ArrayList<>();
        for(VirtualFile vf : testA)
        {
            PsiFile psiFile = PsiManager.getInstance(project).findFile(vf);
            if(psiFile instanceof  PsiJavaFile)
            {
                PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
                PsiClass @NotNull [] classes = psiJavaFile.getClasses();
                for(PsiClass psiClass: classes) {
                    psiFile.accept(sv);
                    List<Method> methods = sv.getPsiMethods();
                    ClassModel c = new ClassModel(psiClass.getQualifiedName(), 0,0, ClassModel.ClassType.Class,psiClass);
                    classesArray.add(c);
                    for (Method m : methods) {
                        classNames.add(psiFile.getName());
                        methodTotal.add(m);
                    }
                }
            }
        }

        JPanel dialogPanel = new JPanel(new BorderLayout());

        PieChart pieChart = new PieChart(this.getTitle(), "Identifier Distribution", createDataset(classesArray, methodTotal));
        pieChart.setSize(560, 367);
        pieChart.pack();
        pieChart.setVisible(true);
        RefineryUtilities.centerFrameOnScreen(pieChart);

        JLabel label = new JLabel("Hello!");
        label.setPreferredSize(new Dimension(100, 100));
       dialogPanel.add(label, BorderLayout.CENTER);
        dialogPanel.add(pieChart, BorderLayout.CENTER);

//
        return dialogPanel;
    }


    private PieDataset createDataset(List<ClassModel> classes, List<Method> methods) {
        List<Identifier> identifiers = new ArrayList<>();
        identifiers.addAll(classes);
        identifiers.addAll(methods);
        System.out.println(identifiers);

        Map<String, Long> identifierTypeCount =
                identifiers.stream()
                        .collect(groupingBy(Identifier::getType, counting()));

        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Long> entry : identifierTypeCount.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        return dataset;
    }
}
