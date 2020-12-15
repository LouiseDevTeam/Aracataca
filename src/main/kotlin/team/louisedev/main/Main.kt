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
import team.louisedev.message.Message
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.collections.ArrayList

suspend fun main(args: Array<String>){
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

/*    bot.subscribeMessages {
        "Hello, Kotlin" reply "Hello, Kotlin"
        case("at me"){
            reply(At(sender as Member) + "pa!")
        }

        (contains("tian") or contains("Boss Xi")) {
            reply("Boss Xi Great!")
        }
    }
 */
    var messages = ArrayList<Message>()
    bot.subscribeFriendMessages {
        always {
            /*print(this.message.contentToString())
            print(this.time)
            print(this.sender.id)
            print(this.sender.nick)
            print(this.sender.avatarUrl)
            print(this.senderName)

             */

            messages.add(Message(this.sender.id,
                this.senderName,
                this.message.contentToString(),
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(1000L * this.time))))
        }
    }
    Thread{
        Timer().schedule(object:TimerTask(){
            override fun run() {
                //println("Hello World!")
                var delta = String()
                for( i in messages){
                    delta += i.toString() + "\n"
                }
                mail.sendMail(smtpUsername,mailUsername,"你收到${messages.size}條聯絡人訊息",delta)
                messages.clear()
            }
        }, Date(), 1800 * 1000)
    }.start()

/*    bot.subscribeGroupMessages {
        always {
            print(this.message.contentToString())
            print(this.sender.id)
            print(this.sender.nick)
            print(this.sender.avatarUrl)
            print(this.senderName)
            print(this.group.id)
        }
    }

 */
    bot.join()
}