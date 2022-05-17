package edu.itesm.gastos.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import edu.itesm.gastos.databinding.ActivityCapturaGastoBinding
import edu.itesm.gastos.entities.Gasto

class GastoCapturaDialog(private  val onSudmintClickListener: (Gasto)->Unit):DialogFragment(){

    //se recultilza l activity anterioer
    private lateinit var binding: ActivityCapturaGastoBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = ActivityCapturaGastoBinding.inflate(LayoutInflater.from(context))
        // es una ventanan de alert dialgog y lo de cosntruct es para que se vea
        val constructor = AlertDialog.Builder(requireActivity())
        constructor.setView(binding.root)
        binding.agregaGasto.setOnClickListener {
            val concepto = binding.conceptoGastoCifra.text.toString()
            val monto = binding.montoGastoCifra.text.toString().toDouble()
            val gasto = Gasto("",concepto,monto)

            onSudmintClickListener.invoke(gasto)
            //se mate la accion
            dismiss()
        }

        val dialog = constructor.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }

}