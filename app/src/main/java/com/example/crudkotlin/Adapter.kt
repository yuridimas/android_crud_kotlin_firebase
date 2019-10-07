package com.example.crudkotlin

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

class Adapter(val mCtx: Context, val layoutResId: Int, val list: List<Users>) :
    ArrayAdapter<Users>(mCtx, layoutResId, list) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val tvNama = view.findViewById<TextView>(R.id.tvNama)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)

        val tvUpdate = view.findViewById<TextView>(R.id.tvUpdate)
        val tvDelete = view.findViewById<TextView>(R.id.tvDelete)

        val user = list[position]

        tvNama.text = user.Nama
        tvStatus.text = user.Status

        tvUpdate.setOnClickListener {
            showUpdateDialog(user)
        }
        tvDelete.setOnClickListener {
            deleteInfo(user)
        }

        return view
    }

    private fun deleteInfo(user: Users) {
        val mydatabse = FirebaseDatabase.getInstance().getReference("USERS")
        mydatabse.child(user.Id).removeValue()
        Toast.makeText(mCtx,"Deleted!!",Toast.LENGTH_SHORT).show()
        val intent = Intent(context, ShowActivity::class.java)
        context.startActivity(intent)
    }

    private fun showUpdateDialog(user: Users) {
        val builder = AlertDialog.Builder(mCtx)

        builder.setTitle("Update")

        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.update, null)

        val textNama = view.findViewById<EditText>(R.id.edNamaEdit)
        val textStatus = view.findViewById<EditText>(R.id.edStatusEdit)

        textNama.setText(user.Nama)
        textStatus.setText(user.Status)

        builder.setView(view)

        builder.setPositiveButton("Update") { dialog, which ->
            val dbuser = FirebaseDatabase.getInstance().getReference("USERS")
            val nama = textNama.text.toString().trim()
            val status = textStatus.text.toString().trim()

            if (nama.isEmpty()) {
                textNama.error = "nama anda siapa?"
                textNama.requestFocus()
                return@setPositiveButton
            }
            if (status.isEmpty()) {
                textStatus.error = "status anda apa?"
                textStatus.requestFocus()
                return@setPositiveButton
            }

            val user = Users(user.Id,nama,status)

            dbuser.child(user.Id).setValue(user).addOnCompleteListener {
                Toast.makeText(mCtx,"Updated",Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("No"){dialog, which ->

        }

        val alert = builder.create()
        alert.show()
    }
}