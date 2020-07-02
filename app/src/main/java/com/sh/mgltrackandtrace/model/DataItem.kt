package com.sh.mgltrackandtrace.model

import java.io.Serializable


data class DataItem(val date: String) : Serializable {

    var title: Title? = null
    var excerpt: Excerpt? = null
    var better_featured_image: FeatureImage? = null
    var content : Content? = null
    var id : Int? = null
    //var status : String? = null
}