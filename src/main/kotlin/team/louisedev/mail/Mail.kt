package team.louisedev.mail

import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class Mail constructor(username : String,password : String, host : String){
    private val username = username
    private val password = password
    private val host = host


    fun sendMail(from : String, to : String,subject : String, text : String){
        var properties = System.getProperties()
        properties.setProperty("mail.smtp.host",host)
        properties.put("mail.smtp.auth","true")
        var session = Session.getDefaultInstance(properties)
        var message = MimeMessage(session)
        message.setFrom(InternetAddress(from))
        message.setRecipient(Message.RecipientType.TO, InternetAddress(to))

        message.setSubject(subject)
        message.setText(text)
        message.saveChanges()

        var transport = session.getTransport()
        transport.connect(username,password)
        transport.sendMessage(message,message.allRecipients)
        transport.close()
    }
}