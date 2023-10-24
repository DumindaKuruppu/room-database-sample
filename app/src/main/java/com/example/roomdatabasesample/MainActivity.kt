package com.example.roomdatabasesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdatabasesample.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
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

        lifecycleScope.launch {
            employeeDao.getAllEmployees().collect {
                val list = ArrayList(it)
                getEmployeeDataListToRecyclerView(list, employeeDao)
            }
        }
    }

    private fun getEmployeeDataListToRecyclerView(
        employeesList: ArrayList<EmployeeEntity>,
        employeeDao: EmployeeDao
    ) {
        if (employeesList.isNotEmpty()) {
            val itemAdapter = ItemAdapter(
                employeesList
            )

            binding?.rvEmployeeList?.layoutManager = LinearLayoutManager(this)
            binding?.rvEmployeeList?.adapter = itemAdapter
            binding?.rvEmployeeList?.visibility = android.view.View.VISIBLE
            binding?.tvEmptyRecordsError?.visibility = android.view.View.GONE
        } else {
            binding?.rvEmployeeList?.visibility = android.view.View.GONE
            binding?.tvEmptyRecordsError?.visibility = android.view.View.VISIBLE
        }


    }

    private fun addRecord(employeeDao: EmployeeDao) {
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