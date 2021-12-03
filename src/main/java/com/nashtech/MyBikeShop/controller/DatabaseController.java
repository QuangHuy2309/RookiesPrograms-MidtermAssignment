package com.nashtech.MyBikeShop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.MyBikeShop.services.DatabaseService;
import com.nashtech.MyBikeShop.payload.response.MessageResponse;
import com.nashtech.MyBikeShop.security.JWT.JwtAuthTokenFilter;
import com.nashtech.MyBikeShop.security.JWT.JwtUtils;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class DatabaseController {
	@Autowired
	DatabaseService dbServices;

	@Autowired
	private JwtUtils jwtUtils;

	private static final Logger logger = Logger.getLogger(DatabaseController.class);

	@GetMapping("/db/backup")
	@PreAuthorize("hasRole('ADMIN')")
	public boolean dbBackUpAction(HttpServletRequest request) {
		String jwt = JwtAuthTokenFilter.parseJwt(request);
		String id = jwtUtils.getUserNameFromJwtToken(jwt);
		dbServices.executeCommand("backup");
		logger.info("Backup data by account ID " + id);
		return true;
	}

	@GetMapping("/db/restore")
	@PreAuthorize("hasRole('ADMIN')")
	public boolean dbRestoreAction(HttpServletRequest request) {
		String jwt = JwtAuthTokenFilter.parseJwt(request);
		String id = jwtUtils.getUserNameFromJwtToken(jwt);
		dbServices.executeCommand("restore");
		logger.info("Restore data by account ID " + id);
		return true;
	}

	@GetMapping("/db/export")
	@PreAuthorize("hasRole('ADMIN')")
	public boolean dbExportAction(HttpServletRequest request) {
		String jwt = JwtAuthTokenFilter.parseJwt(request);
		String id = jwtUtils.getUserNameFromJwtToken(jwt);
		dbServices.exportToCSV();
		logger.info("Export data to CSV by account ID " + id);
		return true;
	}

	@DeleteMapping("/db/{filename}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteBackupFolder(HttpServletRequest request,
			@PathVariable(name = "filename") String filename) {
		String jwt = JwtAuthTokenFilter.parseJwt(request);
		String id = jwtUtils.getUserNameFromJwtToken(jwt);
		if (dbServices.deleteFolderBackup(filename)) {
			logger.info("Delete backup data success by account ID " + id);
			return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
		}
		logger.error("Delete backup data failed by account ID " + id);
		return ResponseEntity.badRequest().body(new MessageResponse("Error: Delete backup folder failed."));
	}

	@GetMapping("/db/getBackupFileName")
	@PreAuthorize("hasRole('ADMIN')")
	public List<String> getBackupFileName() {
		return dbServices.getFolderBackup();
	}

	@PostMapping("/db/import/{filename}")
	@PreAuthorize("hasRole('ADMIN')")
	public boolean dbImportAction(@PathVariable(name = "filename") String filename) {
		dbServices.importToDB(filename);
		return true;
	}
}
