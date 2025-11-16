package com.prod.t_tasks.presentation.screens.tripList

import androidx.lifecycle.viewModelScope
import com.prod.t_tasks.data.data_providers.local_db.entities.mapToDB
import com.prod.t_tasks.domain.usecase.trips.GetAllUserTripsUseCase
import com.prod.t_tasks.presentation.commonDataModels.UITripModel
import com.prod.t_tasks.presentation.commonDataModels.mapToUI
import com.prod.t_tasks.presentation.utils.UIState
import com.prod.t_tasks.presentation.utils.base.BaseViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TripListScreenViewModel(
	private val getAllUserTripsUseCase: GetAllUserTripsUseCase
) : BaseViewModel() {

	val tripsList = MutableUIStateFlow<List<UITripModel>>()

	private fun loadTripsList() {
		getAllUserTripsUseCase().map { it.map { it.map { it.mapToUI() } } }
			.collectRequest(tripsList)
	}

	fun update() {
		loadTripsList()
	}

	init {
		update()
	}
}
