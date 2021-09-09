package com.redsun.server.main.controller.common;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.redsun.server.main.util.FileUtil;

/**
 * Upload files Controller
 */
@RestController
@RequestMapping("files")
public class FilesRestController {
	
	public static final Logger logger = LoggerFactory.getLogger(FilesRestController.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private ServletContext servletContext;
	
	@RequestMapping(value = "uploadFile", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> uploadFile(@RequestParam("filePath") String filePath, @RequestParam("file") MultipartFile file) throws IOException {
		// Create File.
		filePath = URLDecoder.decode(filePath, "UTF-8");
	    final String absolutePath = (env.getProperty("filelocation") + filePath).replace('\\', File.separatorChar);
		// Rename old file.
	    Date currentDate = new Date();
		String fileName = URLDecoder.decode(file.getOriginalFilename(), "UTF-8");
		int index = fileName.lastIndexOf(".");
		String fName = fileName.substring(0, index);
		String fExtension = fileName.substring(index);
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	    FileUtil.renameFileFromLocal(absolutePath + fileName, absolutePath + fName + "_" + df.format(currentDate) + fExtension);
		// Create new files.
	    fileName = FileUtil.saveFileToLocal(absolutePath, file);
	    // Return result.
	    Map<String, Object> result = new HashMap<String, Object>();
	    result.put("fileName", fileName);
	    return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "uploadFiles", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> uploadFiles(@RequestParam("filePath") String filePath, @RequestParam("files") MultipartFile[] files) throws IOException {
		// Create File.
		filePath = URLDecoder.decode(filePath, "UTF-8");
	    final String absolutePath = (env.getProperty("filelocation") + filePath).replace('\\', File.separatorChar);
		// Create new files.
	    List<String> fileNames = FileUtil.saveFilesToLocal(absolutePath, files);
	    // Return result.
	    Map<String, Object> result = new HashMap<String, Object>();
	    result.put("fileNames", fileNames);
	    return new ResponseEntity<>(result, HttpStatus.OK);
	}

    @RequestMapping(value = "downloadFile", method = RequestMethod.GET)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> downloadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("fileFullName") String fileFullName) throws IOException {
	    // Prepare file path.
    	fileFullName = URLDecoder.decode(fileFullName, "UTF-8");
    	String absolutePath = (env.getProperty("filelocation") + fileFullName).replace('\\', File.separatorChar);
    	// Create header.
	    String mimeType = servletContext.getMimeType(absolutePath);
	    if(mimeType == null) {
	    	mimeType = "application/octet-stream";
	    }
        response.setContentType(mimeType);
	    response.addHeader("Content-Disposition", "attachmnet; filename=" + absolutePath);
	    // Return resource.
        return new ResponseEntity<>(new FileSystemResource(absolutePath), HttpStatus.OK);
    }
	
	@RequestMapping(value = "updateForDeleteFile", method = RequestMethod.PUT)
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> updateForDeleteFile(@RequestParam("fileFullName") String fileFullName) throws IOException {
		int index = fileFullName.lastIndexOf(".");
		String fName = fileFullName.substring(0, index);
		String fExtension = fileFullName.substring(index);
		fName = URLDecoder.decode(fName, "UTF-8");
	    final String absolutePath = (env.getProperty("filelocation") + fName).replace('\\', File.separatorChar);
		// Rename old file.
	    Date currentDate = new Date();
	    fName = URLDecoder.decode(fName, "UTF-8");
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	    FileUtil.renameFileFromLocal(absolutePath + fExtension, absolutePath + "_" + df.format(currentDate) + fExtension);
	    // Return result.
	    Map<String, Object> result = new HashMap<String, Object>();
	    result.put("fileName", fileFullName);
	    return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
