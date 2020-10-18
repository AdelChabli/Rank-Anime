package com.example.rankanime

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.rankanime.modele.FirebaseHelper
import com.example.rankanime.modele.RvItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AddActivity : AppCompatActivity() {

    private var firebase = FirebaseHelper()
    private lateinit var editNom : EditText
    private lateinit var editNum : EditText
    private lateinit var btnAdd : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // ----------- Initialisation ---------------
        editNom = findViewById<EditText>(R.id.editNom)
        editNum = findViewById<EditText>(R.id.editNum)
        btnAdd = findViewById<Button>(R.id.btnAdd)

        // ------------- Listener boutton ajouter --------------------
        btnAdd.setOnClickListener() {
            val nom = editNom.text.toString()
            val num = editNum.text.toString()

            if((nom.length > 0) && (num.length > 0)) {

                firebase.addItemToFirebase(RvItem(num,nom))

                val intent = Intent(this, AccueilActivity::class.java)
                intent.putExtra("nom", nom)
                intent.putExtra("num", num)
                setResult(5, intent);
                finish()
            }
            else {
                val dialog = AlertDialog.Builder(this, R.style.AlertDialogStyle)
                dialog.setTitle("ERREUR")
                dialog.setMessage("Impossible de laisser des champs vides ! ")
                dialog.show()
            }
        }
    }
}