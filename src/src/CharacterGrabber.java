import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class CharacterGrabber {

	static class Line {
		public int startY;
		public int endY;

		public int getHeight() {
			return Math.abs(startY - endY);
		}
	}

	// returns sum of columns from inmat
	public static double[] getCols(Mat in) {
		double[] cols = new double[in.cols()];
		for (int i = 0; i < in.cols(); i++) {
			for (int k = 0; k < in.rows(); k++) {
				cols[i] += in.get(k, i)[0];
			}
		}
		return cols;
	}

	// returns sum of rows from inmat
	public static double[] getRows(Mat in) {
		double[] rows = new double[in.rows()];
		Arrays.fill(rows, 0);
		for (int i = 0; i < in.rows(); i++) {
			for (int k = 0; k < in.cols(); k++) {
				rows[i] += in.get(i, k)[0];
			}
		}
		return rows;
	}

	// used for vertical segmentation
	public static class CharLocation {
		public int startX;
		public int endX;

		public int getLength() {
			return Math.abs(startX - endX);
		}
	}

	public static final double annXSize = 32;
	public static final double annYSize = 32;

	static int charCount = 0;

	public static double[] matToANNDouble(Mat in) {

		// y/x
		double oldRatio = in.rows() / in.cols();

		double yNew;
		double xNew;
		// y/x
		// maintain the old ratio so take the largest of the two
		if (in.rows() >= in.cols()) {
			// y > x
			// y = 32, x = 32
			yNew = annXSize;
			xNew = (int) (annXSize / oldRatio);
		} else {
			xNew = annXSize;
			yNew = (int) annXSize * oldRatio;
			if (yNew == 0) {
				yNew = 20;
			}
		}

		Mat croppedMat = new Mat();
		Imgproc.resize(in, croppedMat, new Size(xNew, yNew));
		double[] output = new double[(int) (annXSize * annYSize) + 2];


		charCount++;
		int totalFilled = 0;
		// System.out.println("row size" + croppedMat.rows());
		// System.out.println("col size" + croppedMat.cols());
		for (int l = 0; l < croppedMat.rows(); l++) {
			for (int k = 0; k < croppedMat.cols(); k++) {
				if (croppedMat.get(k, l) == null) {
					output[l * croppedMat.rows() + k] = 0;
				} else {
					if ((croppedMat.get(l, k)[0] / 255) > 0) {
						totalFilled++;
						output[l * croppedMat.rows() + k] = 1;
					} else {
						output[l * croppedMat.rows() + k] = 0;
					}
				}
				// System.out.print(output[l * croppedMat.rows() + k] + " ");

			}
			// System.out.println();
		}

		output[output.length - 2] = 0;// in.rows() / in.cols();
		output[output.length - 1] = 0;// totalFilled / (annXSize * annYSize);
		return output;
	}

	public static Mat removeWhiteSpace(Mat inMat) {
		int top = 0;
		int bot = inMat.rows();

		double[] in = getRows(inMat);

		for (int k = 0; k < in.length; k++) {
			if (in[k] < 255) {
				top = k;
			} else {
				break;
			}
		}

		for (int j = in.length - 1; j >= 0; j--) {
			if (in[j] < 255) {
				bot = j;
			} else {
				break;
			}
		}

		Rect newCharBox = new Rect(new Point(0, 0), new Point(inMat.cols() - 1,
				bot));

		return new Mat(inMat, newCharBox);
	}

	public static ArrayList<OcrCharacter> segmentImage(String path) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat input = Imgcodecs.imread(path, 0);
		System.out.println(input.rows() + "  " + input.cols());

		// filter the iamge so we everything else is easy
		Imgproc.threshold(input, input, 210, 255, Imgproc.THRESH_BINARY_INV);

		// remove small contours
		double[] rows = getRows(input);

		// vertical segmentation
		// we want to split the rows data into an array of start of line and end
		// of line

		// take average of non zero rows
		double rowAve = 0;
		for (int i = 0; i < rows.length; i++) {
			if (rows[i] != 0) {
				rowAve += rows[i];
			}
		}
		rowAve /= rows.length;

		ArrayList<Line> lines = new ArrayList<Line>();
		Line currLine = null;
		for (int i = 0; i < rows.length; i++) {
			// starts iterating from the top of the image moves downward
			if (rows[i] > rowAve / 20) {
				if (currLine != null) {
					// we have a current line do nothing
				} else {
					// we do not have a current line
					// create a new one
					currLine = new Line();
					currLine.startY = i;
				}
			} else {
				// our current line is below the threshold
				if (currLine != null) {
					// we have a current line, end it
					currLine.endY = i;
					if (currLine.getHeight() >= 20) {
						// if the line is large enough add it
						lines.add(currLine);
					}
					// remove the current line
					currLine = null;
				}
			}
		}

		double aveCharWidth = matGetAveContourWidth(input);
		Mat outClone = input.clone();
		// okay we now have our lines now segment vertically
		ArrayList<OcrCharacter> outputChars = new ArrayList<OcrCharacter>();
		int lineCount = 1;
		for (Line l : lines) {

			// create a mat of each line
			Rect lineRect = new Rect();
			lineRect.y = l.startY;
			lineRect.x = 0;
			lineRect.height = Math.abs(l.startY - l.endY);
			lineRect.width = input.cols();
			Mat segLine = new Mat(input, lineRect);

			// create something to store location of all our characters
			ArrayList<CharLocation> charsOnLine = new ArrayList<CharLocation>();

			// get average of each column for splitting later
			double[] lineCols = getCols(segLine);

			// find the column average
			double colSum = 0;
			for (int i = 0; i < lineCols.length; i++) {
				if (lineCols[i] != 0) {
					colSum += lineCols[i];
				}
			}
			double colAve = colSum / lineCols.length;
			// split the line into characters
			CharLocation currentChar = null;
			for (int i = 0; i < lineCols.length; i++) {
				// check if the currAve has a char at it, or it's a space
				if (lineCols[i] > (colAve / 8)) {
					if (currentChar == null) {
						currentChar = new CharLocation();
						currentChar.startX = i;
					} else {
						// already are counting, do nothing
					}
				} else {
					if (currentChar == null) {
						// do nothing
					} else {
						// System.out.println(lineCount + "   " + i);
						currentChar.endX = i;
						charsOnLine.add(currentChar);
						if (currentChar.getLength() < 3) {
							currentChar = null;
							continue;
						}
						Rect charBox = new Rect(new Point(currentChar.startX,
								l.startY), new Point(currentChar.endX, l.endY));
						Mat out = removeWhiteSpace(new Mat(input, charBox));

						// look for contours
						List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
						Mat image32S = out.clone();
						Mat hierarchy = new Mat();
						Imgproc.findContours(image32S, contours, hierarchy,
								Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

						if (contours.size() >= 2) {
							// we need to split if character is not an i or a j

							MatOfPoint2f curve = new MatOfPoint2f();
							MatOfPoint2f findCont = new MatOfPoint2f(contours
									.get(0).toArray());
							double dist = Imgproc.arcLength(findCont, true) * 0.02;
							Imgproc.approxPolyDP(findCont, curve, dist, true);
							MatOfPoint points = new MatOfPoint(curve.toArray());
							Rect boundingRect = Imgproc.boundingRect(points);
							// find ave x value of bounding rect
							double aveX = boundingRect.width / 2
									+ boundingRect.x;
							double aveW = boundingRect.width;

							findCont = new MatOfPoint2f(contours.get(1)
									.toArray());
							dist = Imgproc.arcLength(findCont, true) * 0.02;
							Imgproc.approxPolyDP(findCont, curve, dist, true);
							points = new MatOfPoint(curve.toArray());
							boundingRect = Imgproc.boundingRect(points);
							double aveX2 = boundingRect.width / 2
									+ boundingRect.x;

							// System.out.println("detected ");
							if (aveX > aveX2) {
								// System.out.println("detected contours ");

							}

						}

						Imgproc.rectangle(outClone, new Point(
								currentChar.startX, l.startY), new Point(
								currentChar.endX, l.endY), new Scalar(255, 255,
								255), 2);
						OcrCharacter outChar = new OcrCharacter();
						if (charsOnLine.size() >= 2) {
							double spaceDis = Math
									.abs(charsOnLine.get(charsOnLine.size() - 2).endX
											- charsOnLine.get(charsOnLine
													.size() - 1).startX);
							// System.out.println(spaceDis + "  " +
							// aveCharWidth);
							if (spaceDis >= aveCharWidth / 2) {
								outputChars.get(outputChars.size() - 1).spaceAfter = true;
							}
						}

						outChar.data = matToANNDouble(out);
						outputChars.add(outChar);
						currentChar = null;
					}
				}

			}

			outputChars.get(outputChars.size() - 1).returnAfter = true;
			// split the mat into columns breaking at white space
			lineCount++;

		}

		Imgcodecs.imwrite("output.png", outClone);

		System.out.println(outputChars.size());
		return outputChars;
	}

	public static double matGetAveContourWidth(Mat input) {

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat image32S = input.clone();
		Mat hierarchy = new Mat();

		Imgproc.findContours(image32S, contours, hierarchy, Imgproc.RETR_CCOMP,
				Imgproc.CHAIN_APPROX_SIMPLE);

		Mat contourImg = image32S;

		MatOfPoint2f curve = new MatOfPoint2f();

		ArrayList<OcrCharacter> outputChars = new ArrayList<OcrCharacter>();
		int charCount = 0;
		// first get the ave size of a contour

		double aveWidth = 0;
		double aveHeight = 0;
		int counted = 0;
		// we need over 32 pixels to do a good read anyways
		double sizeThresh = 5;
		for (int i = 0; i < contours.size(); i++) {
			MatOfPoint2f findCont = new MatOfPoint2f(contours.get(i).toArray());
			double dist = Imgproc.arcLength(findCont, true) * 0.02;

			Imgproc.approxPolyDP(findCont, curve, dist, true);

			MatOfPoint points = new MatOfPoint(curve.toArray());

			Rect rect = Imgproc.boundingRect(points);

			if (rect.width > sizeThresh && rect.height > sizeThresh) {
				aveWidth += rect.width;
				aveHeight += rect.height;
				counted++;
			}
		}
		aveWidth = aveWidth / counted;
		aveHeight = aveHeight / counted;
		return aveWidth;
	}

}