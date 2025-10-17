package com.SCX.ControleDeExame.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
//Classe contendo a lógica do envio de email
@Service
public class EmailService {
    //Criando instâncias utilizadas na classe
    @Autowired
    private JavaMailSender javaMailSender;
    //Definindo remetente do email através da variável de ambiente
    @Value("${spring.mail.username}")
    private String remetente;
    //Metodo para enviar email
    public String sendEmail (String destinatario, String assunto, String mensagem){
        //Try catch
        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(remetente);
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(mensagem, true);
            javaMailSender.send(mimeMessage);
            return "Email enviado";

        } catch (Exception e) {
            return "Erro ao enviar o email " + e;
        }
    }

}