package com.example.spodemy.Authentication_Asset

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.spodemy.data.User
import com.example.spodemy.R
import com.example.spodemy.data.fit
//import com.github.ybq.android.spinkit.SpinKitView
//import com.example.bookhub.databinding.SignupFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.signup_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class signup_fragment(): Fragment() {
    var root:View?=null
    private lateinit var ref:DatabaseReference
    var mAurh:FirebaseAuth?=null
    var userID:String?=null
    private var progressBarDialog:ProgressDialog?=null
    private var chn:String?=null
//    private var selected_year:String?=null
//    private var selected_month:String?=null
//    private var selected_day:String?=null
    private  var age:String?=null

    companion object{
        const val TAG="TAG"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.signup_fragment, container, false)

        register()
        return root
    }

@SuppressLint("UseRequireInsteadOfGet")
// create user for authentication
fun createuser(){
//        val dateofbirth=root!!.findViewById<EditText>(R.id.dob)
        var ffullname:EditText=root!!.findViewById(R.id.fullname)
//        val mfullname=ffullname.text.toString()
        val phone:EditText=root!!.findViewById(R.id.phone)
        val email:EditText=root!!.findViewById(R.id.email)
        val password:EditText=root!!.findViewById(R.id.password)
//        val DOB:EditText= root!!.findViewById(R.id.dob)
        val memail=email.text.toString()
        val mpassword=password.text.toString()
        val fAuth:FirebaseAuth= FirebaseAuth.getInstance()

//        val progressbar:SpinKitView=root!!.findViewById(R.id.progress_bar)
        try {
            if (email.text.toString().isEmpty())
            {
                email.error="Email is required"
                return
            }
            if (password.text.toString().isEmpty())
            {
                password.error="Email is required"
                return
            }
            if(fullname.text.toString().isEmpty())
            {
                fullname.error="Fullname is required"
                return
            }
            if (password.text.toString().length<6)
            {
                password.error="password must be greater then 6 characters"
                return
            }
            progressBarDialog= ProgressDialog(requireContext())
//            progressbar.visibility=View.VISIBLE
            progressBarDialog!!.show()
            //create account on app
            fAuth.createUserWithEmailAndPassword(memail, mpassword).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // send verification link
                    val fuser = fAuth.currentUser
                    fuser!!.sendEmailVerification().addOnSuccessListener {
                        Toast.makeText(
                            activity,
                            "Verification Email Has been Sent.\n Verify Your Email to use this app",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent:Intent= Intent(activity, MainAuthentication::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        activity!!.finish()
                    }.addOnFailureListener { e ->
                        Log.d(
                            TAG,
                            "onFailure: Email not sent " + e.message
                        )
//                        progressbar.visibility=View.GONE
                        progressBarDialog!!.dismiss()
                    }
                    ref= FirebaseDatabase.getInstance().getReference("Healthify/users")
//                    val getid=ref.push().key
                    val currenuser=fAuth.currentUser!!.uid
                    val User= User(fullname.text.toString(),email.text.toString(),password.text.toString(),phone.text.toString(),dob.text.toString(),chn!!.toString(),age.toString())
                    ref.child(currenuser).setValue(User).addOnSuccessListener {
                        val ref: DatabaseReference =
                            FirebaseDatabase.getInstance()
                                .getReference("Healthify/users/${currenuser.toString()}")
                        val fit = fit("0","0","0")
                        ref.child("Fitness").setValue(fit)
                            .addOnSuccessListener {
                            }
                        ffullname.text.clear()
                        email.text.clear()
                        password.text.clear()
                        phone.text.clear()
                    }.addOnFailureListener{
                        Toast.makeText(
                            activity,
                            "something going wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        activity,
                        "Error ! " + task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBarDialog!!.dismiss()
                }
            }
        }
        catch (e:Exception)
        {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }
//    register function
@RequiresApi(Build.VERSION_CODES.N)
fun register(){
    val dob:TextView=root!!.findViewById(R.id.dob)
    dob.setOnClickListener {
        clickDataPicker()
    }
    val gender=resources.getStringArray(R.array.gender)
    val arrayAdapter=ArrayAdapter(requireContext(),R.layout.dropdown,gender)
     val chgen=root!!.findViewById<AutoCompleteTextView>(R.id.gender)
     chgen.setAdapter(arrayAdapter)
    chgen.setOnItemClickListener { adapterView, view, i, l ->
        chn=adapterView.getItemAtPosition(i).toString()
    }
//    chn=chgen.text.toString()
        val regbtn:Button=root!!.findViewById(R.id.signupbtn)
        regbtn.setOnClickListener{
            createuser()
        }
    }
    fun clickDataPicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            requireContext(),
            { view, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                dob.text=(selectedDate.toString())
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                val theDate = sdf.parse(selectedDate)
                val selectedyear=theDate.year
                val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))
                var currentYear =currentDate.year
                 age = (currentYear - selectedyear).toString()
            },
            year,
            month,
            day
        )
        dpd.datePicker.maxDate = Date().time - 86400000
        dpd.show()
    }
}