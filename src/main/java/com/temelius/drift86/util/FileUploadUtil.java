package com.temelius.drift86.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.uuid.Generators;

/**
 * 
 * Utility class for file upload
 *
 */

public class FileUploadUtil {
	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		// Get specified destination directory
		Path uploadPath = Paths.get(uploadDir);
		
		// If the directory path does not exist, create missing directories
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		// Try to copy multipartfile to new destination
		try (InputStream inputStream = multipartFile.getInputStream()){
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Could not save image file: " + fileName, ioe);
		}
	}
	
	// Generate uuid for newly uploaded image
	public String generateUuidForImage(String imageName) {
		// get extension of the file (does not check binaries, only the filename extension)
		String extension = FilenameUtils.getExtension(imageName);
		
		// generate time based uuid and return string
		UUID uuid = Generators.timeBasedGenerator().generate();
		
		return uuid + "." + extension;
	}
	
}
