package com.xxb.reactive;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

import org.apache.commons.io.FileUtils;

public class FileTest {
	
	public static void main(String... args) {
		Instant start = Instant.now();
		long size = FileUtils.sizeOfDirectory(new File("D:\\Workfolder\\Resource\\Weixin"));
		System.out.println("size is "+size/(1024*1024));
		Instant end = Instant.now();
		System.out.println("used time "+Duration.between(start, end).toMillis());
	}

}
