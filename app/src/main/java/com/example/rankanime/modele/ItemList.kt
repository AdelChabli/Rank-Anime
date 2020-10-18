package com.example.rankanime.modele

import java.util.*
import kotlin.collections.ArrayList

class ItemList {

    var liste : ArrayList<RvItem>

    init {
        liste = ArrayList<RvItem>()
    }

    fun addItem(item : RvItem) {

        if(item.rang.toInt() <= 0)
            item.rang = "1"

        if(item.rang.toInt() > (liste.size+1) || liste.isEmpty())
        {
            item.rang = (liste.size+1).toString()
            liste.add(item)
        }
        else
        {
            liste.add(item.rang.toInt()-1, item)
        }

        reorganisedList()
    }

    fun swap(i : Int, j : Int){
        var item = liste.get(i)
        liste.set(i, liste.get(j))
        liste.set(j, item)
    }

    fun reorganisedFromAndAddItem(item : RvItem)
    {

    }

    fun remove(positon : Int)
    {
        liste.removeAt(positon)
        reorganisedList()
    }

    fun reorganisedList() {
        for(i in 0 until liste.size)
        {
            liste.get(i).rang = (i+1).toString()
        }
    }
}