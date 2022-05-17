package edu.itesm.gastos.mvvm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import edu.itesm.gastos.dao.GastoDao
import edu.itesm.gastos.database.GastosDB
import edu.itesm.gastos.entities.Gasto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.random.Random


class MainActivityViewModel(private val gastoDao: GastoDao) : ViewModel(){

    //lo que hace el flow
    fun getGastosFlujo(): Flow<List<Gasto>> = gastoDao.getAllGastos()
    /* var liveData: MutableLiveData<List<Gasto>>
    init {
        liveData = MutableLiveData()
    }

        //es mmejor regresar la lista que el objecto que contine la lista
    fun getLiveDataObserver(): MutableLiveData<List<Gasto>>{
        return liveData
    }

    fun getGastos(gastoDao: GastoDao){
        //dao inserta pero todos se corre en la coorutina
        CoroutineScope(Dispatchers.IO).launch {
            for (i in 0..100){
                gastoDao.insertaGasto(Gasto(0,"Gasto ${i}", Random.nextDouble()*100))

            }
            //esto le dice al live data que actualice y ponga todos los gastos de la live data
            liveData.postValue(gastoDao.getAllGastos())

        }


    }*/

    class MainActivityViewModelFactory(private val gastoDao: GastoDao):
        ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
                return MainActivityViewModel(gastoDao) as T
            }
            throw IllegalArgumentException("Clase de MVVM no conocida")
        }

    }



}