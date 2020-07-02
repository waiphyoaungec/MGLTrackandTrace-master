package com.sh.mgltrackandtrace.model

import java.io.Serializable

data class FeatureImage(val media_details: FeatureImage) : Serializable {
    var sizes: FeatureImage? = null
    var medium: FeatureImage? = null
    var source_url: String? = ""
}