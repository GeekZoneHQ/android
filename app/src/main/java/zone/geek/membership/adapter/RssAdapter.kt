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

package zone.geek.membership.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import zone.geek.membership.R
import zone.geek.membership.model.Event

class RssAdapter(eventList: ArrayList<Event>) : RecyclerView.Adapter<RssAdapter.RssViewHolder>() {

    private val mEventList = eventList
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    inner class RssViewHolder(itemView: View, private var listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val mImageView: ImageView = itemView.event_icon
        var mTitle: TextView = itemView.event_title
        var mDate: TextView = itemView.event_date

        fun bindItem(position: Int) = with(itemView) {
            setOnClickListener {
                if(position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RssViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return RssViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: RssViewHolder, position: Int) {
        val currentItem: Event = mEventList[position]

        holder.mImageView.setImageResource(R.drawable.ic_event_item)
        holder.mTitle.text = currentItem.title
        holder.mDate.text = currentItem.date

        holder.bindItem(position)
    }

    override fun getItemCount(): Int = mEventList.size
}