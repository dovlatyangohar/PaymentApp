package com.example.paymentapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

abstract class BaseFragment<ViewModel : BaseViewModel, ViewBind : ViewBinding> : Fragment() {
    abstract val viewModel: ViewModel
    abstract val binding: ViewBind


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onEach()
        onView()
        onViewClick()

    }

    protected open fun onView() {}

    protected open fun onViewClick() {}

    protected open fun onEach() {}

    protected inline fun <reified T> onEach(flow: Flow<T>, crossinline action: (T) -> Unit) =
        view?.run {
            if (!this@BaseFragment.isAdded) return@run
            flow.onEach { action(it ?: return@onEach) }.observeInLifecycle(viewLifecycleOwner)
        }

    inline fun <reified T> Flow<T>.observeInLifecycle(
        lifecycleOwner: LifecycleOwner
    ) = FlowObserver(lifecycleOwner, this)

}