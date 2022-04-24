package edu.itesm.gastos.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import edu.itesm.gastos.R
import edu.itesm.gastos.dao.GastoDao
import edu.itesm.gastos.database.GastoApp
import edu.itesm.gastos.database.GastosDB
import edu.itesm.gastos.databinding.ActivityMainBinding
import edu.itesm.gastos.entities.Gasto
import edu.itesm.gastos.mvvm.MainActivityViewModel
import edu.itesm.perros.adapter.GastosAdapter
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var gastoDao: GastoDao
    private lateinit var  gastos: List<Gasto>
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: GastosAdapter
    private lateinit var viewModel : MainActivityViewModel

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
    }
    private fun initRecycler(){
        gastos = mutableListOf<Gasto>()
        adapter = GastosAdapter(gastos)
        binding.gastos.layoutManager = LinearLayoutManager(this)
        binding.gastos.adapter = adapter
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        //es para que se actulicen  los cambios
        viewModel.getLiveDataObserver().observe(this, Observer {
            if(!it.isEmpty()){
                adapter.setGastos(it)
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.getGastos(gastoDao)
    }
}