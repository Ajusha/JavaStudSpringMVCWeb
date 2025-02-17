package com.javastud.springmvcweb.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadDownloadController {
	
	public static final String FILE_PATH = "D:\\upload\\";

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String uploadGET() {
		return "fileUpload";
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadPOST( @RequestParam("file") MultipartFile file , Model model) throws IOException {
		
		if(!file.isEmpty()){
			
			//Save file in drive
			FileOutputStream out = new FileOutputStream(FILE_PATH + file.getOriginalFilename());
			out.write(file.getBytes());
			out.close();
			
			model.addAttribute("successMsg", "File Upload Success: " + file.getOriginalFilename());
			model.addAttribute("fileName", URLEncoder.encode(file.getOriginalFilename(), "UTF-8") );
			
		}
		
		return "fileUpload";
	}
	
	@RequestMapping(value="/download", method = RequestMethod.GET)
	public void download(  @RequestParam("file") String fileName, HttpServletResponse response ) throws IOException{
		fileName = URLDecoder.decode(fileName, "UTF-8");
		String ext = FilenameUtils.getExtension(fileName);
		String name = FilenameUtils.getBaseName(fileName);
		
		if(ext.equals("png") || ext.equals("jpg") || ext.equals("jpeg")){
			response.setContentType("image/"+ext);
		}else if(ext.equals("pdf")){
			response.setContentType("application/"+ext);
		}
		response.setHeader("Content-Disposition", "attachment;filename="+fileName);
		
		PrintWriter out = response.getWriter();
		FileInputStream fin = new FileInputStream(FILE_PATH + fileName);
		
		int i = 0;
		while( (i = fin.read()) != -1 ){
			out.write(i);
		}
		fin.close();
		out.close();
	}
	
	

}
