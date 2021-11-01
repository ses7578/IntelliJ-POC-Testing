package org.scanl.plugins.poc.model;

import com.intellij.codeInspection.LocalInspectionTool;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IdentifierTableModel extends AbstractTableModel {

    private final String[] columnNames = {
            "Class", "Method"
    };

    private final List<SmellType> smellTypes = Arrays.asList(SmellType.values());
    private Object[][] data;

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length + smellTypes.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        if(column<2)
            return columnNames[column];
        else
            return String.valueOf(smellTypes.get(column-2));
    }

    @Override
    public java.lang.Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    public void constructTable(List<Class> classes, List<Method> methods){

        List<Identifier> identifiers = new ArrayList<>();
        identifiers.addAll(classes);
        identifiers.addAll(methods);

        data = new Object[identifiers.size()][3];
        for(int i=0; i<identifiers.size(); i++){
            data[i][0] = identifiers.get(i).getType();
            data[i][1] = identifiers.get(i).getName();
//            data[i][2] = identifiers.get(i).getLineNumber();
//            data[i][3] = identifiers.get(i).getColumnNumber();
            data[i][2] = identifiers.get(i).getPsiObject();
        }
    }

    public void constructTable2(List<String> classNames, List<Method> methods){
        data = new Object[classNames.size()][4];
        for(int i = 0; i<classNames.size(); i++){
            data[i][0] = classNames.get(i);
            data[i][1] = methods.get(i).getName();
            List<SmellType> smellList = methods.get(i).getSmellTypeList();
            data[i][2] = smellList.contains(SmellType.EMPTY_METHOD);
            data[i][3] = smellList.contains(SmellType.REDUNDANT_PRINT);
        }
    }

    public void constructTable3(List<String> classNames, List<Method> methods){
        data = new Object[classNames.size()][smellTypes.size()+2];
        for(int i = 0; i<classNames.size(); i++){
            data[i][0] = classNames.get(i);
            data[i][1] = methods.get(i).getName();
            List<SmellType> smellList = methods.get(i).getSmellTypeList();
            for(int x = 0; x<smellTypes.size(); x++){
                data[i][x+2] = smellList.contains(smellTypes.get(x));
            }
        }
    }
}
