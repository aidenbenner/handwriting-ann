import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UserInterface extends JFrame implements ActionListener {

	public static ImagePanel rightImgPanel = new ImagePanel();
	public static ImagePanel leftImgPanel = new ImagePanel();

	public static HashSet<String> approvedFileType = new HashSet<String>();


	public static void main(String args[]) {
		approvedFileType.add("jpg");
		approvedFileType.add("png");
		approvedFileType.add("jp2");
		approvedFileType.add("jpeg");
		approvedFileType.add("jpe");
		approvedFileType.add("pgm");
		approvedFileType.add("ppm");
		UserInterface ui = new UserInterface();
		ui.setVisible(true);
		ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private JTextField inputFileTF;
	private JTextField outputFileTF;
	private JFileChooser fileChooser = new JFileChooser();

	JButton btnRun = new JButton("Run!");

	JButton btnSelectInput = new JButton("Select");
	JButton btnSelect = new JButton("Select");
	JCheckBox chckbxParseDirectory;
	JCheckBox chckbxEnableConfidenceOutput;

	public UserInterface() {

		super("Optical Character Recognition Tool");
		rightImgPanel.setLocation(429, 202);
		rightImgPanel.setSize(384, 349);
		getContentPane().add(rightImgPanel);
		leftImgPanel.setLocation(39, 202);
		leftImgPanel.setSize(376, 349);
		getContentPane().add(leftImgPanel);
		setSize(847, 674);
		getContentPane().setLayout(null);

		inputFileTF = new JTextField();
		inputFileTF.setEditable(false);
		inputFileTF.setBounds(269, 10, 346, 20);
		getContentPane().add(inputFileTF);
		inputFileTF.setColumns(10);

		outputFileTF = new JTextField();
		outputFileTF.setColumns(10);
		outputFileTF.setEditable(false);
		outputFileTF.setBounds(269, 41, 346, 20);
		getContentPane().add(outputFileTF);

		btnSelectInput.addActionListener(this);
		btnSelectInput.setBounds(639, 8, 89, 23);
		getContentPane().add(btnSelectInput);

		btnRun.addActionListener(this);

		btnSelect.addActionListener(this);
		btnSelect.setBounds(639, 39, 89, 23);
		getContentPane().add(btnSelect);

		JLabel lblInputDirectory = new JLabel("Input Directory");
		lblInputDirectory.setBounds(39, 16, 194, 14);
		getContentPane().add(lblInputDirectory);

		JLabel label = new JLabel("Output Directory\r\n");
		label.setBounds(39, 47, 212, 14);
		getContentPane().add(label);

		btnRun.setBounds(581, 135, 182, 23);
		getContentPane().add(btnRun);

		chckbxEnableConfidenceOutput = new JCheckBox("Enable Confidence Output");
		chckbxEnableConfidenceOutput.setBounds(154, 81, 250, 23);
		getContentPane().add(chckbxEnableConfidenceOutput);

		JLabel lblPreview = new JLabel("Preview:");
		lblPreview.setBounds(39, 180, 70, 15);
		getContentPane().add(lblPreview);

		chckbxParseDirectory = new JCheckBox("Parse Directory");
		chckbxParseDirectory.setBounds(154, 108, 194, 23);
		getContentPane().add(chckbxParseDirectory);
		chckbxParseDirectory.addActionListener(this);
	}

	public boolean isDirectoryMode() {
		return chckbxParseDirectory.isSelected();
	}

	public boolean confidenceModeEnabled() {
		return chckbxEnableConfidenceOutput.isSelected();
	}

	public void clearTextBoxes() {
		inputFileTF.setText("");
		outputFileTF.setText("");
	}

	public String getInputDirectory() {
		return inputFileTF.getText();
	}

	public String getOutputDirectory() {
		return outputFileTF.getText();
	}

	public boolean checkIfFileTypeValid(String fp) {
		if (fp == "") {
			return true;
		}
		String[] fpbreaks = fp.split("\\.");
		if (!approvedFileType.contains(fpbreaks[fpbreaks.length - 1])) {
			JOptionPane
					.showMessageDialog(
							this,
							"please enter one of the following file types: jpg, png, jp2, jpeg, jpe, pgm, ppm ");
			return false;
		}
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Trainer.confidenceMode = confidenceModeEnabled();
		if (e.getSource().equals(btnSelect)) {
			fileChooser.showOpenDialog(this);
			fileChooser.getSelectedFile();
			String fp = fileChooser.getSelectedFile().getAbsolutePath();

//			if (!checkIfFileTypeValid(fp)) {
//			 return;
//			}

			outputFileTF.setText(fileChooser.getSelectedFile()
					.getAbsolutePath());

		}
		if (e.getSource().equals(btnSelectInput)) {
			fileChooser.showOpenDialog(this);
			String fp = fileChooser.getSelectedFile().getAbsolutePath();
			if (!isDirectoryMode()) {
				if (!checkIfFileTypeValid(fp)) {
					return;
				}
			}
			inputFileTF
					.setText(fileChooser.getSelectedFile().getAbsolutePath());
			leftImgPanel.grabImage(getInputDirectory());
			leftImgPanel.repaint();
			// getContentPane().repaint();
		}
		if (e.getSource().equals(btnRun)) {
			try {
				Trainer.confidenceMode = confidenceModeEnabled();
				if(getOutputDirectory().isEmpty()){
					JOptionPane
					.showMessageDialog(
							this,
							"please enter an output file ");
					return;
					
				}
				if(getInputDirectory().isEmpty()){
					JOptionPane
					.showMessageDialog(
							this,
							"please enter an input file ");
					return;
					
				}
				if (isDirectoryMode()) {
					AnnParser.parseDirectory(getInputDirectory(),
							getOutputDirectory());
					UserInterface.rightImgPanel.grabImage("output.png");
					UserInterface.rightImgPanel.repaint();
				} else {
					AnnParser.readAndOutput(getInputDirectory(),
							getOutputDirectory());
					UserInterface.rightImgPanel.grabImage("output.png");
					UserInterface.rightImgPanel.repaint();
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (e.getSource().equals(chckbxParseDirectory)) {
			if (chckbxParseDirectory.isSelected()) {
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				clearTextBoxes();
			} else {
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				clearTextBoxes();
			}
		}
	}
}