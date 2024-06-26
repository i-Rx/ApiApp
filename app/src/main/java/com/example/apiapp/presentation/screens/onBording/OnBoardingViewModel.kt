package com.example.apiapp.presentation.screens.onBording

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiapp.domain.onBording.GetIsSafeFromDataStoreUseCase
import com.example.apiapp.domain.onBording.SaveIsFirstTimeInDataStoreUseCase
import com.example.apiapp.presentation.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val saveIsFirstTimeInDataStoreUseCase: SaveIsFirstTimeInDataStoreUseCase,
    private val getIsSafeFromDataStoreUseCase: GetIsSafeFromDataStoreUseCase
) : ViewModel() {

    val onBoardingCompleted = MutableStateFlow(false)
    val startDestination: MutableState<String> = mutableStateOf(Screens.Home.route)


    init {
        getOnBoardingState()
    }

    fun getOnBoardingState() {
        viewModelScope.launch {
            onBoardingCompleted.value =
                getIsSafeFromDataStoreUseCase().stateIn(viewModelScope).value
            startDestination.value =
                if (onBoardingCompleted.value) Screens.Home.route else Screens.OnBoarding.route

        }
    }

    fun saveOnBoardingState(showOnBoardingPage: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            saveIsFirstTimeInDataStoreUseCase(showTipsPage = showOnBoardingPage)
        }
    }
}