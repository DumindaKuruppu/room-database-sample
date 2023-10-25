package com.example.roomdatabasesample

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdatabasesample.databinding.ActivityMainBinding
import com.example.roomdatabasesample.databinding.DialogUpdateBinding
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
            val itemAdapter = ItemAdapter(employeesList,
                { updateId ->
                    updateEmployeeRecordDialog(updateId, employeeDao)
                },
                { deleteId ->
                    deleteEmployeeRecordDialog(deleteId, employeeDao)
                }

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

    private fun updateEmployeeRecordDialog(id: Int, employeeDao: EmployeeDao) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        val binding = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        lifecycleScope.launch {
            employeeDao.getEmployeeById(id).collect {
                if (it != null) {
                    binding.etName.setText(it.name)
                    binding.etEmail.setText(it.email)
                }
            }
        }

        binding.tvUpdate.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()


            if (name.isNotEmpty() && email.isNotEmpty()) {
                lifecycleScope.launch {
                    employeeDao.updateEmployee(EmployeeEntity(id, name, email))
                    Toast.makeText(applicationContext, "Record Updates", Toast.LENGTH_SHORT).show()
                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Name or email cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()
    }

    private fun deleteEmployeeRecordDialog(id: Int, employeeDao: EmployeeDao) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Employee")

        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                employeeDao.deleteEmployee(EmployeeEntity(id))
                Toast.makeText(applicationContext, "Deleted Successfully !", Toast.LENGTH_SHORT)
                    .show()

            }
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
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