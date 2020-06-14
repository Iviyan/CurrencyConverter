package com.example.currencyconverter
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView


/**
 * Created by Belal on 9/14/2017.
 */
//we need to extend the ArrayAdapter class as we are building an adapter
class ListViewAdapter //constructor initializing the values
    (
    //activity context
    var context_: Context,
    //the layout resource file for the list items
    var resource: Int,
    //the list values in the List of type hero
    var currencyList: List<Currency>
) :
    ArrayAdapter<Currency?>(context_, resource, currencyList) {

    //this will return the ListView Item as a View
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        val layoutInflater = LayoutInflater.from(context)

        //getting the view
        val view = layoutInflater.inflate(resource, null, false)

        //getting the view elements of the list from the view
        val textViewName = view.findViewById<TextView>(R.id.CurrencyName)
        val textViewValue = view.findViewById<TextView>(R.id.CurrencyValue)
        val textViewERate =
            view.findViewById<TextView>(R.id.ExchangeRate)

        //getting the hero of the specified position
        val currency: Currency = currencyList[position]

        //adding values to the list item
        textViewName.text = currency.name
        textViewValue.text = currency.value.toString()
        textViewERate.text = currency.rate.toString()

        //adding a click listener to the button to remove item from the list
        /*buttonDelete.setOnClickListener { //we will call this method to remove the selected value from the list
            //we are passing the position which is to be removed in the method
            removeHero(position)
        }*/

        //finally returning the view
        return view
    }

    //this method will remove the item from the list
    private fun remove(position: Int) {
    }

}