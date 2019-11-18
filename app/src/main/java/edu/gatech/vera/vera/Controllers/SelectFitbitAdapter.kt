package edu.gatech.vera.vera.Controllers

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import edu.gatech.vera.vera.R
import org.w3c.dom.Text

class SelectFitbitAdapter(val items: ArrayList<String>): RecyclerView.Adapter<SelectFitbitAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fitbit_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.fitbitName?.text = items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val fitbitName : TextView = itemView.findViewById(R.id.fitbitName)
    }
}
