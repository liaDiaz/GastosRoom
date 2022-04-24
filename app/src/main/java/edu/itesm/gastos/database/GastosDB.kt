package edu.itesm.gastos.database

import android.os.Build
import androidx.room.Database
import androidx.room.RoomDatabase
import edu.itesm.gastos.dao.GastoDao
import edu.itesm.gastos.entities.Gasto

//el que va conectar la parte de la aplicaion y creara la base de datos

//se crea las tablas
// da de data las entidades
 @Database(entities = [Gasto::class ], version = 1)
// hace referencia a las
abstract class GastosDB: RoomDatabase(){
    abstract fun gastoDB(): GastoDao
}




