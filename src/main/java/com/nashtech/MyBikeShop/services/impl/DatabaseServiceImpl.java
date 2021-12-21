package com.nashtech.MyBikeShop.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.repository.PersonRepository;
import com.nashtech.MyBikeShop.services.DatabaseService;
import com.nashtech.MyBikeShop.services.PersonService;

@Service
public class DatabaseServiceImpl implements DatabaseService {

	@Autowired
	private PersonRepository personRepo;

	@PersistenceContext
	private EntityManager em;

	private String databaseName = "rookies";

	@Value("${spring.datasource.password}")
	private String dbPassword;

	public void executeCommand(String type) {

		File backupFilePath = new File(System.getProperty("user.home") + File.separator + "backup_" + databaseName);

		if (!backupFilePath.exists()) {
			File dir = backupFilePath;
			dir.mkdirs();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String backupFileName = "backup_" + databaseName + "_" + sdf.format(new Date()) + ".sql";

		List<String> commands = getPgComands(backupFilePath, backupFileName, type);
		if (!commands.isEmpty()) {
			try {
				ProcessBuilder pb = new ProcessBuilder(commands);
				pb.environment().put("PGPASSWORD", dbPassword);

				Process process = pb.start();

				try (BufferedReader buf = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
					String line = buf.readLine();
					while (line != null) {
						System.err.println(line);
						line = buf.readLine();
					}
				}

				process.waitFor();
				process.destroy();

				System.out.println("===> Success on " + type + " process.");
			} catch (IOException | InterruptedException ex) {
				System.out.println("Exception: " + ex);
			}
		} else {
			System.out.println("Error: Invalid params.");
		}
	}

	public List<String> getPgComands(File backupFilePath, String backupFileName, String type) {
		ArrayList<String> commands = new ArrayList<>();
		String path = "D:\\Programs\\PostpreSQL\\bin\\";
		switch (type) {
		case "backup":
			commands.add(path + "pg_dump");
			commands.add("-h"); // database server host
			commands.add("localhost");
			commands.add("-p"); // database server port number
			commands.add("5432");
			commands.add("-U"); // connect as specified database user
			commands.add("postgres");
			commands.add("-F"); // output file format (custom, directory, tar, plain text (default))
			commands.add("c");
			commands.add("-b"); // include large objects in dump
			commands.add("-v"); // verbose mode
			commands.add("-f"); // output file or directory name
			commands.add(backupFilePath.getAbsolutePath() + File.separator + backupFileName);
			commands.add("-d"); // database name
			commands.add(databaseName);
			System.out.println(backupFilePath.getAbsolutePath() + File.separator + backupFileName);
			break;
		case "restore":
			commands.add(path + "pg_restore");
			commands.add("-h");
			commands.add("localhost");
			commands.add("-p");
			commands.add("5432");
			commands.add("-U");
			commands.add("postgres");
			commands.add("-d");
			commands.add(databaseName);
			commands.add("-v");
			commands.add(backupFilePath.getAbsolutePath() + File.separator + backupFileName);
			break;
		default:
			return Collections.EMPTY_LIST;
		}
		return commands;
	}

	public String[] convertToCSV(PersonEntity person) {
		return new String[] { person.getEmail() + "," + person.getPassword() + "," + person.getFullname() + ","
				+ person.getDob() + "," + person.isGender() + "," + person.getAddress() + "," + person.getPhonenumber()
				+ "," + person.getRole() + "," + person.isStatus() };
	}

	@Transactional
	public boolean exportToCSV() {
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
		StringBuilder backUpPath = new StringBuilder("D:/WORK/backupDb/" + date);
		File backupFilePath = new File(backUpPath.toString());
		Boolean check = true;
		if (!backupFilePath.exists()) {
			File dir = backupFilePath;
			dir.mkdirs();
		}
		ArrayList<String> typeList = new ArrayList<>(Arrays.asList("categories", "persons", "products", "orderbill",
				"orderdetails", "orderimport", "orderimportdetails", "reviewdetails"));
		for (String type : typeList) {
			StringBuilder pathForEach = new StringBuilder(backUpPath.toString() + ("/" + type + ".csv"));
			switch (type) {
			case "categories": {
				StringBuilder sql = new StringBuilder(
						"\\copy (select name,description,status from categories order by id)" + " to '"
								+ pathForEach.toString() + "' DELIMITER ',' CSV HEADER;");
				int row = em.createNativeQuery(sql.toString()).executeUpdate();
				if (row < 1)
					check = false;
				break;
			}
			case "persons": {
				StringBuilder sql = new StringBuilder(
						"\\copy (select email,password,fullname,dob,gender,address,phonenumber,role,status from persons order by id)"
								+ " to '" + pathForEach.toString() + "' DELIMITER ',' CSV HEADER;");
				int row = em.createNativeQuery(sql.toString()).executeUpdate();
				if (row < 1)
					check = false;
				break;
			}
			case "products": {
				StringBuilder sql = new StringBuilder(
						"\\copy products(id,name,price,quantity,producttype,description,brand,photo,createdate,updatedate,updateby,status)"
								+ " to '" + pathForEach.toString() + "' DELIMITER ',' CSV HEADER;");
				int row = em.createNativeQuery(sql.toString()).executeUpdate();
				if (row < 1)
					check = false;
				break;
			}
			case "orderbill": {
				StringBuilder sql = new StringBuilder(
						"\\copy (select id, customerid,timebought,address,status,ispay,note,approveby from orderbill order by timebought)"
								+ " to '" + pathForEach.toString() + "' DELIMITER ',' CSV HEADER;");
				int row = em.createNativeQuery(sql.toString()).executeUpdate();
				if (row < 1)
					check = false;
				break;
			}
			case "orderdetails": {
				StringBuilder sql = new StringBuilder("\\copy orderdetails(orderid,productid,amount,unitprice)"
						+ " to '" + pathForEach.toString() + "' DELIMITER ',' CSV HEADER;");
				int row = em.createNativeQuery(sql.toString()).executeUpdate();
				if (row < 1)
					check = false;
				break;
			}
			case "orderimport": {
				StringBuilder sql = new StringBuilder(
						"\\copy (select id, employeeid,timeimport,status from orderimport order by id)" + " to '"
								+ pathForEach.toString() + "' DELIMITER ',' CSV HEADER;");
				int row = em.createNativeQuery(sql.toString()).executeUpdate();
				if (row < 1)
					check = false;
				break;
			}
			case "orderimportdetails": {
				StringBuilder sql = new StringBuilder("\\copy orderimportdetails(orderimportid,productid,amount,price)"
						+ " to '" + pathForEach.toString() + "' DELIMITER ',' CSV HEADER;");
				int row = em.createNativeQuery(sql.toString()).executeUpdate();
				if (row < 1)
					check = false;
				break;
			}
			case "reviewdetails": {
				StringBuilder sql = new StringBuilder(
						"\\copy reviewdetails(productid,customerid,rate_num,rate_text,datereview)" + " to '"
								+ pathForEach.toString() + "' DELIMITER ',' CSV HEADER;");
				int row = em.createNativeQuery(sql.toString()).executeUpdate();
				if (row < 1)
					check = false;
				break;
			}
			}
		}
		;
//		String path = "'D:/WORK/backupDb/persons_" + date + ".csv'";
//		String sql = "\\copy persons(email,password,fullname,dob,gender,address,phonenumber,role,status)" + " to "
//				+ path + " DELIMITER ',' CSV HEADER;";
//		System.out.println(sql);
//		int row = em.createNativeQuery(sql).executeUpdate();
		return check;
	}

	@Transactional
	public boolean importToDB(String filename) {
		StringBuilder backUpPath = new StringBuilder("D:/WORK/backupDb/" + filename);
		boolean check = true;
		File backupFilePath = new File(backUpPath.toString());
		if (!backupFilePath.exists()) {
			throw new ObjectNotFoundException("Cannot find the folder with name: " + filename);
		}
		StringBuilder sql_truncate = new StringBuilder(
				"TRUNCATE TABLE orderbill, orderdetails, persons, orderimport, orderimportdetails, reviewdetails, products, categories \r\n"
						+ "RESTART IDENTITY;");
		int row_truncate = em.createNativeQuery(sql_truncate.toString()).executeUpdate();

		ArrayList<String> typeList = new ArrayList<>(Arrays.asList("categories", "persons", "products", "orderbill",
				"orderdetails", "orderimport", "orderimportdetails", "reviewdetails"));
		for (String type : typeList) {
			StringBuilder pathForEach = new StringBuilder(backUpPath.toString() + ("/" + type + ".csv"));
			switch (type) {
			case "categories": {
				StringBuilder sql = new StringBuilder("\\copy categories(name,description,status)" + " from '"
						+ pathForEach.toString() + "' DELIMITER ',' CSV HEADER;");
				int row = em.createNativeQuery(sql.toString()).executeUpdate();
				if (row < 1)
					check = false;
				break;
			}
			case "persons": {
				StringBuilder sql = new StringBuilder(
						"\\copy persons(email,password,fullname,dob,gender,address,phonenumber,role,status)" + " from '"
								+ pathForEach.toString() + "' DELIMITER ',' CSV HEADER;");
				int row = em.createNativeQuery(sql.toString()).executeUpdate();
				if (row < 1)
					check = false;
				break;
			}
			case "products": {
				StringBuilder sql = new StringBuilder(
						"\\copy products(id,name,price,quantity,producttype,description,brand,photo,createdate,updatedate,updateby,status)"
								+ " from '" + pathForEach.toString() + "' DELIMITER ',' CSV HEADER;");
				int row = em.createNativeQuery(sql.toString()).executeUpdate();
				if (row < 1)
					check = false;
				break;
			}
			case "orderbill": {
				StringBuilder sql = new StringBuilder(
						"\\copy orderbill(id, customerid, timebought, address, status, ispay,note,approveby)"
								+ " from '" + pathForEach.toString() + "' DELIMITER ',' CSV HEADER;");
				int row = em.createNativeQuery(sql.toString()).executeUpdate();
				if (row < 1)
					check = false;
				break;
			}
			case "orderdetails": {
				StringBuilder sql = new StringBuilder("\\copy orderdetails(orderid,productid,amount,unitprice)"
						+ " from '" + pathForEach.toString() + "' DELIMITER ',' CSV HEADER;");
				int row = em.createNativeQuery(sql.toString()).executeUpdate();
				if (row < 1)
					check = false;
				break;
			}
			case "orderimport": {
				StringBuilder sql = new StringBuilder("\\copy orderimport(id, employeeid, timeimport, status)"
						+ " from '" + pathForEach.toString() + "' DELIMITER ',' CSV HEADER;");
				int row = em.createNativeQuery(sql.toString()).executeUpdate();
				if (row < 1)
					check = false;
				break;
			}
			case "orderimportdetails": {
				StringBuilder sql = new StringBuilder("\\copy orderimportdetails(orderimportid,productid,amount,price)"
						+ " from '" + pathForEach.toString() + "' DELIMITER ',' CSV HEADER;");
				int row = em.createNativeQuery(sql.toString()).executeUpdate();
				if (row < 1)
					check = false;
				break;
			}
			case "reviewdetails": {
				StringBuilder sql = new StringBuilder(
						"\\copy reviewdetails(productid,customerid,rate_num,rate_text,datereview)" + " from '"
								+ pathForEach.toString() + "' DELIMITER ',' CSV HEADER;");
				int row = em.createNativeQuery(sql.toString()).executeUpdate();
				if (row < 1)
					check = false;
				break;
			}
			}
		}
		;
		return check;
	}

	public List<String> getFolderBackup() {
		ArrayList<String> listFile = new ArrayList<>();
		File dir = new File("D:/WORK/backupDb/");
		File[] children = dir.listFiles();
		for (File file : children) {
//            System.out.println(file.getName());
			listFile.add(file.getName());
		}
		return listFile;
	}

	public boolean deleteFolderBackup(String filename) {
		try {
			FileUtils.deleteDirectory(new File("D:/WORK/backupDb/" + filename));
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
