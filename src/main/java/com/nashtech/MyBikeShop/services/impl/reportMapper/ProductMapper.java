package com.nashtech.MyBikeShop.services.impl.reportMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.nashtech.MyBikeShop.DTO.ReportProdProcess;
import com.nashtech.MyBikeShop.DTO.ReportTopProduct;
import com.nashtech.MyBikeShop.entity.ProductEntity;

public class ProductMapper implements RowMapper<ProductEntity>{
	
	@Override
	public ProductEntity  mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductEntity prod = new ProductEntity();
		prod.setId(rs.getString("id"));
		prod.setName(rs.getString("name"));
		prod.setPrice(rs.getFloat("price"));
		prod.setQuantity(rs.getInt("quantity"));
		prod.setPhoto(rs.getBytes("photo"));
		return prod;
	}
}
