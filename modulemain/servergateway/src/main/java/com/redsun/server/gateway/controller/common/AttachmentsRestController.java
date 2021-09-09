package com.redsun.server.gateway.controller.common;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.redsun.server.gateway.util.FileUtil;
import com.redsun.server.gateway.util.RestAPIHelper;

@RestController
@RequestMapping("attachment")
public class AttachmentsRestController {

	@Autowired
	private Environment env;

    @Transactional
    @RequestMapping(value = "/createWithFile", method = RequestMethod.POST)
    public ResponseEntity<?> createWithFile(@RequestParam("attachment") final String attachment, @RequestParam("file") final MultipartFile file, HttpServletRequest request) throws IOException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
    	final String fileServerUrl = env.getProperty("fileserver.path");
    	final String absolutePath = System.getProperty("java.io.tmpdir").replace('\\', File.separatorChar);
        String filename = FileUtil.saveFileToLocal(absolutePath, file);
        //file.transferTo(tempFile);
        final LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new FileSystemResource(absolutePath + filename));
        map.add("attachment", attachment);
        // Header.
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("userinfo", request.getHeader("userinfo"));
        headers.set("ConId", request.getHeader("ConId"));
        final HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        // Call server.
        ResponseEntity<?> result = RestAPIHelper.exchangeService(fileServerUrl + "attachment/createWithFile", HttpMethod.POST, headers, requestEntity, null);
        FileUtil.deleteFileFromLocal(absolutePath + filename);
        return result;
    }

    @Transactional
    @RequestMapping(value = "/updateWithFile/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> updateWithFile(@PathVariable("id") final Integer id, @RequestParam("attachment") final String attachment, @RequestParam("file") final MultipartFile file, HttpServletRequest request) throws IOException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
    	final String fileServerUrl = env.getProperty("fileserver.path");
    	final String absolutePath = System.getProperty("java.io.tmpdir").replace('\\', File.separatorChar);
        String filename = FileUtil.saveFileToLocal(absolutePath, file);
        //file.transferTo(tempFile);
        final LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new FileSystemResource(absolutePath + filename));
        map.add("attachment", attachment);
        // Header.
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("userinfo", request.getHeader("userinfo"));
        headers.set("ConId", request.getHeader("ConId"));
        final HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        // Call server.
        ResponseEntity<?> result = RestAPIHelper.exchangeService(fileServerUrl + "attachment/updateWithFile/" + id, HttpMethod.POST, headers, requestEntity, null);
        FileUtil.deleteFileFromLocal(absolutePath + filename);
        return result;
    }

    @Transactional
    @RequestMapping(value = "/updateWithFileAndLock/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> updateWithFileAndLock(@PathVariable("id") final Integer id, @RequestParam("attachment") final String attachment, @RequestParam("file") final MultipartFile file, HttpServletRequest request) throws IOException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
    	final String fileServerUrl = env.getProperty("fileserver.path");
    	final String absolutePath = System.getProperty("java.io.tmpdir").replace('\\', File.separatorChar);
        String filename = FileUtil.saveFileToLocal(absolutePath, file);
        //file.transferTo(tempFile);
        final LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new FileSystemResource(absolutePath + filename));
        map.add("attachment", attachment);
        // Header.
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("userinfo", request.getHeader("userinfo"));
        headers.set("ConId", request.getHeader("ConId"));
        final HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        // Call server.
        ResponseEntity<?> result = RestAPIHelper.exchangeService(fileServerUrl + "attachment/updateWithFileAndLock/" + id, HttpMethod.POST, headers, requestEntity, null);
        FileUtil.deleteFileFromLocal(absolutePath + filename);
        return result;
    }

}
