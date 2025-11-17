package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.domain.auth.Auth;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
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

    @Value("${api.key.mailGrid}")
    private String sendGridKey;

    //Definindo remetente do email através da variável de ambiente

    //Metodo para enviar email
    public void sendEmail (String destinatario, String assunto, String mensagem){
        Email from = new Email("eduardo.darwich23@gmail.com");
        Email to = new Email(destinatario);
        Content content = new Content("text/plain",  mensagem);
        Mail mail = new Mail(from, assunto, to, content);

        SendGrid sg = new SendGrid(sendGridKey);
        // sg.setDataResidency("eu");
        // uncomment the above line if you are sending mail using a regional EU subuser
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (Exception ex) {

        }
    }

    //Metodo para enviar o email de first login
    public void firtLoginEmail (Auth auth){
        String tokenE = auth.getToken();
        String url = "http://localhost:5173/firstLogin/" + tokenE;

        sendEmail(auth.getUsernameKey(), "Para ativar sua conta acesse esse link", url);
    }

    //Metodo para enviar o email de reset de senha
    public void resetSenhaEmail (Auth auth){
        String tokenE = auth.getToken();
        String url = "http://localhost:5173/firstLogin/" + tokenE;

        sendEmail(auth.getUsernameKey(), "Para redefinir sua senha acesse esse link", url);

    }

}