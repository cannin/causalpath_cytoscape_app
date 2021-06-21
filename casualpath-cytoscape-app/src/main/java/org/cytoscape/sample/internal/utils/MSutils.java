package org.cytoscape.sample.internal.utils;

public class MSutils
{

	/**
	 * Converts the Java primary list into an R list format (e.g. c(1.0,2.0))
	 * 
	 * @param values
	 * @return
	 */
	public static String getAsRList(double[] values)
	{
		StringBuilder rValuesList = new StringBuilder("c(");
		for (int i = 0; i < values.length; i++)
		{
			rValuesList.append(values[i]);

			if (i < (values.length - 1))
				rValuesList.append(",");
		}
		rValuesList.append(")");

		return rValuesList.toString();
	}

	/**
	 * Converts the Java primary list into an R list format (e.g. c(1.0,2.0))
	 * 
	 * @param values
	 * @return
	 */
	public static String getAsRList(float[] values)
	{
		StringBuilder rValuesList = new StringBuilder("c(");
		for (int i = 0; i < values.length; i++)
		{
			rValuesList.append(values[i]);

			if (i < (values.length - 1))
				rValuesList.append(",");
		}
		rValuesList.append(")");

		return rValuesList.toString();
	}

	/**
	 * Converts the Java primary list into an R list format (e.g. c(1,2,3,4))
	 * 
	 * @param values
	 * @return
	 */
	public static String getAsRList(int[] values)
	{
		StringBuilder rValuesList = new StringBuilder("c(");
		for (int i = 0; i < values.length; i++)
		{
			rValuesList.append(values[i]);

			if (i < (values.length - 1))
				rValuesList.append(",");
		}
		rValuesList.append(")");

		return rValuesList.toString();
	}

	/**
	 * Converts the Java primary list into an R list format (e.g.
	 * c("Hello","world"))
	 * 
	 * @param values
	 * @return
	 */
	public static String getAsRList(String[] values)
	{
		StringBuilder rValuesList = new StringBuilder("c(");
		for (int i = 0; i < values.length; i++)
		{
			rValuesList.append("\"");
			rValuesList.append(values[i]);
			rValuesList.append("\"");

			if (i < (values.length - 1))
				rValuesList.append(",");
		}
		rValuesList.append(")");

		return rValuesList.toString();
	}

	/**
	 * Converts the Java primary list into an R list format (e.g.
	 * c('H','e','l','l','o'))
	 * 
	 * @param values
	 * @return
	 */
	public static String getAsRList(char[] values)
	{
		StringBuilder rValuesList = new StringBuilder("c(");
		for (int i = 0; i < values.length; i++)
		{
			rValuesList.append("'");
			rValuesList.append(values[i]);
			rValuesList.append("'");

			if (i < (values.length - 1))
				rValuesList.append(",");
		}
		rValuesList.append(")");

		return rValuesList.toString();
	}

	/**
	 * Method to fix file paths in windows. Backward slash, typically used in
	 * windows file paths, is processed as the starting char for hexdecimal
	 * codes in R.
	 * 
	 * Replacing backward slash with forward slash solves the problem as it is
	 * also support by windows.
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getWindowsCorrectPath(String filePath)
	{
		String filePath2 = filePath;
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
			filePath2 = filePath.replace("\\", "/");

		return filePath2;
	}

	public static String getJavaCorrectPath(String filePath)
	{
		String filePath2 = filePath;
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
			filePath2 = filePath.replace("/", "\\");

		return filePath2;
	}
}
