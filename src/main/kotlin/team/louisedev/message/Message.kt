package team.louisedev.message

data class Message(val id : Long,
                    val nick : String,
                    val message : String,
                    val time : String){

    override fun toString() : String{
        return "$id($nick)說：$message 在 $time"
    }
}