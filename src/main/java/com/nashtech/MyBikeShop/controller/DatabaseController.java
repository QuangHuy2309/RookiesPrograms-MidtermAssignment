package com.nashtech.MyBikeShop.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.MyBikeShop.services.DatabaseService;
import com.nashtech.MyBikeShop.payload.response.MessageResponse;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class DatabaseController {
	@Autowired
	DatabaseService dbServices;

	@GetMapping("/db/backup")
	@PreAuthorize("hasRole('ADMIN')")
	public boolean dbBackUpAction() {
		dbServices.executeCommand("backup");
		return true;
	}

	@GetMapping("/db/restore")
	@PreAuthorize("hasRole('ADMIN')")
	public boolean dbRestoreAction() {
		dbServices.executeCommand("restore");
		return true;
	}

	@GetMapping("/db/export")
	@PreAuthorize("hasRole('ADMIN')")
	public boolean dbExportAction() {
		dbServices.exportToCSV();
		return true;
	}

	@DeleteMapping("/db/{filename}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteBackupFolder(@PathVariable(name = "filename") String filename) {
		if (dbServices.deleteFolderBackup(filename))
		{ 
			return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
		}
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
