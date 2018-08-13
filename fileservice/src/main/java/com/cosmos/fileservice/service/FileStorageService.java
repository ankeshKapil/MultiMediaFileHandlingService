package com.cosmos.fileservice.service;

import com.cosmos.fileservice.domain.Command;
import com.cosmos.fileservice.exception.FileStorageException;
import com.cosmos.fileservice.exception.MyFileNotFoundException;
import com.cosmos.fileservice.property.FileStorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileStorageService {

	private static Logger logger = LoggerFactory.getLogger(FileStorageService.class);

	public final Path fileStorageLocation;

	@Autowired
	public FileStorageService(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		logger.info("file location :- " + fileStorageLocation);
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
	}

	public String storeFile(MultipartFile file) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	public Resource loadFileAsResource(String fileName) {

		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			logger.debug(resource.getFilename());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + fileName);

			}

		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + fileName, ex);

		}

	}

	public String executeCommand(String cmd, String fileName) throws IOException {

		// File uploadedFile=Paths.get(this.fileStorageLocation.toString(),
		// fileName).toFile();
		// uploadedFile.se

		Path path = Paths.get(this.fileStorageLocation.toString(), fileName);
		// BasicFileAttributes fileAttrView = Files.readAttributes(path,
		// BasicFileAttributes.class);

		long currentTime = System.currentTimeMillis();
		FileTime ft = FileTime.fromMillis(currentTime);
		Files.setLastModifiedTime(path, ft);

		List<String> command = new ArrayList<>();
		command.add("cmd.exe");
		command.add("/c");
		command.add("cd");
		command.add(this.fileStorageLocation.toString());
		command.add("&&");


		command.addAll(Arrays.asList(cmd.split(" ")));
		command.add("&&");
		command.add("dir");

		ProcessBuilder builder = new ProcessBuilder(command);
		logger.debug(builder.command().toString());
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) {
				break;
			}
			System.out.println(line);
		}

		File dir = new File(this.fileStorageLocation.toString());

		File[] directoryListing = dir.listFiles();
		String modifiedFilename = fileName;

		if (directoryListing != null) {
			for (File child : directoryListing) {
				BasicFileAttributes fileAttrView = Files.readAttributes(child.toPath(), BasicFileAttributes.class);
				if (fileAttrView.lastModifiedTime().toMillis() == currentTime) {
					modifiedFilename = child.getName();
				}
			}
		}
		return modifiedFilename;
	}

	public static void main(String[] args) throws Exception {

		Path path = Paths.get("G://uploads", "kapil.mp3");
		// BasicFileAttributes fileAttrView = Files.readAttributes(path,
		// BasicFileAttributes.class);

		long currentTime = System.currentTimeMillis();
		FileTime ft = FileTime.fromMillis(currentTime);
		Files.setLastModifiedTime(path, ft);

		List<String> command = new ArrayList<>();
		command.add("cmd.exe");
		command.add("/c");
		command.add("cd");
		command.add("G://uploads");
		command.add("&&");

//		command.add("ren");
//		command.add("back.jpg");
//		command.add("ankesh.jpg");

		command.addAll(Arrays.asList("ren","kapil.mp3","ankesh.mp3"));
		command.add("&&");
		command.add("dir");

		ProcessBuilder builder = new ProcessBuilder(command);
		System.out.println(builder.command());
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) {
				break;
			}
			System.out.println(line);
		}

		File dir = new File("G://uploads");

		File[] directoryListing = dir.listFiles();
		String modifiedFilename = null;

		if (directoryListing != null) {
			for (File child : directoryListing) {
				BasicFileAttributes fileAttrView = Files.readAttributes(child.toPath(), BasicFileAttributes.class);
				if (fileAttrView.lastModifiedTime().toMillis() == currentTime) {
					modifiedFilename = child.getName();
				}
			}
		}
		
		System.out.println(modifiedFilename);
	}

}
