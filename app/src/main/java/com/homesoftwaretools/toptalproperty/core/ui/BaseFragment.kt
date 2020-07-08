package com.homesoftwaretools.toptalproperty.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KClass

abstract class BaseFragment : Fragment() {

    abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    inline fun <reified T : BaseViewModel> BaseFragment.scopedViewModel(): Lazy<T> =
        viewModel { parametersOf((activity as? BaseActivity)?.lifecycleScope?.id) }

    fun <T> LiveData<T>.onChange(listener: (T) -> Unit) = this.observe(viewLifecycleOwner, Observer(listener))
}