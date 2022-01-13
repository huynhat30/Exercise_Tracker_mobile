package com.example.exercisetracking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exercisetracking.R
import com.example.exercisetracking.database.PhysActivity
import com.example.exercisetracking.service.TimerConverter
import kotlinx.android.synthetic.main.item_act_run.view.*
import java.text.SimpleDateFormat
import java.util.*

class ActAdapter : RecyclerView.Adapter<ActAdapter.ActViewHolder>() {
    inner class ActViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)


    val diffCallBack = object : DiffUtil.ItemCallback<PhysActivity>() {
        /**
         * Called to check whether two objects represent the same item.
         *
         *
         * For example, if your items have unique ids, this method should check their id equality.
         *
         *
         * Note: `null` items in the list are assumed to be the same as another `null`
         * item and are assumed to not be the same as a non-`null` item. This callback will
         * not be invoked for either of those cases.
         *
         * @param oldItem The item in the old list.
         * @param newItem The item in the new list.
         * @return True if the two items represent the same object or false if they are different.
         *
         * @see Callback.areItemsTheSame
         */
        override fun areItemsTheSame(oldItem: PhysActivity, newItem: PhysActivity): Boolean {
            return oldItem.id == newItem.id
        }

        /**
         * Called to check whether two items have the same data.
         *
         *
         * This information is used to detect if the contents of an item have changed.
         *
         *
         * This method to check equality instead of [Object.equals] so that you can
         * change its behavior depending on your UI.
         *
         *
         * For example, if you are using DiffUtil with a
         * [RecyclerView.Adapter], you should
         * return whether the items' visual representations are the same.
         *
         *
         * This method is called only if [.areItemsTheSame] returns `true` for
         * these items.
         *
         *
         * Note: Two `null` items are assumed to represent the same contents. This callback
         * will not be invoked for this case.
         *
         * @param oldItem The item in the old list.
         * @param newItem The item in the new list.
         * @return True if the contents of the items are the same or false if they are different.
         *
         * @see Callback.areContentsTheSame
         */
        override fun areContentsTheSame(oldItem: PhysActivity, newItem: PhysActivity): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    fun submitList(list: List<PhysActivity>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActViewHolder {
        return ActViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_act_run, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ActViewHolder, position: Int) {

        val act = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(act.img1).into(ivRunImage)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = act.dateRecord
            }
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            tvDate.text = dateFormat.format(calendar.time)

            val avgSpeed = "${act.avgVelKMH}km/h"
            tvAvgSpeed.text = avgSpeed

            val distanceInKm = "${act.latitudeMeter / 1000f}km"
            tvDistance.text = distanceInKm

            tvTime.text = TimerConverter.getTimerFormatted(act.timeRecord)

            val caloriesBurned = "${act.estCalor}kcal"
            tvCalories.text = caloriesBurned
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}