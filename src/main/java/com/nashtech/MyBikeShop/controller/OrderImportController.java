package com.nashtech.MyBikeShop.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nashtech.MyBikeShop.DTO.OrderImportDTO;
import com.nashtech.MyBikeShop.DTO.OrderImportDetailDTO;
import com.nashtech.MyBikeShop.entity.OrderImportDetailEntity;
import com.nashtech.MyBikeShop.entity.OrderImportEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.exception.ObjectPropertiesIllegalException;
import com.nashtech.MyBikeShop.payload.response.MessageResponse;
import com.nashtech.MyBikeShop.security.JWT.JwtAuthTokenFilter;
import com.nashtech.MyBikeShop.security.JWT.JwtUtils;
import com.nashtech.MyBikeShop.services.OrderImportDetailService;
import com.nashtech.MyBikeShop.services.OrderImportService;
import com.nashtech.MyBikeShop.services.PersonService;
import com.nashtech.MyBikeShop.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class OrderImportController {

	@Autowired
	OrderImportService orderImportService;

	@Autowired
	OrderImportDetailService importDetailService;

	@Autowired
	ProductService productService;

	@Autowired
	PersonService personService;

	@Autowired
	private JwtUtils jwtUtils;

	private static final Logger logger = Logger.getLogger(OrderImportController.class);

	@GetMapping("/orderImport/totalOrder")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public long getNumberOfOrders() {
		return orderImportService.countTotal();
	}

	@Operation(summary = "Create Order import")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = OrderImportDTO.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@PostMapping("/imports")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public ResponseEntity<?> createOrderImport(HttpServletRequest request,
			@RequestBody OrderImportDTO newOrderImportDto) {
		String jwt = JwtAuthTokenFilter.parseJwt(request);
		String userId = jwtUtils.getUserNameFromJwtToken(jwt);
		PersonEntity personImport = personService.getPerson(newOrderImportDto.getEmployeeEmail());
		if (personImport == null) {
			logger.error("Create import failed: Employee not found with email " + newOrderImportDto.getEmployeeEmail());
			throw new ObjectNotFoundException("Employee not found");
		} else if (!userId.equals(String.valueOf(personImport.getId()))) {
			logger.error("Create import failed: Employee Id " + personImport.getId()
					+ " create import not match Employee Id " + userId + " send request");
			throw new ObjectPropertiesIllegalException(
					"Create import failed: Employee Id create import not match Employee Id send request");
		}
		Set<OrderImportDetailDTO> importDetailDtoList = newOrderImportDto.getOrderImportDetails();
		Set<OrderImportDetailEntity> importDetailEntityList = new HashSet<OrderImportDetailEntity>();
		OrderImportEntity orderImport = orderImportService.convertToEntity(newOrderImportDto);
		for (OrderImportDetailDTO detailDto : importDetailDtoList) {
			ProductEntity product = productService.getProduct(detailDto.getProductId()).orElse(null);
			if (product == null) {
				logger.error("Account id " + personImport.getId() + " create Import failed: Product ID "
						+ detailDto.getProductId() + " not found!");
				throw new ObjectNotFoundException("Product ID " + detailDto.getProductId() + " not found!");
			}
			OrderImportDetailEntity detailEntity = importDetailService.convertToEntity(detailDto);
			detailEntity.setOrder(orderImport);
			importDetailEntityList.add(detailEntity);
		}
		orderImport.setOrderImportDetails(importDetailEntityList);
		OrderImportEntity orderImportCreated = orderImportService.createOrderImport(orderImport, Integer.parseInt(userId));
		logger.info("Account id " + personImport.getId() + " create Import success");
		return new ResponseEntity<OrderImportDTO>(orderImportService.convertToDto(orderImportCreated), HttpStatus.OK);

	}

	@PostMapping("/imports/file")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public ResponseEntity<?> createOrderImportFromExcel(HttpServletRequest request,
			@RequestParam("file") MultipartFile reapExcelDataFile) {
		String jwt = JwtAuthTokenFilter.parseJwt(request);
		String email = jwtUtils.getUserNameFromJwtToken(jwt);
		OrderImportEntity orderImportCreated = orderImportService.createOrderFromXLSS(reapExcelDataFile, email);
		return new ResponseEntity<OrderImportDTO>(orderImportService.convertToDto(orderImportCreated), HttpStatus.OK);

	}

	@Operation(summary = "Get Order import")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = OrderImportDTO.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping("/imports")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public ResponseEntity<?> getOrderImport(@RequestParam(name = "pagenum") int page,
			@RequestParam(name = "size") int size) {
		List<OrderImportEntity> orderImportEntity = orderImportService.getOrderImportPage(page, size);
		List<OrderImportDTO> orderImportDto = orderImportEntity.stream().map(orderImportService::convertToDto)
				.collect(Collectors.toList());
		return new ResponseEntity<List<OrderImportDTO>>(orderImportDto, HttpStatus.OK);
	}

	@GetMapping("/imports/search/ImportByEmployee/{keyword}")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public List<OrderImportDTO> searchOrderByCustomer(@PathVariable(name = "keyword") String keyword) {
		return orderImportService.searchOrderImportByEmployee(keyword).stream().map(orderImportService::convertToDto)
				.collect(Collectors.toList());
	}

	@Operation(summary = "Get Order import detail")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = OrderImportDTO.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping("/imports/{importId}")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public ResponseEntity<?> getOrderImportDetail(@PathVariable int importId) {
		OrderImportEntity orderImport = orderImportService.findOrderImportById(importId);
		if (orderImport == null) {
			throw new ObjectNotFoundException("Order import not found with id: " + importId);
		}
		return new ResponseEntity<OrderImportDTO>(orderImportService.convertToDto(orderImport), HttpStatus.OK);
	}

	@Operation(summary = "Update Order import")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = OrderImportDTO.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@PutMapping("/imports/{importId}")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public ResponseEntity<?> updateOrder(HttpServletRequest request, 
			@RequestBody OrderImportDTO orderImportDto,
			@PathVariable(name = "importId") int importId) {
		String jwt = JwtAuthTokenFilter.parseJwt(request);
		String userId = jwtUtils.getUserNameFromJwtToken(jwt);
		OrderImportEntity orderImport = orderImportService.findOrderImportById(importId);
		if (orderImport == null) {
			throw new ObjectNotFoundException("Order import not found!");
		}

		OrderImportEntity orderImportUpdate = orderImportService.updateOrderImport(orderImportDto, importId, Integer.parseInt(userId));
		if (orderImportUpdate == null) {
			return ResponseEntity.internalServerError().body(new MessageResponse("Update order import fail!"));
		}

		return new ResponseEntity<OrderImportDTO>(orderImportService.convertToDto(orderImportUpdate), HttpStatus.OK);
	}

	@Operation(summary = "Delete Order import by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = OrderImportDTO.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@DeleteMapping("/imports/{importId}")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public ResponseEntity<?> deleteOrderImport(HttpServletRequest request,
			@PathVariable(name = "importId") int importId) {
		String jwt = JwtAuthTokenFilter.parseJwt(request);
		String userId = jwtUtils.getUserNameFromJwtToken(jwt);
		OrderImportEntity orderImport = orderImportService.findOrderImportById(importId);
		if (orderImport == null) {
			logger.error("Account id " + userId + " delete import failed: Order import not found!");
			throw new ObjectNotFoundException("Order import not found!");
		}
		boolean result = orderImportService.deleteOrderImport(importId);
		if (!result) {
			logger.error("Account id " + userId + " delete import failed: Internal Server Error");
			return ResponseEntity.internalServerError().body(new MessageResponse("Delete fail!"));
		}
		logger.info("Account id " + userId + " delete import success");
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
