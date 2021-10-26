package org.scanl.plugins.poc.model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class IdentifierTableModel extends AbstractTableModel {

    private final String[] columnNames = {
            "Type", "Name", "PsiObject"
    };

    private Object[][] data;

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
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

    public void constructExtraTable(List<String> classNames, List<Integer> methods, List<Boolean> issues)
    {
        data = new Object[classNames.size()][3];
        for(int i = 0; i<classNames.size(); i++)
        {
            data[i][0] = classNames.get(i);
            data[i][1] = methods.get(i);
            data[i][2] = issues.get(i);
        }
    }


}
