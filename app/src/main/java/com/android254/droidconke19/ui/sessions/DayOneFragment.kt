package com.android254.droidconke19.ui.sessions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android254.droidconke19.R
import com.android254.droidconke19.models.SessionTimeModel
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.ui.filters.Filter
import com.android254.droidconke19.ui.filters.FilterStore
import com.android254.droidconke19.ui.schedule.ScheduleFragmentDirections
import com.android254.droidconke19.utils.nonNull
import com.android254.droidconke19.utils.observe
import com.android254.droidconke19.viewmodels.DayOneViewModel
import kotlinx.android.synthetic.main.fragment_day_one.*
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import java.util.*

class DayOneFragment : Fragment() {

    private val sessionsAdapter: SessionsAdapter by lazy {
        SessionsAdapter{ redirectToSessionDetails() }
    }
    private val dayOneViewModel: DayOneViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_day_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(sessionsRv)
        dayOneViewModel.getDayOneSessions()

        val filterStore = FilterStore.instance
        if (filterStore.filter != Filter.empty()) {
            applyFilter(filterStore.filter)
        }


        //observe live data emitted by view model
        observeLiveData()
    }

    private fun redirectToSessionDetails() {
        findNavController().navigate(ScheduleFragmentDirections.actionScheduleFragmentToSessionDetailsFragment())
    }

    private fun observeLiveData() {
        dayOneViewModel.getSessionsResponse().nonNull().observe(this) {sessionList ->
            updateAdapterWithList(sessionList)
        }

    }

    private fun updateAdapterWithList(sessionList: List<SessionsModel>) {
        sessionsAdapter.update(sessionList)
        loginProgressBar.visibility = View.GONE

    }

    fun onSessionClick(sessionModel : SessionsModel){
        redirectToSessionDetails()
    }

    fun applyFilter(filter: Filter) {
        sessionsAdapter.applyFilter(filter)
    }

    private fun handleError(databaseError: String?) {
        activity?.toast(databaseError.toString())
    }

    private fun initView(sessionsRv: RecyclerView) {
        sessionsRv.adapter = sessionsAdapter
    }

}