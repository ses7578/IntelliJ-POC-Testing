package org.scanl.plugins.poc.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiJavaFile;
import org.jetbrains.annotations.Nullable;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RefineryUtilities;
import org.scanl.plugins.poc.SampleVisitor;
import org.scanl.plugins.poc.model.Class;
import org.scanl.plugins.poc.model.Identifier;
import org.scanl.plugins.poc.model.Method;
import org.scanl.plugins.poc.ui.controls.PieChart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
        psiJavaFile.accept(sv);

        JPanel dialogPanel = new JPanel(new BorderLayout());

        PieChart pieChart = new PieChart(this.getTitle(), "Identifier Distribution", createDataset(null, sv.getPsiMethods()));
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


    private PieDataset createDataset(List<Class> classes, List<Method> methods) {
        List<Identifier> identifiers = new ArrayList<>();
        identifiers.addAll(classes);
        identifiers.addAll(methods);

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
