package org.tain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

public class TestBubble01 {

	public static void main(String[] args) {
		//args = new String[] {"up", "/Users/kangmac/jar.tgz", "/Users/kangmac/_bubble.json"};
		//args = new String[] {"down", "/Users/kangmac/_bubble.json", "/Users/kangmac/_jar.tgz"};

		if (args.length != 3) {
			printUsage();
			System.exit(-1);
		}

		if (!"up".equals(args[0]) && !"down".equals(args[0])) {
			printUsage();
			System.exit(-1);
		}

		if ("up".equals(args[0])) {
			bubbleUp(args[1], args[2]);
			System.exit(-1);
		}

		if ("down".equals(args[0])) {
			bubbleDown(args[1], args[2]);
			System.exit(-1);
		}
	}

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	private static void printUsage() {
		System.out.println("----------------- USAGE -------------------------");
		System.out.println("KANG-20200714 >>>>> USAGE");
		System.out.println("$ java Bubble [up|down] SOURCE_FILE TARGET_FILE");
		System.out.println("ex) $ java Bubble up Light.tgz _bubble.txt");
		System.out.println("ex) $ java Bubble down _bubble.txt Light.tgz");
		System.out.println("----------------- SAMPLE -------------------------");
		System.out.println("$ tar cvzf jar.tgz *.jar");
		System.out.println("$ java org.tain.TestBubble01 up jar.tgz _bubble.txt");
		System.out.println("$ tar cvzf - _bubble.txt | split -b 10m - _bubble.tgz");
		System.out.println("$ ls");
		System.out.println("    _bubble.tgzaa");
		System.out.println("    _bubble.tgzab");
		System.out.println("    .....");
		System.out.println("    _bubble.tgzak");
		System.out.println("$ cat _bubble.tgz* | tar xvzf -");
		System.out.println("$ java org.tain.TestBubble01 down _bubble.txt _jar.tgz");
		System.out.println("$ diff jar.tgz _jar.tgz");
		System.out.println("$ tar xvzf _jar.tgz");
		System.out.println("---------------------------------------------------");
	}

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	private static void bubbleUp(String sourceFilePath, String targetFilePath) {
		System.out.printf("KANG-20200714 >>>> Bubble UP Process %s -> %s\n", sourceFilePath, targetFilePath);

		FileInputStream fis = null;
		BufferedWriter writer = null;
		try {
			fis = new FileInputStream(sourceFilePath);
			writer = new BufferedWriter(new FileWriter(targetFilePath, true));

			byte[] byteRead = new byte[1024 * 1024];
			String strWrite = null;
			int nRead = -1;
			int step = 0;
			writer.write("{");
			writer.write("\"project\":\"Lightnet Transfer\",");
			writer.write("\"descriptor\":\"Transfer Data\",");
			writer.write("\"date\":\"2020-07-15 08:34:23\",");
			writer.write("\"data\":[");
			while ((nRead = fis.read(byteRead)) != -1) {
				System.out.println("Bubble UP >>>>> wirte step: " + (++step));
				strWrite = byteArrayToHexString(byteRead, nRead);
				writer.write("\"");
				writer.write(strWrite);
				writer.write("\",");
			}
			writer.write("]");
			writer.write("}");
			writer.flush();
			System.out.println("Bubble UP >>>>> Finish ");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) try { fis.close(); } catch (Exception e) {}
			if (writer != null) try { writer.close(); } catch (Exception e) {}
		}
	}
	
	public static String byteArrayToHexString(byte[] bytes, int size) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < size; i++) {
			sb.append(String.format("%02X", bytes[i] & 0xff));
		}
		return sb.toString();
	}


	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	private static void bubbleDown(String sourceFilePath, String targetFilePath) {
		System.out.printf("KANG-20200714 >>>> Bubble DOWN Process %s -> %s\n", sourceFilePath, targetFilePath);

		BufferedReader reader = null;
		FileOutputStream fos = null;
		try {
			reader = new BufferedReader(new FileReader(sourceFilePath), 1024 * 1024 * 2);
			fos = new FileOutputStream(targetFilePath);
			
			String strRead = null;
			int step = 0;
			while ((strRead = reader.readLine()) != null) {
				System.out.println("Bubble DOWN >>>>> wirte step: " + (++step));
				byte[] byteWrite = hexStringToByteArray(strRead);
				fos.write(byteWrite);
			}
			System.out.println("Bubble DOWN >>>>> Finish ");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) try { reader.close(); } catch (Exception e) {}
			if (fos != null) try { fos.close(); } catch (Exception e) {}
		}
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}
}
