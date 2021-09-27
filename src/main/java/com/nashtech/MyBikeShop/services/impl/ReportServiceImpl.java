package com.nashtech.MyBikeShop.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.DTO.ReportTopProduct;
import com.nashtech.MyBikeShop.services.OrderImportService;
import com.nashtech.MyBikeShop.services.OrderService;
import com.nashtech.MyBikeShop.services.ReportService;
import com.nashtech.MyBikeShop.services.impl.reportMapper.ReportTopProductMapper;

@Service
public class ReportServiceImpl implements ReportService {

	NamedParameterJdbcTemplate template;

	@Autowired
	OrderService orderService;

	@Autowired
	OrderImportService importService;

	public ReportServiceImpl(NamedParameterJdbcTemplate template) {
		this.template = template;
	}

	public List<Float> profitByYear(int year) {
		List<Float> profit = new ArrayList<>();
		for (int i = 1; i <= 12; i++) {
			profit.add(orderService.profitByMonth(i, year));
		}
		return profit;
	}

	public List<Float> purchaseCostByYear(int year) {
		List<Float> profit = new ArrayList<>();
		for (int i = 1; i <= 12; i++) {
			profit.add(importService.purchaseCostByMonth(i, year));
		}
		return profit;
	}

	public List<ReportTopProduct> topProductByMonth(String fromMonth, String toMonth) {
		String sqlQuery = "select p.id as id, p.name as name, p.price as price, p.quantity as quantity, \r\n"
				+ "p.photo as photo, sum(o2.amount) as totalsold from products p , orderbill o ,orderdetails o2 \r\n"
				+ "where o2.orderid =o.id and o2.productid = p.id and (to_char(o.timebought , 'MM-YYYY') between '"
				+fromMonth+"' and '"+toMonth+"')\r\n"
				+ "group by p.id order by totalsold desc";
		return template.query(sqlQuery, new ReportTopProductMapper());
	}
}
