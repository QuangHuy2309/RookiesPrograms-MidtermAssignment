package com.nashtech.MyBikeShop.services;

import java.io.File;
import java.util.List;

import com.nashtech.MyBikeShop.entity.PersonEntity;

public interface DatabaseService {
	public List<String> getPgComands(File backupFilePath, String backupFileName, String type);

	public void executeCommand(String type);

	public boolean exportToCSV();

	public String[] convertToCSV(PersonEntity person);

	public boolean importToDB(String filename);
	
	public List<String> getFolderBackup();
	
	public boolean deleteFolderBackup(String filename);
}
