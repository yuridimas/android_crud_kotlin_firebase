package com.example.crudkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ref = FirebaseDatabase.getInstance().getReference("USERS")

        btnSimpan.setOnClickListener {
            saveData()
            val intent = Intent(this, ShowActivity::class.java)
            startActivity(intent)
        }

    }

    private fun saveData() {
        val Nama = edNama.text.toString()
        val Status = edStatus.text.toString()

        val userId = ref.push().key.toString()
        val user = Users(userId,Nama,Status)

        ref.child(userId).setValue(user).addOnCompleteListener{
            Toast.makeText(this,"Sukses Cui", Toast.LENGTH_SHORT).show()
            edNama.setText("")
            edStatus.setText("")
        }
    }
}
