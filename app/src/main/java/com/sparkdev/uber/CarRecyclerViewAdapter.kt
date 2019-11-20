package com.sparkdev.uber
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sparkdev.uber.CarRecyclerViewAdapter.CarViewHolder
import kotlinx.android.synthetic.main.car_item.view.*

class CarRecyclerViewAdapter(val carListImg : ArrayList<Drawable>, val carList: ArrayList<String>): RecyclerView.Adapter<CarViewHolder>(){

    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val carType = itemView.tv_rideType
        val carImage = itemView.carImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
       val carView = LayoutInflater.from(parent.context).inflate(R.layout.car_item, parent, false)
        return CarViewHolder(carView)
    }

    override fun getItemCount(): Int {
        return carList.size
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.carType.text = carList[position]
        holder.carImage.background = carListImg[position]
    }
}
