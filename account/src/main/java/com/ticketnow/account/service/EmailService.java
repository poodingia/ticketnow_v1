package com.ticketnow.account.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ticketnow.account.dto.CrudEmailDTO;
import com.ticketnow.account.dto.CrudEmailItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final QRCodeWriter qrCodeWriter;


    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine, QRCodeWriter qrCodeWriter) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.qrCodeWriter = qrCodeWriter;
    }

    public void sendPaymentEmail(CrudEmailDTO crudEmailDTO) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(crudEmailDTO.email());
        helper.setSubject("Xác nhận đặt vé - " + crudEmailDTO.paymentRef());

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy");
        LocalDateTime paymentDate = LocalDateTime.parse(crudEmailDTO.paymentDate(), inputFormatter);
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        Context context = new Context();
        context.setVariable("eventName", crudEmailDTO.name());
        context.setVariable("eventDate", crudEmailDTO.eventDate().format(outputFormatter));
        context.setVariable("location", crudEmailDTO.location());
        context.setVariable("paymentRef", crudEmailDTO.paymentRef());
        context.setVariable("paymentDate", paymentDate.format(outputFormatter));
        context.setVariable("paymentMethod", crudEmailDTO.paymentMethod());

        byte[] qrCodeImage = generateQRCodeImage(crudEmailDTO.paymentRef());

        List<Map<String, Object>> items = new ArrayList<>();
        int total = 0;
        for (CrudEmailItemDTO item : crudEmailDTO.crudEmailItemDTOs()) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("description", item.description());
            itemMap.put("quantity", item.quantity());
            itemMap.put("price", currencyFormatter.format(item.price()));
            itemMap.put("totalPrice", currencyFormatter.format(item.price() * item.quantity()));
            items.add(itemMap);
            total += item.quantity();
        }
        context.setVariable("items", items);
        context.setVariable("total", total);

        String htmlContent = templateEngine.process("orderConfirmEmail", context);
        helper.addAttachment("qrcode.png", new ByteArrayResource(qrCodeImage));


        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendEventSaleNotification(String eventName, String username, String[] bccEmails) throws MessagingException {
        if (bccEmails == null || bccEmails.length == 0) {
            log.warn("No email to send to");
            return;
        }
        Context context = new Context();
        context.setVariable("eventName", eventName);
        context.setVariable("username", username);
        String emailContent = templateEngine.process("eventSaleEmail", context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo("tommyphung1212@gmail.com");
        helper.setBcc(bccEmails);
        helper.setSubject("🎟️ Vé sự kiện mở bán vào ngày mai!");
        helper.setText(emailContent, true);

        mailSender.send(message);
    }

    public void sendEventChangeNotification(String part, String[] bccEmails) throws MessagingException {
        if (bccEmails == null || bccEmails.length == 0) {
            return;
        }
        Context context = new Context();
        context.setVariable("eventName", part);
        String emailContent = templateEngine.process("eventChangeEmail", context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo("tommyphung1212@gmail.com");
        helper.setBcc(bccEmails);
        helper.setSubject("🎟️ Sự kiện bạn theo dõi có cập nhật!");
        helper.setText(emailContent, true);

        mailSender.send(message);
    }

    private byte[] generateQRCodeImage(String text) throws Exception {
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }
}

