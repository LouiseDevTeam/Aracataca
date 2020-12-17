package team.louisedev.main

import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.subscribeFriendMessages
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.join
import net.mamoe.mirai.message.data.At
import team.louisedev.mail.Mail
import team.louisedev.message.GroupMessage
import team.louisedev.message.Message
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

suspend fun main(args: Array<String>){
    val VERSION = "Louise Dev Team Aracataca 0.1-GM.1"
    val HELP = "${VERSION}\n" +
                "Usage:\n" +
                "  -a              QQ ID\n" +
                "  -p              QQ password\n" +
                "  -smtp-host      SMTP server\n" +
                "  -smtp-username  SMTP username\n" +
                "  -smtp-password  SMTP password\n" +
                "  -mail-username  Mail to receive\n" +
                "\nExample:\n" +
                "java -jar Aracataca-{VERSION}-all.jar -a 10000 -p pwd -smtp-host smtp.exmail.qq.com -smtp-username i@qq.com -smtp-password pwd -mail-username app@qq.com\n"

    if (args.isEmpty()){
        print(HELP)
        return
    }

    if(args.size % 2 != 0){
        print("Error: Wrong args")
        return
    }

    var qqID  = 0L
    var password = String()
    var smtpHost = String()
    var smtpUsername = String()
    var smtpPassword = String()
    var mailUsername = String()

    val end = args.size - 1
    for(i in 0 .. end step 2){
        if (args[i] == "-a") qqID = args[i + 1].toLong()
        else if (args[i] == "-p") password = args[i + 1]
        else if (args[i] == "-smtp-host") smtpHost = args[i + 1]
        else if (args[i] == "-smtp-username") smtpUsername = args[i + 1]
        else if (args[i] == "-smtp-password") smtpPassword = args[i + 1]
        else if (args[i] == "-mail-username") mailUsername = args[i + 1]
    }

    var mail = Mail(smtpUsername,smtpPassword,smtpHost)
    var bot = Bot(qqID, password) {
        fileBasedDeviceInfo("device.json")
    }.alsoLogin()

    var messages = ArrayList<Message>()
    var groupMessages = ArrayList<GroupMessage>()
    //var groupNames = HashSet<String>()
    bot.subscribeFriendMessages {
        always {
            messages.add(Message(this.sender.id,
                this.senderName,
                this.message.contentToString(),
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(1000L * this.time))))
        }
    }

    bot.subscribeGroupMessages {
        always {
            //groupNames.add(this.group.name)
            groupMessages.add(
                GroupMessage(this.sender.id,
                    if(this.sender.nameCard == "") this.sender.nick else this.sender.nameCard,
                    this.group.name,
                    this.message.contentToString(),
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(1000L * this.time)))
            )
        }
    }

    Thread{
        Timer().schedule(object:TimerTask(){
            override fun run() {
                if(messages.size > 0 || groupMessages.size > 0){
                    var delta = String()
                    for(i in messages){
                        delta += i.toString() + "\n"
                    }
                    for(i in groupMessages){
                        delta += i.toString() + "\n"
                    }
                    delta += VERSION + "\n"
                    mail.sendMail(smtpUsername,mailUsername,"你收到${messages.size}條聯絡人訊息及${groupMessages.size}條群組訊息",delta)
                    messages.clear()
                    groupMessages.clear()
                }
            }
        }, Date(), 1800 * 1000)
    }.start()

    bot.join()
}