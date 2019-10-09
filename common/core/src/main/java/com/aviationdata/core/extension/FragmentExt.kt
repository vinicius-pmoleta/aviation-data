package com.aviationdata.core.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aviationdata.core.dependencies.KodeinTags
import org.kodein.di.Kodein
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

fun Fragment.selfBind(component: ConfigurableKodein) = Kodein.lazy {
    extend(component)
    bind<Fragment>(tag = KodeinTags.HOST) with provider {
        this@selfBind
    }
}

fun Fragment.updateTitle(title: String) {
    (requireActivity() as AppCompatActivity).supportActionBar?.title = title
}