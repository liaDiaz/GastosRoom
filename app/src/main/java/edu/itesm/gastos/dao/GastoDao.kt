package edu.itesm.gastos.dao

import androidx.room.*
import edu.itesm.gastos.entities.Gasto
//define una lista de fucniones que se puedan implentar despues
@Dao
interface GastoDao{

    @Query("Select * from Gasto")
    //suspende para correr en un hilo aparte
    suspend fun getAllGastos(): List<Gasto>

    //"SELECT SUM(monto) from Gasto"

    //"Insert
    @Insert
    suspend fun insertaGasto(gasto: Gasto){

    }

}

