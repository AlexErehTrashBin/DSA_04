package ru.vsu.cs.ereshkin_a_v.task04;

import ru.vsu.cs.ereshkin_a_v.task04.gui.FrameMain;
import ru.vsu.cs.ereshkin_a_v.task04.heapsort.HeapSort;
import ru.vsu.cs.util.ArrayUtils;
import ru.vsu.cs.util.SwingUtils;

import java.util.Locale;

public class Main {
	public static void main(String[] args) {
		SwingUtils.setLookAndFeelByName("Windows");
		Locale.setDefault(Locale.ROOT);
		//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		SwingUtils.setDefaultFont("Microsoft Sans Serif", 18);

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(() -> new FrameMain().setVisible(true));
	}
}