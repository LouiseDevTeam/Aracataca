package team.louisedev.message

data class Message(val id : Long,
                    val nick : String,
                    val message : String,
                    val time : String){

    override fun toString() : String{
        //$id
        return "「$nick」說：「$message」 在 $time。"
    }
}

data class GroupMessage(val id : Long,
                   val nick : String,
                   val groupName : String,
                   val message : String,
                   val time : String){

    override fun toString() : String{
        //$id
        return "「$nick」在群組（${groupName}）說：「$message」 在 $time。"
    }
}