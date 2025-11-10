package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.domain.auth.Auth;
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
    @Value("${MAIL_FROM:eduardo.darwich23@gmail.com}")
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

    //Metodo para enviar o email de first login (testar)
    public void firtLoginEmail (Auth auth){
        String tokenE = auth.getToken();
        String url = "http://localhost:5173/firstLogin/" + tokenE;

        sendEmail(auth.getUsernameKey(), "Para ativar sua conta acesse esse link", url);
    }

    //Metodo para enviar o email de reset de senha (testar)
    public void resetSenhaEmail (Auth auth){
        String tokenE = auth.getToken();
        String url = "http://localhost:5173/firstLogin/" + tokenE;

        sendEmail(auth.getUsernameKey(), "Para redefinir sua senha acesse esse link", url);

    }

}