package com.example.roomdatabasesample

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// DAO stands for "Data Access Object"

// DAOs are responsible for defining the methods that access the database.
// Room uses the DAO to create a clean API for your code.
// The DAO must be an interface or abstract class.
// By default, all queries must be executed on a separate thread.
// Room has coroutines support, allowing your queries to be annotated with the suspend modifier
// and then called from a coroutine or from another suspension function.
// Room provides compile-time checks of SQLite statements.
// Your queries are checked against your database schema, so if you make a mistake like a typo,
// you get a compile-time error instead of a runtime failure.
// Room also validates SQLite queries.
// If there is a problem with the query itself (like a syntax error),
// you get a compile-time error.
// Room supports LiveData, Flow, and RxJava for data observation,
// and it includes a SQLite query compiler that checks your SQLite queries at compile time.

@Dao
interface EmployeeDao {
    @Insert
    suspend fun insertEmployee(employeeEntity: EmployeeEntity)

    @Update
    suspend fun updateEmployee(employeeEntity: EmployeeEntity)

    @Delete
    suspend fun deleteEmployee(employeeEntity: EmployeeEntity)

    @Query("SELECT * FROM `employee-table`")
    fun getAllEmployees(): Flow<List<EmployeeEntity>>

    @Query("SELECT * FROM `employee-table` WHERE id = :id")
    fun getEmployeeById(id: Int): Flow<EmployeeEntity>
}