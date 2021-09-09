package com.redsun.server.main.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
	
	// Save a file to loccal computer and return map of resources.
	public final static LinkedMultiValueMap<String, Object> saveFileToLocalReturnLinkedMultiValueMap(final MultipartFile file) throws IOException {
		LinkedMultiValueMap<String, Object> result = new LinkedMultiValueMap<String, Object>();
		ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
			@Override
			public String getFilename() {
				String fileName = file.getOriginalFilename();
	            try {
					fileName = URLDecoder.decode(fileName, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return fileName;
			}
		};
        // Add map to result.
        result.add("file", fileResource);

		return result;
	}
	
	// Save files to loccal computer and return map of resources.
	public final static LinkedMultiValueMap<String, Object> saveFilesToLocalReturnLinkedMultiValueMap(final MultipartFile[] files) throws IOException {
		LinkedMultiValueMap<String, Object> result = new LinkedMultiValueMap<String, Object>();
	    for (MultipartFile file : files) {
	        if (file.isEmpty()) {
	            continue;
	        }
	        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()){
		            @Override
		            public String getFilename(){
		            	String fileName = file.getOriginalFilename();
			            try {
							fileName = URLDecoder.decode(fileName, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return fileName;
		            }
		    };
            // Add map to result.
            result.add("files",fileResource);
	    }

		return result;
	}
	
	// Save files to loccal computer and return map of resources.
	public final static LinkedMultiValueMap<String, Object> saveFilesToLocalReturnLinkedMultiValueMap(final String absolutePath, final MultipartFile[] files) throws IOException {
		LinkedMultiValueMap<String, Object> result = new LinkedMultiValueMap<String, Object>();
		// Create folder if not.
	    File dir = new File(absolutePath);
        if (!dir.exists())
            dir.mkdirs();
        // Save files.
		String fileName;
		FileOutputStream fos;
	    for (MultipartFile file : files) {
	        if (file.isEmpty()) {
	            continue;
	        }
            byte[] bytes = file.getBytes();
            fileName = URLDecoder.decode(file.getOriginalFilename(), "UTF-8");
            fos = new FileOutputStream(absolutePath + fileName);
            fos.write(bytes);
            fos.close();
            // Add map to result.
            result.add("files", new FileSystemResource(fileName));
	    }

		return result;
	}
	
	// Delete a file from resources on local computer.
	public final static boolean deleteFileFromLocalForMultipartFile(final String absolutePath, final MultipartFile file) throws UnsupportedEncodingException {
		String fileName = URLDecoder.decode(file.getOriginalFilename(), "UTF-8");
		File f = new File(absolutePath + fileName);
		if(f.exists()) {
			f.delete();
		}
	    return true;
	}
	
	// Delete files from resources on local computer.
	public final static boolean deleteFilesFromLocalForMultipartFiles(final String absolutePath, final MultipartFile[] files) throws UnsupportedEncodingException {
	    for (MultipartFile file : files) {
	    	String fileName = URLDecoder.decode(file.getOriginalFilename(), "UTF-8");
			File f = new File(absolutePath + fileName);
	        if(f.exists()) {
				f.delete();
			}
	    }
	    return true;
	}
	
	// Save a file to loccal computer.
	public final static String saveFileToLocalWithEncode(final String absolutePath, final MultipartFile file) throws IOException {
		// Create folder if not.
	    File dir = new File(absolutePath);
        if (!dir.exists())
            dir.mkdirs();
        // Save files.
		String fileName = URLEncoder.encode(file.getOriginalFilename(), "UTF-8");
        File f = new File(absolutePath + fileName);
		file.transferTo(f);
		return fileName;
	}
	
	// Save a file to loccal computer.
	public final static String saveFileToLocal(final String absolutePath, final MultipartFile file) throws IOException {
		// Create folder if not.
	    File dir = new File(absolutePath);
        if (!dir.exists())
            dir.mkdirs();
        // Save files.
        String fileName = URLDecoder.decode(file.getOriginalFilename(), "UTF-8");
        File f = new File(absolutePath + fileName);
		file.transferTo(f);
/*
		FileOutputStream fos;
		byte[] bytes = file.getBytes();
        fos = new FileOutputStream(absolutePath + fileName);
        fos.write(bytes);
        fos.close();
*/
		return fileName;
	}
	
	// Save files to loccal computer.
	public final static List<String> saveFilesToLocalWithEncode(final String absolutePath, final MultipartFile[] files) throws IOException {
		List<String> result = new ArrayList<String>();
		// Create folder if not.
	    File dir = new File(absolutePath);
        if (!dir.exists())
            dir.mkdirs();
        // Save files.
		String fileName;
		FileOutputStream fos;
	    for (MultipartFile file : files) {
	        if (file.isEmpty()) {
	            continue;
	        }
            byte[] bytes = file.getBytes();
            fileName = URLEncoder.encode(file.getOriginalFilename(), "UTF-8");
            result.add(fileName);
            fos = new FileOutputStream(absolutePath + fileName);
            fos.write(bytes);
            fos.close();
	    }

		return result;
	}
	
	// Save files to loccal computer.
	public final static List<String> saveFilesToLocal(final String absolutePath, final MultipartFile[] files) throws IOException {
		List<String> result = new ArrayList<String>();
		// Create folder if not.
	    File dir = new File(absolutePath);
        if (!dir.exists())
            dir.mkdirs();
        // Save files.
		String fileName;
		FileOutputStream fos;
	    for (MultipartFile file : files) {
	        if (file.isEmpty()) {
	            continue;
	        }
            byte[] bytes = file.getBytes();
            fileName = URLDecoder.decode(file.getOriginalFilename(), "UTF-8");
            result.add(fileName);
            fos = new FileOutputStream(absolutePath + fileName);
            fos.write(bytes);
            fos.close();
	    }

		return result;
	}
	
	// Delete a file from local computer.
	public final static boolean deleteFileFromLocal(final String fileFullName) {
		File file = new File(fileFullName);
		if(file.exists()) {
			file.delete();
		}
		
		return true;
	}
	
	// Delete files from local computer.
	public final static boolean deleteFilesFromLocal(final List<String> fileFullNames) {
		for(String fileFullName : fileFullNames) {
			File file = new File(fileFullName);
			if(file.exists()) {
				file.delete();
			}
		}
		
		return true;
	}
	
	// Get file.
	public final static File getFile(final String fileFullName) throws FileNotFoundException {
		File file = new File(fileFullName);
		if(!file.exists()) {
			throw new FileNotFoundException("File with path as " + fileFullName + "was not found.");
		}
		
		return file;
	}
	
	// Rename a file from local computer.
	public final static boolean renameFileFromLocal(final String oldFileFullName, final String newFileFullName) {
		File oldfile =new File(oldFileFullName);
		File newfile =new File(newFileFullName);
		boolean result = oldfile.renameTo(newfile);
		return result;
	}
	
}
