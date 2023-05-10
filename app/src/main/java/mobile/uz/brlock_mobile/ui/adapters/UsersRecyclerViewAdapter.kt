package mobile.uz.brlock_mobile.ui.adapters

import mobile.uz.brlock_mobile.domain.Entity
import kotlinx.android.synthetic.main.item_view.view.*
import mobile.uz.brlock_mobile.R
import mobile.uz.brlock_mobile.base.BaseAdapter
import mobile.uz.brlock_mobile.utils.common.ViewHolder

class UsersRecyclerViewAdapter : BaseAdapter<Entity>(R.layout.item_view) {

    override fun bindViewHolder(holder: ViewHolder, data: Entity) {
        holder.itemView.apply {
            data.apply {
                fullName.text = data.name + data.surname
                phone_number.text = data.phoneNumber
            }
        }
    }
}
