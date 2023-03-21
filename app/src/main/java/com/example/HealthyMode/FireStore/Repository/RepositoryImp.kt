package com.example.HealthyMode.FireStore.Repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.HealthyMode.Utils.UIstate
import com.example.HealthyMode.data_Model.weight
import com.github.mikephil.charting.data.Entry
import com.google.firebase.firestore.DocumentReference
import java.time.LocalDate

class RepositoryImp(
    private val database: DocumentReference
) : Repository {
    override fun getWeight(result: (UIstate<ArrayList<Entry>>) -> Unit) {
        database.collection("Weight track").get().addOnSuccessListener {
            val entries = ArrayList<Entry>()
            for (document in it) {
                val date = document.get("date")
                val weight = document.get("weight")?.toString()
                val getm = date.toString().substring(5, 7)
                val day = date.toString().substring(8, 10)
                if (weight != null) {
                    entries.add(
                        Entry(
                            day.toFloat(),
                            weight.toFloat()
                        )
                    )
                }
            }
            result.invoke(
                UIstate.Success(entries)
            )
        }.addOnFailureListener {
            result.invoke(
                UIstate.Failure(it.localizedMessage)
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun changedWeight(weight: weight, result: (UIstate<String>) -> Unit){
        database.collection("Weight track").document(LocalDate.now().toString())
            .set(weight)
            .addOnSuccessListener {
                result.invoke(
                    UIstate.Success("weight changed successfully")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UIstate.Failure(
                        it.localizedMessage
                    )
                )
            }
    }


}