package com.nashtech.MyBikeShop.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.DTO.ReportProdProcess;
import com.nashtech.MyBikeShop.DTO.ReportTopProduct;
import com.nashtech.MyBikeShop.entity.OrderDetailEntity;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.repository.OrderImportRepository;
import com.nashtech.MyBikeShop.repository.OrderRepository;
import com.nashtech.MyBikeShop.services.OrderImportService;
import com.nashtech.MyBikeShop.services.OrderService;
import com.nashtech.MyBikeShop.services.ReportService;
import com.nashtech.MyBikeShop.services.impl.reportMapper.ProductMapper;
import com.nashtech.MyBikeShop.services.impl.reportMapper.ReportProductProcessMapper;
import com.nashtech.MyBikeShop.services.impl.reportMapper.ReportTopProductMapper;

@Service
public class ReportServiceImpl implements ReportService {

	NamedParameterJdbcTemplate template;

	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderImportService importService;

	@Autowired
	OrderImportRepository importRepo;
	
	@Autowired
	OrderRepository orderRepo;

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
	public List<ProductEntity> hotProduct(int size){
		String sqlQuery = "select p.id as id, p.name as name, p.price as price, p.quantity as quantity,"
				+ "p.photo as photo, sum(o2.amount) as totalsold from products p , orderbill o ,orderdetails o2, categories c "
				+ "where o2.orderid =o.id and o2.productid = p.id and p.quantity > 0"
				+ "and p.producttype = c.id and c.status != false and p.status != false "
				+ "group by p.id order by totalsold desc "
				+ "LIMIT "+size;
		return template.query(sqlQuery, new ProductMapper());
	}
	public List<ReportTopProduct> topProductByMonth(String fromMonth, String toMonth) {
		String sqlQuery = "select p.id as id, p.name as name, p.price as price, p.quantity as quantity, \r\n"
				+ "p.photo as photo, sum(o2.amount) as totalsold from products p , orderbill o ,orderdetails o2 \r\n"
				+ "where o2.orderid =o.id and o2.productid = p.id and (to_char(o.timebought , 'MM-YYYY') between '"
				+fromMonth+"' and '"+toMonth+"')\r\n"
				+ "group by p.id order by totalsold desc";
		return template.query(sqlQuery, new ReportTopProductMapper());
	}
	
	public List<ReportProdProcess> productProccess(int cateId) {
		String sqlQuery = "select p.id, p.\"name\", p.quantity , \r\n"
				+ "	coalesce(\r\n"
				+ "		(select SUM(o2.amount) as quantity \r\n"
				+ "		from orderbill o, orderdetails o2\r\n"
				+ "		where p.id = o2.productid and o.id = o2.orderid and o.status = 1\r\n"
				+ "		group by p.id, o.status ), 0) as inprocess,\r\n"
				+ "	coalesce(\r\n"
				+ "		(select SUM(o2.amount) as quantity\r\n"
				+ "		from orderbill o, orderdetails o2\r\n"
				+ "		where p.id = o2.productid and o.id = o2.orderid and o.status = 2\r\n"
				+ "		group by p.id, o.status ), 0) as delivery, \r\n"
				+ "	coalesce(\r\n"
				+ "		(select SUM(o2.amount) as quantity\r\n"
				+ "		from orderbill o , orderdetails o2\r\n"
				+ "		where p.id = o2.productid and o.id = o2.orderid and o.status = 3\r\n"
				+ "		group by p.id, o.status ), 0) as completed,\r\n"
				+ "	coalesce(\r\n"
				+ "		(select SUM(o2.amount) as quantity\r\n"
				+ "		from orderbill o, orderdetails o2\r\n"
				+ "		where p.id = o2.productid and o.id = o2.orderid and o.status = 4\r\n"
				+ "		group by p.id, o.status ), 0) as cancel\r\n"
				+ "from\r\n"
				+ "	products p, categories c \r\n"
				+ "	where p.producttype = c.id and c.status != false and p.status != false and p.producttype = "+cateId+";";
		return template.query(sqlQuery, new ReportProductProcessMapper());
	}
	
	public List<Float> profitMonth(String fromMonth, String toMonth) {
		List<Float> profit = new ArrayList<>();
		if (fromMonth.compareTo(toMonth) == 1) {
			String temp = fromMonth;
			fromMonth = toMonth;
			toMonth = temp;
		}
		String[] arrayFrom = fromMonth.split("-");
		String[] arrayTo = toMonth.split("-");
		int monthFrom = Integer.parseInt(arrayFrom[0]);
		int monthTo = Integer.parseInt(arrayTo[0]);
		int yearFrom = Integer.parseInt(arrayFrom[1]);
		int yearTo = Integer.parseInt(arrayTo[1]);
		while (monthFrom <= monthTo || yearFrom < yearTo) {
			profit.add(orderService.profitByMonth(monthFrom, yearFrom));
			if (monthFrom == monthTo && yearFrom == yearTo) break;
			monthFrom++;
			if (monthFrom > 12) {
				yearFrom++;
				monthFrom=1;
			}
		}
		return profit;
	}
	
	public double profitAvgMonth(int month, int year) {
		List<OrderEntity> orderList = orderRepo.getOrderFromMonth(month, year);
		// Profit
		Map<String, Integer> prodList = new HashMap<>();
		for (OrderEntity order : orderList) {
			for (OrderDetailEntity detail : order.getOrderDetails()) {
				String prodId = detail.getId().getProductId();
				if (prodList.isEmpty() || !prodList.containsKey(prodId)) {	
					prodList.put(prodId, detail.getAmmount());
				} else if (prodList.containsKey(prodId)) {
					int oldAmount = prodList.get(prodId);
					prodList.replace(prodId, detail.getAmmount() + oldAmount);
				}
			}
		}
		double total = 0;
		for (Map.Entry<String, Integer> entry : prodList.entrySet()) {
			double costAVG = importRepo.avgCostImportOfProd(entry.getKey(), month, year);
			double profitAVG = orderRepo.avgPriceSoldOfProd(entry.getKey(), month, year);
			total += (profitAVG - costAVG) * prodList.get(entry.getKey());
		}
		return total;
	}
}
