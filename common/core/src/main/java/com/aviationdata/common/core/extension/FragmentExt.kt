package com.aviationdata.common.core.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aviationdata.common.core.dependencies.DependenciesUtil
import org.kodein.di.Kodein
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

fun Fragment.selfBind(component: ConfigurableKodein) = Kodein.lazy {
    extend(component)
    bind<Fragment>(tag = DependenciesUtil.HOST) with provider {
        this@selfBind
    }
}

fun Fragment.updateTitle(title: String) {
    val activity = requireActivity()
    if (activity is AppCompatActivity) {
        activity.supportActionBar?.title = title
    }
}