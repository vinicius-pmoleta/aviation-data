package com.aviationdata.core.dependencies

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

object KodeinTags {
    const val HOST_ACTIVITY = "host-activity"
}

fun AppCompatActivity.selfBind(bindings: Kodein.MainBuilder.() -> Unit = {}) = Kodein.lazy {
    val parentContainer = (applicationContext as KodeinAware).kodein
    extend(parentContainer)

    bind<FragmentActivity>(tag = KodeinTags.HOST_ACTIVITY) with provider {
        this@selfBind
    }

    bindings(this)
}