package com.example.roomdatabasesample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// A database class must be annotated with a @Database annotation that includes an entities array
// that lists all of the data entities associated with the database.
// Each entity corresponds to a table that will be created in the database.
// The database class must be an abstract class that extends RoomDatabase.
// Usually, you only need one instance of a Room database for the whole app.
// So, it's a singleton.
// To create a singleton, annotate the class with @Database and use the annotation parameter
// entities to list all the entities in the database.
// You can also use the annotation parameter version to define a version number for your database.
// Each entity corresponds to a table that will be created in the database.

@Database(entities = [EmployeeEntity::class], version = 1)
abstract class EmployeeDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao

    // The companion object allows clients to access the methods for creating or getting the database
    // without instantiating the class.
    // Since the only purpose of this class is to provide a database,
    // there is no reason to ever instantiate it.
    companion object {

        // Volatile means that writes to this field are immediately made visible to other threads.
        // A value of a volatile field becomes visible to all readers (other threads in particular)
        // after a write operation completes on it.
        // Without volatile, this code may not work as expected:
        @Volatile
        private var INSTANCE: EmployeeDatabase? = null

        fun getInstance(context: Context): EmployeeDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EmployeeDatabase::class.java,
                        "employee_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}