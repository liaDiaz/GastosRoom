package edu.itesm.gastos.dao

import androidx.room.*
import edu.itesm.gastos.entities.Gasto
import kotlinx.coroutines.flow.Flow

//define una lista de fucniones que se puedan implentar despues
//DAO DATA ACCESS OBJECT ES UN OBJECTO PARA PODER HACER CONSULTAS GET, POST,DELETE
@Dao
interface GastoDao{

    @Query("Select * from Gasto")
    //suspende para correr en un hilo aparte de las corrutinas, pero debe hacer una fucnion para que se actulice y se se lo quita corre en la corrutina de froma continua haciendo que se actulice automaticamente
     fun getAllGastos(): Flow<List<Gasto>>

    //"SELECT SUM(monto) from Gasto"

    //"Insert
    @Insert
    suspend fun insertaGasto(gasto: Gasto){

    }

}

