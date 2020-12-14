package team.louisedev.main

import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.subscribeFriendMessages
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.join
import net.mamoe.mirai.message.data.At
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

suspend fun main(args: Array<String>){
        var properties = System.getProperties()
        properties.setProperty("mail.smtp.host","smtp.exmail.qq.com")
        properties.put("mail.smtp.auth","true")
        var session = Session.getDefaultInstance(properties)
        var message = MimeMessage(session)
        message.setFrom(InternetAddress("i@pixiv.world"))
        message.setRecipient(Message.RecipientType.TO, InternetAddress("app@pixiv.world"))

        message.setSubject("This is a test")
        message.setText("For test only")
        message.saveChanges()

        var transport = session.getTransport()
        transport.connect("i@pixiv.world","pwd")
        transport.sendMessage(message,message.allRecipients)
        transport.close()


/*    if(args.size % 2 != 0){
        print("Error: Wrong args")
        return
    }



    var qqID  = 0L
    var password  = String()
    var port = 6666

    val end = args.size - 1
    for(i in 0 .. end step 2){
        if (args[i] == "-a") qqID = args[i + 1].toLong()
        else if (args[i] == "-p") password = args[i + 1]
    }

    var bot = Bot(qqID, password).alsoLogin()

    bot.subscribeMessages {
        "Hello, Kotlin" reply "Hello, Kotlin"
        case("at me"){
            reply(At(sender as Member) + "pa!")
        }

        (contains("tian") or contains("Boss Xi")) {
            reply("Boss Xi Great!")
        }
    }
    bot.subscribeFriendMessages {
        always {
            print(this.message.contentToString())
            print(this.sender.id)
            print(this.sender.nick)
            print(this.sender.avatarUrl)
            print(this.senderName)
        }
    }

    bot.subscribeGroupMessages {
        always {
            print(this.message.contentToString())
            print(this.sender.id)
            print(this.sender.nick)
            print(this.sender.avatarUrl)
            print(this.senderName)
            print(this.group.id)
        }
    }
    bot.join()*/
}