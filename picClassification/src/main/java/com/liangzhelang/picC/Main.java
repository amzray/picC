package com.liangzhelang.picC;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import com.drew.imaging.*;
import com.drew.metadata.*;
import com.drew.metadata.exif.*;

public class Main { 
	public static void main (String[] args) throws Exception {
		String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String path = new File(jarPath).getParent();
		System.out.println(path);
		System.out.println("tested!");
		
		String exifPattern = ".*Date/Time\\sOriginal.*";
		String datePattern = "\\d{4}:\\d{2}:\\d{2}";
		
		File root = new File(path);
		File[] flist = root.listFiles();
		
		Pattern pattern = Pattern.compile(datePattern);	
		
		for(File child:flist) {
			if(child.isFile()&&Pattern.matches(".*\\.(?i)cr2", child.getName())) {
				System.out.print(child.getAbsolutePath());
				Metadata metadata = ImageMetadataReader.readMetadata(child);
				Directory d =  metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
				Collection<Tag> tags = d.getTags();
				Iterator i = tags.iterator();
				String year = "";
				while (i.hasNext()) {
					Tag temp = (Tag) i.next();
					if(Pattern.matches(exifPattern, temp.toString())) {
						Matcher matcher = pattern.matcher(temp.toString());
						if(matcher.find()) {
							year = matcher.group();
							break;
						}
					}
				}
				year = year.replace(':', '-');
				File dst = new File(path+"\\"+year);
				if(!(dst.exists())) {
					dst.mkdir();
				}
				String newPath = dst.getAbsolutePath()+"\\"+child.getName();
				child.renameTo(new File(newPath));
				System.out.println(" HAS BEEN MOVED TO:"+newPath);
			}
		}
	}
}
