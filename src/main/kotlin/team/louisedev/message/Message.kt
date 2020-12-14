package team.louisedev.message

data class Message(val id : Long,
                    val nick : String,
                    val message : String,
                    val time : Int){

    override fun toString() : String{
        return "$id($nick):says $message at $time"
    }
}