package com.nashtech.MyBikeShop.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.DTO.PersonDTO;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.exception.ObjectPropertiesIllegalException;
import com.nashtech.MyBikeShop.repository.PersonRepository;
import com.nashtech.MyBikeShop.services.PersonService;

@Service
public class PersonServiceImpl implements PersonService {
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private JavaMailSender javaMailSender;

	private PasswordEncoder encoder;

	HashMap<String, String> otpList = new HashMap<String, String>();

	public PersonServiceImpl() {
		super();
	}

	public PersonServiceImpl(PersonRepository personRepository, PasswordEncoder encoder) {
		this.personRepository = personRepository;
		this.encoder = encoder;
	}

	public List<PersonEntity> retrievePersons() {
		return personRepository.findAll();
	}

	public Optional<PersonEntity> getPerson(int id) {
		return personRepository.findById(id);
	}

	public List<PersonEntity> searchPerson(String keyword, String role) {
		return personRepository.searchPerson(keyword.toUpperCase(), role);
	}

	public List<PersonEntity> searchPersonRoleNot(String keyword, String role) {
		return personRepository.searchPersonRoleNot(keyword.toUpperCase(), role);
	}

	public PersonEntity getPerson(String email) {
		PersonEntity person = personRepository.findByEmail(email);
		if (!person.isStatus())
			throw new ObjectNotFoundException("This account had been disable");
		else
			return person;
	}

	public List<PersonEntity> getPersonsPage(int num, int size, String role) {
		Sort sortable = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(num, size, sortable);
		if (role.equals("EMPLOYEE"))
			return personRepository.findByRoleNotAndStatusNot(pageable, "USER", false);
		else
			return personRepository.findByRoleAndStatusNot(pageable, role.toUpperCase(), false);
	}

	public PersonEntity createPerson(PersonDTO personDTO) {
		Optional<PersonEntity> person = personRepository.findById(personDTO.getId());
		if (person.isPresent()) {
			throw new ObjectAlreadyExistException("There is a person with email " + person.get().getEmail());
		} else {
			PersonEntity personEntity = new PersonEntity(personDTO);
			return personRepository.save(personEntity);
		}
	}

	public boolean deletePerson(PersonEntity person) {
		person.setStatus(false);
		personRepository.save(person);
		return true;

	}

	public boolean updatePerson(PersonDTO personDTO) {
		PersonEntity personEntity = getPerson(personDTO.getId()).get();
		boolean checkEmailChange = personEntity.getEmail().equals(personDTO.getEmail());
		PersonEntity person = new PersonEntity(personDTO);

		person.setPassword(personEntity.getPassword());
//		person.setRole(personEntity.getRole());

		if (checkEmailChange) { // Không đổi email
			personRepository.save(person);
		} else { // Email được đổi, kiểm tra trùng
			List<PersonEntity> list = personRepository.findByEmailIgnoreCase(personDTO.getEmail());
			if (list.isEmpty()) {
				personRepository.save(person);
			} else {
				throw new ObjectAlreadyExistException("There is a user using this email");
			}
		}
		return true;
	}

	public boolean checkExistEmailUpdate(String email, int id) {
		List<PersonEntity> list = personRepository.findByEmailIgnoreCase(email);
		if (list.isEmpty()) {
			return true;
		} else if ((list.size() > 1) || ((list.size() == 1) && (list.get(0).getId() != id)))
			return false;
		else
			return true;
	}

	public int getTotalByRole(String role) {
		if (role.equals("EMPLOYEE"))
			return personRepository.countByRoleNotAndStatusNot("USER", false);
		return personRepository.countByRoleAndStatusNot(role, false);
	}

	public PersonEntity changePassword(String email, String oldPassword, String newPassword) {
		PersonEntity person = getPerson(email);
		String newPasswordEncoded = encoder.encode(newPassword);
		boolean isMatched = encoder.matches(oldPassword, person.getPassword());
		if (!isMatched) {
			throw new ObjectPropertiesIllegalException("Error: Old password is not correct.");
		}
		person.setPassword(newPasswordEncoded);
		return personRepository.save(person);
	}

	public String createOTP() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 5;
		Random random = new Random();

		return random.ints(leftLimit, rightLimit + 1).filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
	}

	public boolean checkOTP(String email, String otp) {
		if (otpList.containsKey(email)) {
			if (otpList.get(email).equals(otp)) {
				otpList.remove(email);
				return true;
			}
			return false;
		}
		return false;
	}

	public PersonEntity forgotPassword(String email, String newPassword) {
		PersonEntity person = getPerson(email);
		String newPasswordEncoded = encoder.encode(newPassword);
		person.setPassword(newPasswordEncoded);
		return personRepository.save(person);
	}

	public void sendOTPEmail(String email) throws MessagingException {
		String otp = createOTP();
		if (otpList.isEmpty() || !otpList.containsKey(email)) {
			otpList.put(email, otp);
		} else if (otpList.containsKey(email)) {
			otpList.replace(email, otp);
		}
		MimeMessage message = javaMailSender.createMimeMessage();

		boolean multipart = true;

		MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");

		String htmlMsg = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\r\n"
				+ "<head>\r\n" + "<!--[if gte mso 9]>\r\n" + "<xml>\r\n" + "  <o:OfficeDocumentSettings>\r\n"
				+ "    <o:AllowPNG/>\r\n" + "    <o:PixelsPerInch>96</o:PixelsPerInch>\r\n"
				+ "  </o:OfficeDocumentSettings>\r\n" + "</xml>\r\n" + "<![endif]-->\r\n"
				+ "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
				+ "  <meta name=\"x-apple-disable-message-reformatting\">\r\n"
				+ "  <!--[if !mso]><!--><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><!--<![endif]-->\r\n"
				+ "  <title></title>\r\n" + "  \r\n" + "    <style type=\"text/css\">\r\n"
				+ "      table, td { color: #000000; } @media only screen and (min-width: 620px) {\r\n"
				+ "  .u-row {\r\n" + "    width: 600px !important;\r\n" + "  }\r\n" + "  .u-row .u-col {\r\n"
				+ "    vertical-align: top;\r\n" + "  }\r\n" + "\r\n" + "  .u-row .u-col-100 {\r\n"
				+ "    width: 600px !important;\r\n" + "  }\r\n" + "\r\n" + "}\r\n" + "\r\n"
				+ "@media (max-width: 620px) {\r\n" + "  .u-row-container {\r\n" + "    max-width: 100% !important;\r\n"
				+ "    padding-left: 0px !important;\r\n" + "    padding-right: 0px !important;\r\n" + "  }\r\n"
				+ "  .u-row .u-col {\r\n" + "    min-width: 320px !important;\r\n"
				+ "    max-width: 100% !important;\r\n" + "    display: block !important;\r\n" + "  }\r\n"
				+ "  .u-row {\r\n" + "    width: calc(100% - 40px) !important;\r\n" + "  }\r\n" + "  .u-col {\r\n"
				+ "    width: 100% !important;\r\n" + "  }\r\n" + "  .u-col > div {\r\n" + "    margin: 0 auto;\r\n"
				+ "  }\r\n" + "}\r\n" + "body {\r\n" + "  margin: 0;\r\n" + "  padding: 0;\r\n" + "}\r\n" + "\r\n"
				+ "table,\r\n" + "tr,\r\n" + "td {\r\n" + "  vertical-align: top;\r\n"
				+ "  border-collapse: collapse;\r\n" + "}\r\n" + "\r\n" + "p {\r\n" + "  margin: 0;\r\n" + "}\r\n"
				+ "\r\n" + ".ie-container table,\r\n" + ".mso-container table {\r\n" + "  table-layout: fixed;\r\n"
				+ "}\r\n" + "\r\n" + "* {\r\n" + "  line-height: inherit;\r\n" + "}\r\n" + "\r\n"
				+ "a[x-apple-data-detectors='true'] {\r\n" + "  color: inherit !important;\r\n"
				+ "  text-decoration: none !important;\r\n" + "}\r\n" + "\r\n" + "</style>\r\n" + "  \r\n" + "  \r\n"
				+ "\r\n"
				+ "<!--[if !mso]><!--><link href=\"https://fonts.googleapis.com/css?family=Open+Sans:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\"><!--<![endif]-->\r\n"
				+ "\r\n" + "</head>\r\n" + "\r\n"
				+ "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #ffffff;color: #000000\">\r\n"
				+ "  <!--[if IE]><div class=\"ie-container\"><![endif]-->\r\n"
				+ "  <!--[if mso]><div class=\"mso-container\"><![endif]-->\r\n"
				+ "  <table style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #ffffff;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n"
				+ "  <tbody>\r\n" + "  <tr style=\"vertical-align: top\">\r\n"
				+ "    <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\r\n"
				+ "    <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #ffffff;\"><![endif]-->\r\n"
				+ "    \r\n" + "\r\n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #017ed0;\">\r\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\r\n"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #017ed0;\"><![endif]-->\r\n"
				+ "      \r\n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n"
				+ "  <div style=\"width: 100% !important;\">\r\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\r\n"
				+ "  \r\n"
				+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 10px 0px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <div style=\"color: #ffffff; line-height: 140%; text-align: center; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 28px; line-height: 39.2px;\"><strong><span style=\"line-height: 39.2px; font-size: 28px;\">Thanks for being with us!</span></strong></span></p>\r\n"
				+ "  </div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
				+ "\r\n" + "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + "  </div>\r\n" + "</div>\r\n"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->\r\n"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + "    </div>\r\n"
				+ "  </div>\r\n" + "</div>\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #f9f9f9;\">\r\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\r\n"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #f9f9f9;\"><![endif]-->\r\n"
				+ "      \r\n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n"
				+ "  <div style=\"width: 100% !important;\">\r\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\r\n"
				+ "  \r\n"
				+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:28px 33px 25px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <div style=\"color: #444444; line-height: 200%; text-align: center; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"line-height: 200%; text-align: left; font-size: 14px;\"><span style=\"font-size: 22px; line-height: 44px;\">Hi,</span><br /><span style=\"font-size: 16px; line-height: 32px;\">Your one-time verification code is: "
				+ otp + "</span></p>\r\n" + "  </div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n"
				+ "  </tbody>\r\n" + "</table>\r\n" + "\r\n" + "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n"
				+ "  </div>\r\n" + "</div>\r\n" + "<!--[if (mso)|(IE)]></td><![endif]-->\r\n"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + "    </div>\r\n"
				+ "  </div>\r\n" + "</div>\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #f9f9f9;\">\r\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\r\n"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #f9f9f9;\"><![endif]-->\r\n"
				+ "      \r\n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n"
				+ "  <div style=\"width: 100% !important;\">\r\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\r\n"
				+ "  \r\n"
				+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:18px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"84%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #d8d0d0;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n"
				+ "    <tbody>\r\n" + "      <tr style=\"vertical-align: top\">\r\n"
				+ "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n"
				+ "          <span>&#160;</span>\r\n" + "        </td>\r\n" + "      </tr>\r\n" + "    </tbody>\r\n"
				+ "  </table>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
				+ "\r\n" + "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + "  </div>\r\n" + "</div>\r\n"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->\r\n"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + "    </div>\r\n"
				+ "  </div>\r\n" + "</div>\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n"
				+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #272362;\">\r\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\r\n"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #272362;\"><![endif]-->\r\n"
				+ "      \r\n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n"
				+ "  <div style=\"width: 100% !important;\">\r\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\r\n"
				+ "  \r\n"
				+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
				+ "  <tbody>\r\n" + "    <tr>\r\n"
				+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:15px 40px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n"
				+ "        \r\n"
				+ "  <div style=\"color: #bbbbbb; line-height: 140%; text-align: center; word-wrap: break-word;\">\r\n"
				+ "    <p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 12px; line-height: 16.8px;\">RookiesAssignment &nbsp; |&nbsp; Lương Quang Huy</span></p>\r\n"
				+ "  </div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
				+ "\r\n" + "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + "  </div>\r\n" + "</div>\r\n"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->\r\n"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + "    </div>\r\n"
				+ "  </div>\r\n" + "</div>\r\n" + "\r\n" + "\r\n"
				+ "    <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\r\n" + "    </td>\r\n" + "  </tr>\r\n"
				+ "  </tbody>\r\n" + "  </table>\r\n" + "  <!--[if mso]></div><![endif]-->\r\n"
				+ "  <!--[if IE]></div><![endif]-->\r\n" + "</body>\r\n" + "\r\n" + "</html>\r\n" + "";
		message.setContent(htmlMsg, "text/html; charset=utf-8");

		helper.setTo(email);

		helper.setSubject("BikeShop verification code");

		javaMailSender.send(message);
	}
}
