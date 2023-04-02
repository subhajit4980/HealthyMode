@file:Suppress("UNREACHABLE_CODE")

package com.example.HealthyMode.UI.Home_fragment
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.HealthyMode.Utils.Constant
import com.example.HealthyMode.databinding.FragmentStatsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.Collections.max
import java.util.Collections.min
@RequiresApi(Build.VERSION_CODES.O)
class Stats : Fragment() {
    private var userDitails: DocumentReference =  Firebase.firestore.collection("user").document(
        FirebaseAuth.getInstance().currentUser!!.uid)
    private lateinit var _binding: FragmentStatsBinding
    private val binding get() = _binding!!
    private  var stats: String="0:0:0:0:0?0:0:0:0:0?0:0:0:0:0?0:0:0:0:0"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentStatsBinding.inflate(inflater,container,false)
        ui()
        return binding.root
    }
    private fun ui(){
          getDataFromSharedPreference()
          val splitParts = stats.split("?")
          setBloodPressure(splitParts[0], splitParts)
          setSugarFasting(splitParts[1], splitParts)
          setSugarPP(splitParts[2], splitParts)
          setCholesterol(splitParts[3], splitParts)
    }
    private fun setBloodPressure(Concat: String, splitParts: List<String>) {
        val splitBloodPressure = Concat.split(":")
        val bloodPressureList: ArrayList<Int> = ArrayList()
        for (i in 0..4) {
            bloodPressureList.add(Integer.parseInt(splitBloodPressure[i]))
        }
        val bloodPressureMin = min(bloodPressureList)
        val bloodPressureMax = max(bloodPressureList)
        binding.bloodPressureRange.text = "Min: $bloodPressureMin, Max: $bloodPressureMax"
        binding.bloodPressure.setData(bloodPressureList)
        binding.addBloodPressureData.setOnClickListener {
            addNewData(0, bloodPressureList, splitParts)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setSugarFasting(Concat: String, splitParts: List<String>) {
        val splitSugarFasting = Concat.split(":")
        val sugarFastingList: ArrayList<Int> = ArrayList()
        for (i in 0..4) {
            sugarFastingList.add(Integer.parseInt(splitSugarFasting[i]))
        }
        val sugarFastingMin = min(sugarFastingList)
        val sugarFastingMax = max(sugarFastingList)
        binding.sugarFastingRange.text = "Min: $sugarFastingMin, Max: $sugarFastingMax"
        binding.sugarFasting.setData(sugarFastingList)
        binding.addSugarFastingData.setOnClickListener {
            addNewData(1, sugarFastingList, splitParts)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setSugarPP(Concat: String, splitParts: List<String>) {
        val splitSugarPP = Concat.split(":")
        val sugarPPList: ArrayList<Int> = ArrayList()
        for (i in 0..4) {
            sugarPPList.add(Integer.parseInt(splitSugarPP[i]))
        }
        val sugarPPMin = min(sugarPPList)
        val sugarPPMax = max(sugarPPList)
        binding.sugarPPRange.text = "Min: $sugarPPMin, Max: $sugarPPMax"
        binding.sugarPP.setData(sugarPPList)

        binding.addSugarPPData.setOnClickListener {
            addNewData(2, sugarPPList, splitParts)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCholesterol(Concat: String, splitParts: List<String>) {
        val splitCholesterol = Concat.split(":")
        val cholesterolList: ArrayList<Int> = ArrayList()
        for (i in 0..4) {
            cholesterolList.add(Integer.parseInt(splitCholesterol[i]))
        }
        val cholesterolMin = min(cholesterolList)
        val cholesterolMax = max(cholesterolList)
        binding.cholesterolRange.text = "Min: $cholesterolMin, Max: $cholesterolMax"
        binding.cholesterol.setData(cholesterolList)

        binding.addCholesterolData.setOnClickListener {
            addNewData(3, cholesterolList, splitParts)
        }
    }

    private fun addNewData(ind: Int, List: ArrayList<Int>, splitParts: List<String>) {
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(requireActivity())
        builder.setTitle("Title")
        // Set up the input
        val input = EditText(requireActivity())
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.hint = "Enter new data"
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)
        // Set up the buttons
        builder.setPositiveButton("Set", DialogInterface.OnClickListener { _, _ ->
            // Here you get get input text from the Edittext
            var newDataValue = input.text.toString().trim()
            val queue: LinkedList<Int> = LinkedList(List)
            queue.poll()
            queue.offer(Integer.parseInt(newDataValue))
            List.clear()
            for (i in 0..4) {
                List.add(queue.poll())
            }
            val merged = mergedData(ind, List, splitParts)
            userDitails.collection("fitness").document("health_report").set(hashMapOf("data" to merged))
           Constant.savedata(requireContext(),"health","stats",merged.toString())
            ui()
        })
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()

    }
    private fun mergedData(i: Int, List: ArrayList<Int>, splitParts: List<String>): String? {
        val tempString: StringBuilder = StringBuilder()
        for (j in 0..4) {
            tempString.append(List[j]).append(":")
        }
//        tempString.setLength(tempString.length - 1)
        val finalStats: StringBuilder = StringBuilder()
        for (j in 0..3) {
            if (j == i) {
                finalStats.append(tempString).append("?")
            } else {
                finalStats.append(splitParts[j]).append("?")
            }
        }
//        finalStats.setLength(finalStats.length - 1)
        return finalStats.toString()
    }
    @SuppressLint("SetTextI18n")
    private fun getDataFromSharedPreference() {
        stats= Constant.loadData(
            requireActivity(),
            "health",
            "stats",
            "0:0:0:0:0?0:0:0:0:0?0:0:0:0:0?0:0:0:0:0"
        ).toString()

    }
}








