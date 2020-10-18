package com.example.rankanime

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rankanime.modele.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class AccueilActivity : AppCompatActivity(), OnSwipeCallback {

    companion object {
        const val ADD_RESULT_CODE = 0
    }

    private var firebase = FirebaseHelper()
    private var itemList = ItemList()
    private lateinit var recyclerView: RecyclerView
    private val context = this
    private lateinit var noData : TextView
    private lateinit var addButton : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        // ---------------- TOOLBAR ------------------------------
        val toolbar = findViewById<Toolbar>(R.id.mytoolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false);

        // ----------- Initialisation ----------------------------
        addButton = findViewById<FloatingActionButton>(R.id.add_button)
        noData = findViewById<TextView>(R.id.noData)
        recyclerView = findViewById<RecyclerView>(R.id.rv)
        noData.visibility = View.GONE


        // ------------ CLICK ADD BUTTON LISTENER ----------------------------
        addButton.setOnClickListener {
            var addIntent = Intent(this, AddActivity::class.java)
            startActivityForResult(addIntent, ADD_RESULT_CODE)
        }

        // --------- INIT AND FILL RECYCLER VIEW FROM FIREBASE -----------
        getAndFillDataRV()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0)
        {
            if(resultCode == 5)
            {
                var itemFromAddActivity = RvItem(data!!.extras!!.get("num").toString(), data!!.extras!!.get("nom").toString())
                itemList.addItem(itemFromAddActivity)

                if(recyclerView.adapter == null) {
                    initAdapter()
                    swipeActionGesture()
                }
                else
                    recyclerView.adapter!!.notifyDataSetChanged()

            }
        }
    }

    private fun getAndFillDataRV() {

        // ------------- Init loading dialog message ----------
        val dialog = ProgressDialog(context)
        dialog.setMessage("Chargement en cours ...")
        dialog.setCancelable(false)
        dialog.setInverseBackgroundForced(false)
        dialog.show()

        // --------- Listener to fill recyclerview from database
        firebase.reference.orderByChild("rang").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()) {
                    noData.visibility = View.GONE
                    itemList.liste.clear()
                    for(item in snapshot.children)
                    {
                        val theItem = item.getValue(RvItem::class.java)
                        itemList.addItem(theItem!!)
                    }

                    initAdapter()
                    swipeActionGesture()

                    dialog.dismiss()
                }
                else {

                    noData.visibility = View.VISIBLE
                    dialog.dismiss()

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun initAdapter(){
        recyclerView.adapter =
            RvAdapter(itemList.liste)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    // ----------------------------- SWIPE RECYCLERVIEW --------------------------------------

    private fun swipeActionGesture() {
        val touchHelper = ItemTouchHelper(
            LeftRightFullSwipeCallback(
                this, recyclerView.adapter as RvAdapter, this,
                R.drawable.ic_delete,
                Color.parseColor("#D1495B"),
                R.drawable.ic_edit,
                Color.parseColor("#F4E285")
            )
        )
        touchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onSwipeLeft(position: Int) {
        itemList.remove(position)
        recyclerView.adapter!!.notifyDataSetChanged()

        firebase.removeItem(position)
    }

    override fun onSwipeRight(position: Int) {
        Toast.makeText(this, "pos right "+ position, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}