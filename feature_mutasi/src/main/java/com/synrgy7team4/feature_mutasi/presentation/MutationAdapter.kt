package com.synrgy7team4.feature_mutasi.presentation

import MutationItemAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.synrgy7team4.feature_mutasi.R
import com.synrgy7team4.feature_mutasi.data.response.MutationGroupedByDate

class MutationAdapter(private var groupedMutations: List<MutationGroupedByDate>) : RecyclerView.Adapter<MutationAdapter.DateViewHolder>() {

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTanggal: TextView = itemView.findViewById(R.id.tv_tanggal)
        val rvTransfer: RecyclerView = itemView.findViewById(R.id.rv_transfer)
        val rvTopUp: RecyclerView = itemView.findViewById(R.id.rv_top_up)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cv_mutasi_pertanggal, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val group = groupedMutations[position]
        holder.tvTanggal.text = group.date

        // Set up Transfer RecyclerView
        val transferAdapter = MutationItemAdapter(group.mutations.filter { it.type == "transfer" })
        holder.rvTransfer.adapter = transferAdapter
        holder.rvTransfer.layoutManager = LinearLayoutManager(holder.rvTransfer.context)

        // Set up Top-Up RecyclerView
        val topUpAdapter = MutationItemAdapter(group.mutations.filter { it.type == "top_up" })
        holder.rvTopUp.adapter = topUpAdapter
        holder.rvTopUp.layoutManager = LinearLayoutManager(holder.rvTopUp.context)
    }

    override fun getItemCount(): Int = groupedMutations.size

    fun updateData(newData: List<MutationGroupedByDate>) {
        groupedMutations = newData
        notifyDataSetChanged()
    }
}
