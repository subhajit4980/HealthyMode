package com.example.HealthyMode.All_View.Authentication_View

//import com.github.ybq.android.spinkit.SpinKitView
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.HealthyMode.All_View.Home.Home_screen
import com.example.HealthyMode.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class Login_fragment():Fragment() {
    var root: View? = null
    @SuppressLint("UseRequireInsteadOfGet", "SetTextI18n")
    private fun createuserlog()
    {
        val memail: EditText = root!!.findViewById(R.id.email)
        val mpassword: EditText =root!!.findViewById(R.id.password)
//        val progressBar: SpinKitView =root!!.findViewById(R.id.progress_bar)
        val pass_error:TextView=root!!.findViewById(R.id.password_error)
        val rightAnimation: Animation? = AnimationUtils.loadAnimation(activity, R.anim.rightt_left)
        val leftAnimation: Animation? = AnimationUtils.loadAnimation(activity, R.anim.left_right)
        val layemail:TextInputLayout=root!!.findViewById(R.id.tvemail)
        val laypass:TextInputLayout=root!!.findViewById(R.id.tvpass)
        pass_error.animation=leftAnimation
        layemail.animation=rightAnimation
        laypass.animation=leftAnimation
        val  fAuth: FirebaseAuth = FirebaseAuth.getInstance()
        try {
            if (memail.text.toString().isEmpty()) {
                memail.error = "Email is Required."
                return
            }
            if (mpassword.text.toString().isEmpty()) {
                pass_error.text="Password is Required *"
                return
            }
            if (mpassword.text.toString().length < 6) {
                pass_error.text="Password Must be greater than 6 Characters *"
                return
            }
//            progressBar.visibility = View.VISIBLE

            fAuth.signInWithEmailAndPassword(memail.text.toString(),mpassword.text.toString()).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if(fAuth.currentUser!!.isEmailVerified){
                        Toast.makeText(activity, "Logged in Successfully", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(activity, Home_screen::class.java))
                        activity!!.finish()
                    }
                    else{
                        Toast.makeText(activity, "You did not verify your Email", Toast.LENGTH_LONG)
                            .show()
//                        progressBar.visibility=View.GONE
                    }

                } else {
                    Toast.makeText(
                        activity,
                        "Error ! " + task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
//                    progressBar.setVisibility(View.GONE)
                }
            }
        }
        catch (e:Exception)
        {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }
    fun forget(){
        val  fAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val forgotTextLink: TextView = root!!.findViewById(R.id.forgotPassword)
        forgotTextLink.setOnClickListener(View.OnClickListener { v ->
            val resetMail = EditText(v.context)
            val passwordResetDialog = AlertDialog.Builder(v.context)
            passwordResetDialog.setTitle("Reset Password ?")
            passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.")
            passwordResetDialog.setView(resetMail)
            passwordResetDialog.setPositiveButton(
                "Yes"
            ) { dialog, which -> // extract the email and send reset link
                val mail = resetMail.text.toString()
                if (resetMail.text.toString().isEmpty()) {
                    resetMail.error = "Email is Required."
                    Toast.makeText(activity,"Error: Email is Required.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                else{
                    fAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                        Toast.makeText(
                            activity,
                            "Reset Link Sent To Your Email.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener { e ->
                        Toast.makeText(
                            activity,
                            "Error ! Reset Link is Not Sent" + e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            passwordResetDialog.setNegativeButton(
                "No"
            ) { dialog, which ->
                // close the dialog
            }
            passwordResetDialog.create().show()
        })
    }
    fun userlog(){
        var btnEnable=true
        val logButton: Button =root!!.findViewById(R.id.loginbtn)
        logButton.setOnClickListener(View.OnClickListener {
            if(btnEnable)
            {
                btnEnable=false
                createuserlog()
            }
            Handler().postDelayed({
                btnEnable=true
            },1000)
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root=inflater.inflate(R.layout.activity_login_fragment,container,false)
        userlog()
        forget()
        return root
    }

}