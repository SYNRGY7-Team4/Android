package com.jer.shared

import androidx.lifecycle.ViewModelProvider

interface ViewModelFactoryProvider {
    fun provideViewModelFactory(): ViewModelProvider.Factory
}