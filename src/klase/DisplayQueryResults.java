/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klase;

import com.sun.javafx.scene.control.skin.TableRowSkinBase;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class DisplayQueryResults extends JFrame {

    static final String DB_URL = "jdbc:mysql://localhost/book";
    static final String USERNAME = "root";
    static final String PASSWORD = "";
    static final String DEFAULT_QUERY = "SELECT * FROM authors";

    private ResultSetTableModel tableModel;
    private JTextArea queryArea;

    public DisplayQueryResults() {
        super("Displaying Query Results");
        try {
            tableModel = new ResultSetTableModel(DB_URL, USERNAME, PASSWORD, DEFAULT_QUERY);

            // set up JTextArea in which user types queries
            queryArea = new JTextArea(DEFAULT_QUERY, 3, 100);
            queryArea.setWrapStyleWord(true);
            queryArea.setLineWrap(true);

            JScrollPane scrollPane = new JScrollPane(queryArea,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            // set up JButton for submitting queries
            JButton submitButton = new JButton("Submit Query");

            // create Box to manage placement of queryArea and
            // submitButton in GUI
            Box boxNorth = Box.createHorizontalBox();
            boxNorth.add(scrollPane);
            boxNorth.add(submitButton);

            JTable resultTable = new JTable(tableModel);

            JLabel filterLabel = new JLabel("Filter:");
            final JTextField filterText = new JTextField();
            JButton filterButton = new JButton("Apply Filter");
            Box boxSouth = Box.createHorizontalBox();

            boxSouth.add(filterLabel);
            boxSouth.add(filterText);
            boxSouth.add(filterButton);

            // place GUI components on content pane
            add(boxNorth, BorderLayout.NORTH);
            add(new JScrollPane(resultTable), BorderLayout.CENTER);
            add(boxSouth, BorderLayout.SOUTH);

            submitButton.addActionListener((ActionEvent e) -> {
                try {
                    tableModel.setQuery(queryArea.getText());
                } catch (SQLException exc) {
                    JOptionPane.showMessageDialog(null, exc.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    tableModel.setQuery(DEFAULT_QUERY);
                    queryArea.setText(DEFAULT_QUERY);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
                    tableModel.disconnectFromDatabase();
                    System.exit(1);
                }
            });

            final TableRowSorter<TableModel> sorter = 
                    new TableRowSorter<>(tableModel);
            resultTable.setRowSorter(sorter);
            setSize(500, 250);
            setVisible(true);

            filterButton.addActionListener((ActionEvent e) -> {
                String text = filterText.getText();
                if (text.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    try {
                        sorter.setRowFilter(RowFilter.regexFilter(text));
                    } catch (PatternSyntaxException ps) {
                        JOptionPane.showMessageDialog(null, ps.getMessage());
                    }
                }
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            tableModel.disconnectFromDatabase();
            System.exit(1);
        }
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                tableModel.disconnectFromDatabase();
                System.exit(0);
            }

        });

    }

    public static void main(String[] args) {
        DisplayQueryResults displayQueryResults = new DisplayQueryResults();
    }

}
