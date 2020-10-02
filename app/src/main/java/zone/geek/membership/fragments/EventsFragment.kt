/**
 *   Copyright 2020 Geek Zone UK
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package zone.geek.membership.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_events.view.*
import zone.geek.membership.R
import zone.geek.membership.`interface`.OnEventSelectedListener
import zone.geek.membership.adapter.RssAdapter
import zone.geek.membership.model.Event
import zone.geek.membership.task.LoadDataTask

/**
 * A simple [Fragment] subclass.
 */
class EventsFragment : LoadDataFragment() {

    private lateinit var callback: OnEventSelectedListener

    companion object {
        private lateinit var mRecycleView: RecyclerView
        private lateinit var mLayoutManager: RecyclerView.LayoutManager
        private lateinit var mAdapter: RssAdapter
        private lateinit var mEvents: ArrayList<Event>
        private const val TOAST_FAILED_MESSAGE = "Failed data"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_events, container, false)

        mRecycleView = view.lv_rss
        mRecycleView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(activity)
        LoadDataTask(this).execute()

        return view
    }

    override fun successData(events: ArrayList<Event>) {
        mEvents = events
        mAdapter = RssAdapter(mEvents)
        mAdapter.setOnItemClickListener(object : RssAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                openEventPage(position)
            }
        })
        mRecycleView.layoutManager = mLayoutManager
        mRecycleView.adapter = mAdapter
    }

    override fun failedData() {
        Toast.makeText(activity, TOAST_FAILED_MESSAGE, Toast.LENGTH_SHORT).show()
    }

    fun setOnEventSelectedListener(callback: OnEventSelectedListener) {
        this.callback = callback
    }

    fun openEventPage(fieldPosition: Int) {
        val uri = mEvents[fieldPosition].link
        callback.onEventSelected(fieldPosition, uri)
    }
}