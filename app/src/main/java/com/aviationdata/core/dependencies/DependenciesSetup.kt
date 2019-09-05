package com.aviationdata.core.dependencies

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object KodeinTags {
    const val HOST_ACTIVITY = "host-activity"
}

fun AppCompatActivity.selfBind(bindings: Kodein.MainBuilder.() -> Unit = {}) = Kodein.lazy {
    val parentContainer = (applicationContext as KodeinAware).kodein
    extend(parentContainer)

    bind<FragmentActivity>(tag = KodeinTags.HOST_ACTIVITY) with provider {
        this@selfBind
    }

    bindings.invoke(this)
}

class DependenciesSetup(private val application: Application) {

    val container by lazy {
        ConfigurableKodein(mutable = true).apply {
            modules.forEach { addImport(it) }
            addConfig {
                bind() from singleton { application }
            }
        }
    }

    private val modules = listOf(
        applicationModule,
        networkModule
    )
}