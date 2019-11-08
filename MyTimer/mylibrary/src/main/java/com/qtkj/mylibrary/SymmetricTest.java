package com.qtkj.mylibrary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class SymmetricTest {

	private static final String pathData = "M223.78,593.48s94.12,27.52 187.49,159c107,150.75 244.49,150.75 244.49,150.75S475.56,974 348.31,890C174.9,775.41 223.78,593.48 223.78,593.48z";
	private static final String[] tag = {"H", "V","z", "Z", "M",  " ", "s", "S", "c", "C", "l", "L", "q", "Q", "t", "T", "a", "A"};
	private static final int specialTagNum = 6;

	public static void main(String[] args) {
		ArrayList<PointF> points = getPoint(pathData.substring(1), true);
		for (PointF point : points) {
			System.out.println(point.x + " " + point.y);
		}
	}

	private boolean isLine(int index){
		return index < 2;
	}

	private boolean isSpace(int index){
		return index == 5;
	}

	static class PointF {
		private float x;
		private float y;

		public PointF(String x, String y) {
			this.x = Float.parseFloat(x);
			this.y = Float.parseFloat(y);
		}

		public PointF(String x, String y, PointF last) {
			this.x = last.x + Float.parseFloat(x);
			this.y = last.y + Float.parseFloat(y);
		}
	}



	private static ArrayList<PointF> getPoint(String pathData, boolean isReal) {
		ArrayList<String> tags = new ArrayList<>(Arrays.asList(tag));
		ArrayList<PointF> result = new ArrayList<>();
		ArrayList<Character> tagList = new ArrayList<>();
		boolean real = true;
		for (int start = 0, end = 1; end < pathData.length(); ) {
			int index = tags.indexOf(String.valueOf(pathData.charAt(end)));
			if (index != - 1) {
				String sPoint = pathData.substring(start, end);
				String[] _point = sPoint.split(",");
				if (index < 2){
				}else {
					getPointF(isReal, result, real, _point);
				}
				tagList.add(pathData.charAt(end));

				if (index % 2 != 0) {
					real = true;
				} else if (index < specialTagNum) {
					real = real;
				} else {
					real = false;
				}

				start = end + 1;
				end = start + 1;
			} else {
				end++;
			}
		}

		handleSymmetricPoint(result, tagList);

		return result;
	}

	private static void getPointF(boolean isReal, ArrayList<PointF> result, boolean real, String[] _point) {
		if (isReal) {
			if (real) {
				result.add(new PointF(_point[0], _point[1]));
			} else {
				PointF last = result.get(result.size() - 1);
				result.add(new PointF(_point[0], _point[1], last));
			}
		} else {
			result.add(new PointF(_point[0], _point[1]));
		}
	}

	private static void handleSymmetricPoint(ArrayList<PointF> result, ArrayList<Character> tagList) {
		StringBuilder builder = new StringBuilder("M");
		for (int i = 0; i < result.size(); i++) {
			PointF pointF = result.get(i);
			builder.append(String.format(Locale.CHINA, "%.2f,%.2f%s"
					, pointF.x
					, pointF.y
					, String.valueOf(tagList.get(i)).toUpperCase()
			));
		}
		System.out.println(builder.toString());
	}

}
