package mobile.uz.brlock_mobile.base

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import mobile.uz.brlock_mobile.utils.common.ViewHolder
import mobile.uz.brlock_mobile.utils.extentions.inflate

abstract class BaseAdapter<T>(@LayoutRes val layoutID: Int) : RecyclerView.Adapter<ViewHolder>() {

    var listener: ((data: Any) -> Unit)? = null
    protected var items = mutableListOf<T>()
    open fun setData(data: List<T>) {
        items.addAll(data)
        notifyDataSetChanged()
    }

 /*   open fun addData(data: List<T>) {
        items.addAll(data)
        notifyItemRangeInserted(items.size - data.size, data.size)
    }

    var nextPage: (() -> Unit)? = null*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(layoutID))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            bindViewHolder(this, items[holder.adapterPosition])
            holder.itemView.setOnClickListener {
                if (holder.adapterPosition != -1)
                    listener?.invoke(this)
            }

       /*     if (holder.adapterPosition == items.size - 1) {
                nextPage?.invoke()
            }*/
        }
    }

    abstract fun bindViewHolder(holder: ViewHolder, data: T)

}