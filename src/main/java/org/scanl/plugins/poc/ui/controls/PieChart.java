package org.scanl.plugins.poc.ui.controls;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.PieDataset;


import javax.swing.*;

public class PieChart extends JFrame {

    public PieChart(String applicationTitle, String chartTitle, PieDataset dataset) {
        super(applicationTitle);
        JFreeChart chart = createChart(dataset, chartTitle);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    private JFreeChart createChart(PieDataset dataset, String chartTitle) {
        return ChartFactory.createPieChart(
                chartTitle,
                dataset,
                true,
                true,
                false);
    }
}

