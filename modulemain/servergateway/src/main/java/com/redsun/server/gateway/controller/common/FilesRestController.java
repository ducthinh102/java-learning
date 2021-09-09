package com.redsun.server.gateway.controller.common;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.redsun.server.gateway.util.FileUtil;
import com.redsun.server.gateway.util.RestAPIHelper;

/**
 * Upload files Controller
 */
@RestController
@RequestMapping("files")
public class FilesRestController {
	
	public static final Logger logger = LoggerFactory.getLogger(FilesRestController.class);
	
	@Autowired
	private Environment env;

    @Transactional
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseEntity<?> uploadFile(@RequestParam("filePath") String filePath, @RequestParam("file") final MultipartFile file, HttpServletRequest request) throws IOException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
    	final String fileServerUrl = env.getProperty("fileserver.path");
    	final String absolutePath = System.getProperty("java.io.tmpdir").replace('\\', File.separatorChar);
        String filename = FileUtil.saveFileToLocalWithEncode(absolutePath, file);
        final LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new FileSystemResource(absolutePath + filename));
        map.add("filePath", filePath);
        // Header.
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("userinfo", request.getHeader("userinfo"));
        headers.set("ConId", request.getHeader("ConId"));
        final HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        // Call server.
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(fileServerUrl + "files/uploadFile");
        URI uri = builder.build().encode().toUri();
        ResponseEntity<?> result = RestAPIHelper.exchangeFileService(uri, HttpMethod.POST, headers, requestEntity);
        FileUtil.deleteFileFromLocal(absolutePath + filename);
        return result;
    }
	
	@RequestMapping(value = "uploadFiles", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> uploadFiles(@RequestParam("filePath") String filePath, @RequestParam("files") MultipartFile[] files, HttpServletRequest request) throws IOException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
    	final String fileServerUrl = env.getProperty("fileserver.path");
    	final String absolutePath = System.getProperty("java.io.tmpdir").replace('\\', File.separatorChar);
	    List<String> fileNames = FileUtil.saveFilesToLocalWithEncode(absolutePath, files);
	    final LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
	    for (String filename : fileNames) {
	    	map.add("files", new FileSystemResource(absolutePath + filename));
		}
	    map.add("filePath", filePath);
        // Header.
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("userinfo", request.getHeader("userinfo"));
        headers.set("ConId", request.getHeader("ConId"));
        final HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        // Call server.
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(fileServerUrl + "files/uploadFiles");
        URI uri = builder.build().encode().toUri();
        ResponseEntity<?> result = RestAPIHelper.exchangeFileService(uri, HttpMethod.POST, headers, requestEntity);
        for (String filename : fileNames) {
        	FileUtil.deleteFileFromLocal(absolutePath + filename);
		}
        return result;
	}

}
