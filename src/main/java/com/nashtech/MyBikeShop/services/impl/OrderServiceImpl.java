package com.nashtech.MyBikeShop.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.text.NumberFormat;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nashtech.MyBikeShop.DTO.OrderDTO;
import com.nashtech.MyBikeShop.DTO.OrderDetailDTO;
import com.nashtech.MyBikeShop.entity.OrderDetailEntity;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.entity.OrderDetailEntity.OrderDetailsKey;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.exception.ObjectPropertiesIllegalException;
import com.nashtech.MyBikeShop.repository.OrderRepository;
import com.nashtech.MyBikeShop.services.OrderDetailService;
import com.nashtech.MyBikeShop.services.OrderService;
import com.nashtech.MyBikeShop.services.PersonService;
import com.nashtech.MyBikeShop.services.ProductService;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	ProductService productService;

	@Autowired
	ModelMapper mapper;

	@Autowired
	PersonService personService;

	@Autowired
	OrderDetailService orderDetailService;

	@Autowired
	private JavaMailSender javaMailSender;

	private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);

	public OrderServiceImpl() {
		super();
	}

	public OrderServiceImpl(OrderRepository orderRepository, ProductService productService, ModelMapper mapper,
			PersonService personService, OrderDetailService orderDetailService, JavaMailSender javaMailSender) {
		super();
		this.orderRepository = orderRepository;
		this.productService = productService;
		this.mapper = mapper;
		this.personService = personService;
		this.orderDetailService = orderDetailService;
		this.javaMailSender = javaMailSender;
	}

	public OrderServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public List<OrderEntity> retrieveOrders() {
		return orderRepository.findAll();

	}

	public Optional<OrderEntity> getOrder(int id) {
		return orderRepository.findById(id);

	}

	public long countTotal() {
		return orderRepository.count();
	}

	public long countTotalOrderByUser(String email) {
		return orderRepository.countByCustomersEmail(email);
	}

	public long countByStatus(int status) {
		return orderRepository.countByStatus(status);
	}

	public List<OrderEntity> getOrdersByCustomerPages(int num, int size, int id) {
		Sort sortable = Sort.by("timebought").descending();
		Pageable pageable = PageRequest.of(num, size, sortable);
		return orderRepository.findByCustomersId(pageable, id);
	}

	public List<OrderEntity> searchOrderByCustomer(String keyword) {

		return orderRepository.searchOrderByCustomer(keyword.toUpperCase());
	}

	public List<OrderEntity> searchOrderByStatusAndCustomer(String keyword, int status) {

		return orderRepository.searchOrderByStatusAndCustomer(keyword.toUpperCase(), status);
	}

	public List<OrderEntity> getOrderPage(int num, int size) {
		Sort sortable = Sort.by("timebought").descending();
		Pageable pageable = PageRequest.of(num, size, sortable);
		return orderRepository.findAll(pageable).stream().collect(Collectors.toList());
	}

	public List<OrderEntity> getOrderPageByStatus(int num, int size, int status) {
		Sort sortable = Sort.by("timebought").descending();
		Pageable pageable = PageRequest.of(num, size, sortable);
		return orderRepository.findByStatus(pageable, status);
	}

	public OrderDTO convertToDTO(OrderEntity order) {
		OrderDTO orderDTO = mapper.map(order, OrderDTO.class);
		double totalCost = 0;
		for (OrderDetailEntity detail : order.getOrderDetails()) {
			totalCost += detail.getUnitPrice() * detail.getAmmount();
		}
		orderDTO.setTotalCost(totalCost);
		orderDTO.setCustomersEmail(order.getCustomers().getEmail());
		orderDTO.setCustomersName(order.getCustomers().getFullname());
		return orderDTO;
	}

	@Transactional
	public OrderEntity createOrder(OrderDTO orderDTO) {
		OrderEntity orderEntity = new OrderEntity(orderDTO);
		orderEntity.setTimebought(LocalDateTime.now());
		PersonEntity person = personService.getPerson(orderDTO.getCustomersEmail());
		orderEntity.setCustomers(person);
		OrderEntity orderSaved = orderRepository.save(orderEntity);
		StringBuilder listProd = new StringBuilder();
		double totalCost = 0;
		for (OrderDetailDTO detailDTO : orderDTO.getOrderDetails()) {
			OrderDetailEntity detail = new OrderDetailEntity(detailDTO);
			OrderDetailsKey id = new OrderDetailsKey(orderSaved.getId(), detailDTO.getProductId());
			detail.setId(id);
			boolean result = orderDetailService.createDetail(detail);
			ProductEntity prod = productService.getProduct(detailDTO.getProductId()).get();
			totalCost += detailDTO.getUnitPrice() * detailDTO.getAmmount();
			listProd.append(
					"<p style=\\\"font-size: 14px; line-height: 200%;\\\"><span style=\\\"font-size: 16px; line-height: 32px;\\\">"
							+ prod.getName() + ". Quantity: " + detailDTO.getAmmount() + ". Unit Price: "
							+ NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(detailDTO.getUnitPrice())
							+ "</span></p>");
			if (!result) {
				logger.error("Account id " + person.getId() + " create order " + orderDTO.getId()
						+ " failed: Create detail order failed");
				throw new ObjectPropertiesIllegalException("Failed in create detail order");
			}
		}
		try {
			sendSimpleMessage(orderDTO.getCustomersEmail(), listProd.toString(), totalCost);
		} catch (MessagingException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		logger.info("Account id " + person.getId() + " create order Id " + orderDTO.getId() + " success");
		return orderRepository.getById(orderSaved.getId());
	}

	@Transactional
	public boolean updateOrderPayment(int id, int userId) {
		OrderEntity order;
		PersonEntity person;
		try {
			order = getOrder(id).get();
		} catch (NoSuchElementException ex) {
			logger.error("Account id " + userId + " updated order payment status with Id " + id
					+ " failed: Not found this account");
			throw new ObjectNotFoundException("Could not find order with Id: " + id);
		}
		try {
			person = personService.getPerson(userId).get();
		} catch (NoSuchElementException ex) {
			logger.error("Account id " + userId + " updated order payment status with Id " + id
					+ " failed: Not found this account");
			throw new ObjectNotFoundException("Not found this account: " + userId);
		}
		if (!order.getCustomers().getEmail().equalsIgnoreCase(person.getEmail())) {
			logger.error("Account id " + person.getId() + " updated order Id " + id
					+ " payment status failed: This account not have permission");
			throw new ObjectPropertiesIllegalException("Error: Unauthorized");
		}
		order.setPayment(true);
		logger.info("Account id " + person.getId() + " updated order payment status with Id " + id + " success");
		return true;

	}

	@Transactional
	public boolean deleteOrder(int id) {
		OrderEntity order = getOrder(id).get();
		PersonEntity person = personService.getPerson(order.getCustomers().getId()).get();
		if (order.getStatus() != 4) { // False = Not delivery yet
			for (OrderDetailEntity detail : orderDetailService.getDetailOrderByOrderId(id)) {
				productService.updateProductQuantity(detail.getProduct().getId(), detail.getAmmount());
			}
		}
		person.getOrders().remove(order);
		orderRepository.delete(order);
		logger.info("Account id " + person.getId() + " create order Id " + id + " success");
		return true;
	}

	public boolean updateOrder(OrderDTO orderDTO) {
		/*
		 * * Sử dụng cách này vì khi thay đổi Order thì số lượng product bán ra sẽ thay
		 * đổi.
		 * 
		 */
		int orderId = orderDTO.getId();
		OrderEntity orderCheck = getOrder(orderId).get(); // Nếu không có Order sẽ gây ra lỗi NoSuchElementException
															// sẽ được catch ở Controller

		for (OrderDetailEntity detail : orderDetailService.getDetailOrderByOrderId(orderId)) {
			boolean result = orderDetailService.deleteDetail(detail);
			if (!result)
				return false;
		}
		for (OrderDetailDTO detailDTO : orderDTO.getOrderDetails()) {
			OrderDetailEntity detail = new OrderDetailEntity(detailDTO);
			orderDetailService.createDetail(detail);
		}
		orderRepository.save(new OrderEntity(orderDTO));
		return true;
	}

	@Transactional
	public boolean updateStatusOrder(int id, int status, String userId) {
		OrderEntity order;
		try {
			order = getOrder(id).get();
		} catch (NoSuchElementException ex) {
			logger.error("Account id " + userId + " update order status with Id " + id
					+ " failed: Could not find Order with id: " + id);
			throw new ObjectNotFoundException(ex.getMessage());
		}
		if (status == 4 && order.getStatus() != 4) {
			for (OrderDetailEntity detail : orderDetailService.getDetailOrderByOrderId(id)) {
				boolean result = false;
				try {
					result = orderDetailService.updateDetailCancel(detail);
				} catch (NoSuchElementException ex) {
					logger.error("Account id " + userId + " update order status with Id " + id
							+ " failed: Product not found with ID " + detail.getId().getProductId());
					throw new ObjectNotFoundException("Product not found with ID " + detail.getId().getProductId());
				}
				if (!result) {
					logger.error("Account id " + order.getCustomers().getId() + " update order status with Id " + id
							+ " failed: Update order details failed");
					return false;
				}
			}
		} else if (status != 4 && order.getStatus() == 4) {
			for (OrderDetailEntity detail : orderDetailService.getDetailOrderByOrderId(id)) {
				boolean result = false;
				try {
					result = orderDetailService.updateDetail(detail);
				} catch (NoSuchElementException ex) {
					logger.error("Account id " + userId + " update order status with Id " + id
							+ " failed: Product not found with ID " + detail.getId().getProductId());
					throw new ObjectNotFoundException(
							"Not found product with ID " + detail.getId().getProductId() + " to update quantity");
				}
				if (!result) {
					logger.error("Account id " + order.getCustomers().getId() + " update order status with Id " + id
							+ " failed: Update order details failed");
					return false;
				}
			}
		}
		if (status == 3)
			order.setPayment(true);
		order.setStatus(status);
		orderRepository.save(order);
		logger.info("Account id " + order.getCustomers().getId() + " update order status with Id " + id + " success");
		return true;
	}

	public List<OrderEntity> getOrderByCustomerEmail(int num, int size, String email) {
		Sort sortable = Sort.by("timebought").descending();
		Pageable pageable = PageRequest.of(num, size, sortable);
		return orderRepository.findByCustomersEmail(pageable, email);
	}

	public float profitByMonth(int month, int year) {
		Float result = orderRepository.profitByMonth(month, year);
		if (result == null)
			result = (float) 0;
		return result;
	}

	public void sendSimpleMessage(String to, String listProd, Double totalCost) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();

		boolean multipart = true;

		MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");

		String htmlMsg = "<!DOCTYPE HTML >\r\n"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\r\n"
				+ "<head>\r\n" + "  <meta charset=\"utf-8\">\r\n"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
				+ "  <meta name=\"x-apple-disable-message-reformatting\">\r\n"
				+ "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n" + "  <title></title>\r\n"
				+ "    <style type=\"text/css\">\r\n"
				+ "      table, td { color: #000000; } @media only screen and (min-width: 620px) {\r\n"
				+ "  .u-row {\r\n" + "    width: 600px !important;\r\n" + "  }\r\n" + "  .u-row .u-col {\r\n"
				+ "    vertical-align: top;\r\n" + "  }\r\n" + "  .u-row .u-col-100 {\r\n"
				+ "    width: 600px !important;\r\n" + "  }\r\n" + "}\r\n" + "@media (max-width: 620px) {\r\n"
				+ "  .u-row-container {\r\n" + "    max-width: 100% !important;\r\n"
				+ "    padding-left: 0px !important;\r\n" + "    padding-right: 0px !important;\r\n" + "  }\r\n"
				+ "  .u-row .u-col {\r\n" + "    min-width: 320px !important;\r\n"
				+ "    max-width: 100% !important;\r\n" + "    display: block !important;\r\n" + "  }\r\n"
				+ "  .u-row {\r\n" + "    width: calc(100% - 40px) !important;\r\n" + "  }\r\n" + "  .u-col {\r\n"
				+ "    width: 100% !important;\r\n" + "  }\r\n" + "  .u-col > div {\r\n" + "    margin: 0 auto;\r\n"
				+ "  }\r\n" + "}\r\n" + "body {\r\n" + "  margin: 0;\r\n" + "  padding: 0;\r\n" + "}\r\n" + "\r\n"
				+ "table,\r\n" + "tr,\r\n" + "td {\r\n" + "  vertical-align: top;\r\n"
				+ "  border-collapse: collapse;\r\n" + "}\r\n" + "p {\r\n" + "  margin: 0;\r\n" + "}\r\n"
				+ ".ie-container table,\r\n" + ".mso-container table {\r\n" + "  table-layout: fixed;\r\n" + "}\r\n"
				+ "* {\r\n" + "  line-height: inherit;\r\n" + "}\r\n" + "a[x-apple-data-detectors='true'] {\r\n"
				+ "  color: inherit !important;\r\n" + "  text-decoration: none !important;\r\n" + "}\r\n"
				+ "</style>\r\n"
				+ "<link href=\"https://fonts.googleapis.com/css?family=Open+Sans:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\">\r\n"
				+ "</head>\r\n"
				+ "<body class=\"clean-body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #ffffff;color: #000000\">\r\n"
				+ "  <table style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #ffffff;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n"
				+ "  <tbody>\r\n" + "  <tr style=\"vertical-align: top\">\r\n"
				+ "    <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\r\n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #017ed0;\">\r\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\r\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n"
				+ "  <div style=\"width: 100% !important;\">\r\n"
				+ "<div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\r\n"
				+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 10px 0px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n"
				+ "  <div style=\"color: #ffffff; line-height: 140%; text-align: center; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 28px; line-height: 39.2px;\"><strong><span style=\"line-height: 39.2px; font-size: 28px;\">Thanks for being with us!</span></strong></span></p>\r\n"
				+ "  </div>\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
				+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 10px 26px;font-family:'Open Sans',sans-serif;\" align=\"left\">   \r\n"
				+ "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\r\n" + "  <tr>\r\n"
				+ "    <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\r\n" + "    </td>\r\n"
				+ "  </tr>\r\n" + "</table>\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n"
				+ "</table>\r\n" + "  </div>\r\n" + "</div>\r\n" + "    </div>\r\n" + "  </div>\r\n" + "</div>\r\n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #f9f9f9;\">\r\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\r\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n"
				+ "  <div style=\"width: 100% !important;\">\r\n"
				+ "<div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\r\n"
				+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:28px 33px 25px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n"
				+ "  <div style=\"color: #444444; line-height: 200%; text-align: center; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"font-size: 14px; line-height: 200%;\"><span style=\"font-size: 22px; line-height: 44px;\">Hi,</span><br /><span style=\"font-size: 16px; line-height: 32px;\">Thank you again for purchase. </span></p>\r\n"
				+ "<p style=\"font-size: 14px; line-height: 200%;\"><span style=\"font-size: 16px; line-height: 32px;\">Your order is:</span></p>\r\n"
				+ listProd + "\r\n\r\nTOTAL: "
				+ NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(totalCost) + "  </div>\r\n"
				+ "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n" + "  </div>\r\n"
				+ "</div>\r\n" + "    </div>\r\n" + "  </div>\r\n" + "</div>\r\n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #272362;\">\r\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\r\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n"
				+ "  <div style=\"width: 100% !important;\">\r\n"
				+ " <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\r\n"
				+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:15px 40px;font-family:'Open Sans',sans-serif;\" align=\"left\">   \r\n"
				+ "  <div style=\"color: #bbbbbb; line-height: 140%; text-align: center; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 12px; line-height: 16.8px;\">RookiesAssignment&nbsp; |&nbsp; Lương Quang Huy</span></p>\r\n"
				+ "  </div>\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table></div>\r\n"
				+ "  </div>\r\n" + "</div>\r\n" + "    </div>\r\n" + "  </div>\r\n" + "</div>\r\n" + "    </td>\r\n"
				+ "  </tr>\r\n" + "  </tbody>\r\n" + "  </table>\r\n" + "</body>\r\n" + "</html>\r\n" + "";

		message.setContent(htmlMsg, "text/html; charset=utf-8");

		helper.setTo(to);

		helper.setSubject("Order Bike Confirmation");

		javaMailSender.send(message);

	}
}
