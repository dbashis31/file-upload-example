package com.file.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;

@RestController
public class UploadController {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "c:\\temp\\";
    private static final String FILENAME = "c:\\test\\filename.txt";

    /**
     * File upload and capture metadata in filename.text 
     * @param file
     * @return
     */
    @RequestMapping(value = "/api/upload", method = RequestMethod.POST)
    public ResponseEntity<String> fileUpload(@RequestParam("file") MultipartFile file
                                   ) {
        
        if (file.isEmpty()) {
        	return new ResponseEntity<String>("File not provided !!! ",HttpStatus.BAD_REQUEST);
        }
        
        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
            write("==========================================");
            write("\ncreationTime: " + attr.creationTime());
            write("\nlastAccessTime: " + attr.lastAccessTime());
            write("\\nlastModifiedTime: " + attr.lastModifiedTime());

            write("\\nisDirectory: " + attr.isDirectory());
            write("\\nisOther: " + attr.isOther());
            write("\\nisRegularFile: " + attr.isRegularFile());
            write("\\nisSymbolicLink: " + attr.isSymbolicLink());
            write("\\nsize: " + attr.size());
            write("==========================================");
            
            return new ResponseEntity<String>("File Uploaded !!!",HttpStatus.OK);

        } catch (FileNotFoundException e) {
        	return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (IOException e) {
        	e.printStackTrace();
        	return new ResponseEntity<String>("File Not found",HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
        	return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        
    }

  
    /**
     * File write
     * @param attribute
     */
	public void write(String  attribute) {

		BufferedWriter bw = null;
		FileWriter fw = null;
        try {
			
            File file = new File(FILENAME);
         	// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
            // true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
            bw.write(attribute);
        } catch (IOException e) {

		} finally {
          	try {
            	if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {
                ex.printStackTrace();
            }
		}

	}

}