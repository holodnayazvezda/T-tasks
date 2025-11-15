package com.prod.t_tasks.presentation.screens.tripList

import androidx.lifecycle.viewModelScope
import com.prod.t_tasks.presentation.commonDataModels.UITripModel
import com.prod.t_tasks.presentation.utils.UIState
import com.prod.t_tasks.presentation.utils.base.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TripListScreenViewModel: BaseViewModel() {

	val tripsList = MutableUIStateFlow<List<UITripModel>>()

	private fun loadTripsList(){
		viewModelScope.launch {
			tripsList.emit(
				UIState.Success(
					listOf(
						UITripModel(
							id = 0L,
							placeName = "Чебоксары",
							dateRangeString = "13.11.2025-17.11-2025",
							countOfPeoples = 4,
							readyPercentage = 52
						),
						UITripModel(
							id = 1L,
							placeName = "Самара",
							dateRangeString = "20.11.2025-25.11-2025",
							countOfPeoples = 7,
							readyPercentage = 12
						),
						UITripModel(
							id = 2L,
							placeName = "Москва",
							dateRangeString = "27.11.2025-05.12-2025",
							countOfPeoples = 2,
							readyPercentage = null
						)
					)
				)
			)
		}
	}

	fun update(){
		loadTripsList()
	}

	init {
		update()
	}
}
