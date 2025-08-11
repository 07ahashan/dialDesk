package com.appedu.dialdesk.utils

class Utility {

    private fun getValidText(string: String) : Boolean{
        return string.isNotEmpty()
    }

    private fun checkEmptyValue(string: String) : String{
        if(string.isNotEmpty() || string.isNotBlank())
            return ""
        return string
    }

    companion object{
        const val appId: Long = 246203318
        const val appSign = "9de1b6306dcd3ef9a29350367ea2ccff18dc7c855b3f30d97a2989c5030a04a1"
    }
}