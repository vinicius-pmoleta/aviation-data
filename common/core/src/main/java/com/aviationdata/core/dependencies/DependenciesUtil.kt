package com.aviationdata.core.dependencies

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import org.kodein.di.Kodein
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

object KodeinTags {
    const val HOST_ACTIVITY = "host-activity"
    const val REMOTE_SOURCE_OPEN_SKY = "opensky-remote"
    const val REMOTE_SOURCE_JET_PHOTOS = "jetphotos-remote"
}

fun AppCompatActivity.selfBind(component: ConfigurableKodein) = Kodein.lazy {
    extend(component)
    bind<FragmentActivity>(tag = KodeinTags.HOST_ACTIVITY) with provider {
        this@selfBind
    }
}