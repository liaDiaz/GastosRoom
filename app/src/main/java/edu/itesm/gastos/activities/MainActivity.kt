package edu.itesm.gastos.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import edu.itesm.gastos.CapturaGastoActivity
import edu.itesm.gastos.R
import edu.itesm.gastos.dao.GastoDao
import edu.itesm.gastos.database.GastoApp
import edu.itesm.gastos.database.GastosDB
import edu.itesm.gastos.databinding.ActivityMainBinding
import edu.itesm.gastos.entities.Gasto
import edu.itesm.gastos.entities.GastoFireBase
import edu.itesm.gastos.mvvm.MainActivityViewModel
import edu.itesm.perros.adapter.GastosAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.FieldPosition
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var gastoDao: GastoDao
    private lateinit var  gastos: List<Gasto>
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: GastosAdapter
    private lateinit var viewModel : MainActivityViewModel
    //va a la base de datos y busca la coleccion "gastos y si no exite la crea
    private val databaseReference= Firebase.database.getReference("gastos")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //se construye la base de datos
        val db = Room.databaseBuilder(this@MainActivity, GastosDB::class.java, "gastos").build()
        // se obtiene el do
        gastoDao = db.gastoDB()
        initRecycler()
        initViewModel()
        fabAgregarDatos()
    }
    private fun initRecycler(){
        gastos = mutableListOf<Gasto>()
        adapter = GastosAdapter(gastos)
        binding.gastos.layoutManager = LinearLayoutManager(this)
        binding.gastos.adapter = adapter
        binding.gastos.setListener(object : SwipeLeftRightCallback.Listener{
            override fun onSwipedLeft(position: Int) {
                removeGasto(position)
            }

            override fun onSwipedRight(position: Int) {
                binding.gastos.adapter?.notifyDataSetChanged()
            }

        })
    }

    private fun removeGasto(position: Int){
        val gasto = adapter.getGasto(position)
        databaseReference.database.getReference("gastos").child(gasto.id.toString()).removeValue().addOnSuccessListener {
            Toast.makeText(baseContext,"Borrado de la base de datos",Toast.LENGTH_SHORT).show()
            //el notify es para recargar la vista
            adapter.notifyDataSetChanged()
        }.addOnFailureListener {
            Toast.makeText(baseContext,"FALLA",Toast.LENGTH_SHORT).show()
        }
    }

    private fun initViewModel(){
       /* viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        //es para que se actulicen  los cambios
        viewModel.getLiveDataObserver().observe(this, Observer {
            if(!it.isEmpty()){
                adapter.setGastos(it)
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.getGastos(gastoDao)*/

        /*val mainActivityViewModelFacyory = MainActivityViewModel.MainActivityViewModelFactory(gastoDao)
        viewModel = ViewModelProvider(this, mainActivityViewModelFacyory).get(MainActivityViewModel::class.java)

        lifecycle.coroutineScope.launch {
            viewModel.getGastosFlujo().collect(){ gastos->
                adapter.setGastos(gastos)
                adapter.notifyDataSetChanged()

            }
        }*/
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var lista = mutableListOf<Gasto>()
                //los children seranlos que estoy obteniendo
                for(gastoObject in snapshot.children){
                    val objeto = gastoObject.getValue(GastoFireBase::class.java)
                    lista.add(Gasto(objeto!!.id.toString(),objeto!!.description!!,objeto!!.monto!!))

                }
                adapter.setGastos(lista)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private val agregaDatosLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            resultado->
        if (resultado.resultCode== RESULT_OK){
            val gasto = resultado.data?.getSerializableExtra("gasto") as Gasto
            Toast.makeText(baseContext,gasto.description,Toast.LENGTH_SHORT).show()
        }
    }



    private fun fabAgregarDatos(){

        //a que activity voy, en donde estoy y hago el intento
        binding.fab.setOnClickListener {
            /*val intento = Intent(baseContext, CapturaGastoActivity::class.java)

            agregaDatosLauncher.launch(intent)*/

            GastoCapturaDialog(onSudmintClickListener = { gasto ->
               //llamar al DAO
                Toast.makeText(baseContext,gasto.description,Toast.LENGTH_SHORT).show()
                /*//gastoDao.insertaGasto(gasto)
               CoroutineScope(Dispatchers.IO).launch {
                   gastoDao.insertaGasto(gasto)
               }*/

                val id = databaseReference.push().key!!
                val gastoFb = GastoFireBase(id,gasto.description,gasto.monto)
                //agrege un hijo utilizado ese ID

                databaseReference.child(id).setValue(gastoFb).addOnSuccessListener {
                    Toast.makeText(baseContext, "agregado", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(baseContext, "error", Toast.LENGTH_SHORT).show()
                }

            }).show(supportFragmentManager, "")
        }

    }
}