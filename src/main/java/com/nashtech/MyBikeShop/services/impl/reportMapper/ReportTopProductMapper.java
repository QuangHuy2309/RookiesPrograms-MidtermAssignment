package com.nashtech.MyBikeShop.services.impl.reportMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.nashtech.MyBikeShop.DTO.ReportTopProduct;

public class ReportTopProductMapper implements RowMapper<ReportTopProduct>{
	
	@Override
	public ReportTopProduct  mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReportTopProduct report = new ReportTopProduct();
		report.setId(rs.getString("id"));
		report.setName(rs.getString("name"));
		report.setQuantity(rs.getInt("quantity"));
		report.setPrice(rs.getFloat("price"));
		report.setTotalsold(rs.getLong("totalsold"));
		report.setPhoto(rs.getBytes("photo"));
		return report;
	}
}
