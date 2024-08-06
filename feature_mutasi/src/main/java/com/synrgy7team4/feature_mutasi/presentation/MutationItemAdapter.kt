import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.synrgy7team4.feature_mutasi.R

import com.synrgy7team4.feature_mutasi.data.response.MutationData


class MutationItemAdapter(private val mutationList: List<MutationData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_TRANSFER = 1
        private const val TYPE_TOP_UP = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (mutationList[position].type) {
            "transfer" -> TYPE_TRANSFER
            "top_up" -> TYPE_TOP_UP
            else -> TYPE_TRANSFER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_TRANSFER -> {
                val view = layoutInflater.inflate(R.layout.cv_transfer, parent, false)
                TransferViewHolder(view)
            }
            TYPE_TOP_UP -> {
                val view = layoutInflater.inflate(R.layout.cv_top_up, parent, false)
                TopUpViewHolder(view)
            }
            else -> {
                val view = layoutInflater.inflate(R.layout.cv_transfer, parent, false)
                TransferViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mutation = mutationList[position]
        when (holder) {
            is TransferViewHolder -> holder.bind(mutation)
            is TopUpViewHolder -> holder.bind(mutation)
        }
    }

    override fun getItemCount(): Int = mutationList.size

    class TransferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTransfer: TextView = itemView.findViewById(R.id.tv_transfer)
        private val tvTransferKe: TextView = itemView.findViewById(R.id.tv_transfer_ke)
        private val tvNominal: TextView = itemView.findViewById(R.id.tv_nominal)

        fun bind(mutation: MutationData) {
            tvTransfer.text = "Transfer" // Replace with appropriate string or value
            tvTransferKe.text = "Transfer ke ${mutation.account_to}"
            tvNominal.text = mutation.amount.toString()

        }
    }

    class TopUpViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTopUp: TextView = itemView.findViewById(R.id.tv_top_up)
        private val tvTopUpKe: TextView = itemView.findViewById(R.id.tv_top_up_ke)
        private val tvNominal: TextView = itemView.findViewById(R.id.tv_nominal)

        fun bind(mutation: MutationData) {
            tvTopUp.text = "Top Up" // Replace with appropriate string or value
            tvTopUpKe.text = "Top Up ke ${mutation.account_to}"
            tvNominal.text = mutation.amount.toString()
        }
    }
}
