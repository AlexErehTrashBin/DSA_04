package ru.vsu.cs.ereshkin_a_v.task04.gui;

import ru.vsu.cs.ereshkin_a_v.task04.heapsort.HeapSort;
import ru.vsu.cs.util.ArrayUtils;
import ru.vsu.cs.util.JTableUtils;
import ru.vsu.cs.util.SwingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;


public class FrameMain extends JFrame {
    private final JFileChooser fileChooserOpen;
    private final JFileChooser fileChooserSave;
    private JPanel panelMain;
    private JTable tableInput;
    private JButton buttonLoadInputFromFile;
    private JButton buttonRandomInput;
    private JButton buttonSaveInputInfoFile;
    private JButton buttonSolve;
    private JButton buttonSaveOutputIntoFile;
    private JTable tableOutput;
    private JTextField fromTextField;
    private JTextField toTextField;


    public FrameMain() {
        this.setTitle("FrameMain");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        JTableUtils.initJTableForArray(tableInput, 40, true, true, false, true);
        JTableUtils.initJTableForArray(tableOutput, 40, true, true, false, true);
        //tableOutput.setEnabled(false);
        tableInput.setRowHeight(25);
        tableOutput.setRowHeight(25);

        fileChooserOpen = new JFileChooser();
        fileChooserSave = new JFileChooser();
        fileChooserOpen.setCurrentDirectory(new File("."));
        fileChooserSave.setCurrentDirectory(new File("."));
        FileFilter filter = new FileNameExtensionFilter("Text files", "txt");
        fileChooserOpen.addChoosableFileFilter(filter);
        fileChooserSave.addChoosableFileFilter(filter);

        fileChooserSave.setAcceptAllFileFilterUsed(false);
        fileChooserSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserSave.setApproveButtonText("Save");

        JMenuBar menuBarMain = new JMenuBar();
        setJMenuBar(menuBarMain);

        JMenu menuLookAndFeel = new JMenu();
        menuLookAndFeel.setText("Вид");
        menuBarMain.add(menuLookAndFeel);
        SwingUtils.initLookAndFeelMenu(menuLookAndFeel);

        JTableUtils.writeArrayToJTable(tableInput, new int[][]{
                {7, 9, 2, 8, 4, 7, 2, 5, 9, 3}
        });

        this.pack();


        buttonLoadInputFromFile.addActionListener(actionEvent -> {
            try {
                if (fileChooserOpen.showOpenDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                    int[][] arr = ArrayUtils.readIntArray2FromFile(fileChooserOpen.getSelectedFile().getPath());
                    JTableUtils.writeArrayToJTable(tableInput, arr);
                }
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });
        buttonRandomInput.addActionListener(actionEvent -> {
            try {
                int[][] matrix = ArrayUtils.createRandomIntMatrix(
                        tableInput.getRowCount(), tableInput.getColumnCount(), 100);
                JTableUtils.writeArrayToJTable(tableInput, matrix);
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });
        buttonSaveInputInfoFile.addActionListener(actionEvent -> {
            try {
                if (fileChooserSave.showSaveDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                    int[][] matrix = JTableUtils.readIntMatrixFromJTable(tableInput);
                    String file = fileChooserSave.getSelectedFile().getPath();
                    if (!file.toLowerCase().endsWith(".txt")) {
                        file += ".txt";
                    }
                    ArrayUtils.writeArrayToFile(file, matrix);
                }
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });
        buttonSaveOutputIntoFile.addActionListener(actionEvent -> {
            try {
                if (fileChooserSave.showSaveDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                    int[][] matrix = JTableUtils.readIntMatrixFromJTable(tableOutput);
                    String file = fileChooserSave.getSelectedFile().getPath();
                    if (!file.toLowerCase().endsWith(".txt")) {
                        file += ".txt";
                    }
                    ArrayUtils.writeArrayToFile(file, matrix);
                }
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });
        buttonSolve.addActionListener(actionEvent -> {
            try {
                int[][] matrix = JTableUtils.readIntMatrixFromJTable(tableInput);
                if (matrix == null) {
                    JTableUtils.writeArrayToJTable(tableOutput, (int[][]) null);
                    return;
                }
                Integer[] arr = ArrayUtils.toObject(matrix[0]);
                int from;
                int to;
                try {
                    from = Integer.parseInt(fromTextField.getText());
                    to = Integer.parseInt(toTextField.getText());
                    if (from < 0 || from > arr.length) throw new Exception();
                    if (to < 0 || to > arr.length) throw new Exception();
                } catch (Exception e) {
                    SwingUtils.showInfoMessageBox("Введите корректные целочисленные значения в пределах размера массива!");
                    return;
                }

                HeapSort.sort(arr, from, to);
                JTableUtils.writeArrayToJTable(tableOutput, ArrayUtils.toPrimitive(arr));
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });
    }
}
