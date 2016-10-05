package com.iticket.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;

public class HexTest {
	public static void main(String[] args) throws IOException, DecoderException {
		File file = new File("f:\\hex\\new.jpg");
		byte[] b = FileUtils.readFileToByteArray(file);
		String encodeHexString = Hex.encodeHexString(b);
		System.out.println(encodeHexString);
		byte[] x = Hex.decodeHex(encodeHexString.toCharArray());
		File file1 = new File("f:\\hex\\new1.jpg");
		FileUtils.writeByteArrayToFile(file1, x);
	}
}
