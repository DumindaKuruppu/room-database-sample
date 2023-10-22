package com.example.roomdatabasesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.roomdatabasesample.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val employeeDao = (application as EmployeeApp).database.employeeDao()

        binding?.btnAdd?.setOnClickListener {
            addRecord(employeeDao)
        }
    }

    fun addRecord(employeeDao: EmployeeDao) {
        val name = binding?.etName?.text.toString()
        val email = binding?.etEmail?.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty()) {
            lifecycleScope.launch { // launch a new coroutine in background and continue
                employeeDao.insertEmployee(EmployeeEntity(name = name, email = email))
                Toast.makeText(applicationContext, "Record Saved", Toast.LENGTH_SHORT).show()
                binding?.etName?.setText("")
                binding?.etEmail?.setText("")
            }
        } else {
            Toast.makeText(applicationContext, "Please fill all the fields", Toast.LENGTH_SHORT)
                .show()
        }

    }
}