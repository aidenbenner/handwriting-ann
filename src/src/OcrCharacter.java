public class OcrCharacter implements Comparable<OcrCharacter> {

	public boolean returnAfter = false;
	public boolean spaceAfter = false;
	public double[] data;
	public int leftMax = 0;
	public int topMax = 0;
	public int botMax = 0;
	public int size = 0;
	public int width = 0;
	public int height = 0;
	public int imIndex = 0;

	public double getAveY() {
		return (topMax - size / 2);
	}

	@Override
	public int compareTo(OcrCharacter o) {
		if (leftMax < o.leftMax) {
			if (Math.abs(topMax - o.topMax) < (size)) {
				return -1;
			} else {
				return topMax > o.topMax ? 1 : -1;
			}
		} else {

			if (Math.abs(topMax - o.topMax) < (size)) {
				return 1;
			} else {
				return topMax > o.topMax ? 1 : -1;
			}

		}
	}

}
