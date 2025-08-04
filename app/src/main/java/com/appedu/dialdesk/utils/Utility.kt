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
}