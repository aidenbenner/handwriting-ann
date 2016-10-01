import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class AnnParser {

	public static ArrayList<OcrCharacter> getDataFromFile(String path) {
		return CharacterGrabber.segmentImage(path);
	}

	public static void writeAnnOutputToFile(ArrayList<OcrCharacter> data, String path) throws Exception {
		NeuralNetwork ann = Trainer.readANNFromDisk();
		String outString = Trainer.getCharString(ann, data);
		BufferedWriter br = new BufferedWriter(new FileWriter(path));
		br.write(outString);
		br.flush();
		br.close();
	}
	
	public static void readAndOutput(String inPath, String outPath) throws Exception{
		writeAnnOutputToFile(getDataFromFile(inPath),outPath);
	}
	
	
	public static boolean correctExtension(String filename){
		String[] inputs = filename.split("\\.");
		if(UserInterface.approvedFileType.contains(inputs[inputs.length - 1])){
			return true;
		}
		return false;
		
	}
	
	public static void parseDirectory(String inPath, String outPath) throws Exception{
		File inFolder = new File(inPath);
		File outFolder = new File(outPath);
		
		LinkedList<File> filesToParse = new LinkedList<File>();
		
		File[] subFiles = inFolder.listFiles();
		
		for(int i = 0; i<subFiles.length; i++){
			if(subFiles[i].isFile()){
				//check if we have the right extension
				if(correctExtension(subFiles[i].getName())){
					filesToParse.add(subFiles[i]);
				}
			}
		}
		
		ListIterator<File> itr = filesToParse.listIterator();
		while(itr.hasNext()){
			File currFile = itr.next();
			writeAnnOutputToFile(getDataFromFile(currFile.getAbsolutePath()),outPath + "/" + currFile.getName() + ".txt");
		}
	}
	
	
}
