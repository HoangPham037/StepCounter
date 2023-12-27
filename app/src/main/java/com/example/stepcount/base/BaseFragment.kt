package com.example.stepcount.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.stepcount.data.viewmodel.AppViewModel
import com.example.stepcount.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingLayoutInflater: (inflater: LayoutInflater) -> VB
) : Fragment() {

    private var _binding: VB? = null
    val binding: VB
        get() = _binding as VB

    val appViewModel: AppViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            _binding = bindingLayoutInflater.invoke(inflater)
            binding.root
        } catch (e: Throwable) {
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        setupDataObserver()
        getData()
        setupView()
        initEventOnClick()

    }

    open fun initEventOnClick() {}
    open fun setData() {}
    open fun getData() {}
    open fun setupView() {}
    open fun setupDataObserver() {}
    open fun getMainActivity() = activity as MainActivity?
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

