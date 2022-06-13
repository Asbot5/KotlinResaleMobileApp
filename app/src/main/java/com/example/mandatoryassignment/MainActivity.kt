package com.example.mandatoryassignment

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.mandatoryassignment.databinding.ActivityMainBinding
import com.example.mandatoryassignment.models.Item
import com.example.mandatoryassignment.models.ItemsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val itemsViewModel: ItemsViewModel by viewModels()

    private fun sortPriceAscending() {
        itemsViewModel.sortPriceAscending()
    }

    private fun sortDateAscending() {
        itemsViewModel.sortDateAscending()
    }
  /* private fun filterPriceAscending() {
        itemsViewModel.filterPriceAscending()
    } */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


        binding.fab.visibility = View.GONE
        binding.fab.refreshDrawableState()

        if(Firebase.auth.currentUser != null)
        {
            binding.fab.visibility = View.VISIBLE
            binding.fab.setOnClickListener { view ->
                showDialog()
            }
        }

        binding.sortByDate.setOnClickListener { view ->
            sortDateAscending()
        }

      binding.sortByPrice.setOnClickListener { view ->
            sortPriceAscending()
        }

        binding.filterByPrice.setOnClickListener {
            filterDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        val loginButton = menu.findItem(R.id.action_login)
        val logoutButton = menu.findItem(R.id.action_logout)

        invalidateOptionsMenu()

        if(Firebase.auth.currentUser == null)
        {
            logoutButton.setVisible(false)
        }
        else
        {
            loginButton.setVisible(false)
        }
        return true
    }

    override fun onOptionsItemSelected(option: MenuItem): Boolean {
        if(option.itemId == R.id.action_logout)
        {
            Firebase.auth.signOut()
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_MainActivity_to_FirstFragment)
        }

        if(option.itemId == R.id.action_login)
        {
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_MainActivity_to_LoginFragment)
        }

        return super.onOptionsItemSelected(option)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun filterDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Filter price")

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val input1 = EditText(this)
        input1.setHint("min price")
        input1.inputType = InputType.TYPE_CLASS_NUMBER
        layout.addView(input1)

        val input2 = EditText(this)
        input2.setHint("max price")
        input2.inputType = InputType.TYPE_CLASS_NUMBER
        layout.addView(input2)

        builder.setView(layout)

        builder.setPositiveButton(
            "Update Now"
        ) { dialog, id ->

            val strmin = input1.text.toString().trim()
            val strmax = input2.text.toString().trim()

            when {
                strmin.isEmpty() ->
                    //inputField.error = "No word"
                    Snackbar.make(binding.root, "No min", Snackbar.LENGTH_LONG).show()
                strmax.isEmpty() -> Snackbar.make(binding.root, "No max", Snackbar.LENGTH_LONG)
                    .show()
                else -> {
                    val min = strmin.toInt()
                    val max = strmax.toInt()

                    if (min < max) {

                        Toast.makeText(
                            this,
                            "min price: {$min}, max price: {$max}",
                            Toast.LENGTH_SHORT
                        ).show()

                        itemsViewModel.filterPriceAscending(min, max)
                    }else
                    {
                        Toast.makeText(
                            this,
                            "max price has to be higher than min price",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        //set positive button
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, id ->
            // User cancelled the dialog
        }

        builder.show()

    }

    private fun showDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Add Item")

        val layout = LinearLayout(this@MainActivity)
        layout.orientation = LinearLayout.VERTICAL

        val titleInputField = EditText(this)
        titleInputField.hint = "Title"
        titleInputField.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(titleInputField)

        val priceInputField = EditText(this)
        priceInputField.hint = "Price"
        priceInputField.inputType = InputType.TYPE_CLASS_NUMBER
        layout.addView(priceInputField)

        val dateInputField = EditText(this)
        dateInputField.hint = "Date"
        dateInputField.inputType = InputType.TYPE_CLASS_NUMBER
        layout.addView(dateInputField)

        val descriptionInputField = EditText(this)
        descriptionInputField.hint = "Description"
        descriptionInputField.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(descriptionInputField)

        builder.setView(layout)

        builder.setPositiveButton("OK") { dialog, which ->
            val title = titleInputField.text.toString().trim()
            val priceStr = priceInputField.text.toString().trim()
            val dateStr = dateInputField.text.toString().trim()
            val descriptionStr = descriptionInputField.text.toString().trim()
            val sellerStr = Firebase.auth.currentUser?.email.toString().trim()
            when {
                //inputField.error = "No word"
                title.isEmpty() ->
                    Snackbar.make(binding.root, "No title", Snackbar.LENGTH_LONG).show()
                priceStr.isEmpty() ->
                    Snackbar.make(binding.root,"No price", Snackbar.LENGTH_LONG).show()
                dateStr.isEmpty() ->
                    Snackbar.make(binding.root,"No Date", Snackbar.LENGTH_LONG).show()
                descriptionStr.isEmpty() ->
                    Snackbar.make(binding.root,"No Description", Snackbar.LENGTH_LONG).show()
               else -> {
                    val item = Item(title, priceStr.toInt(), dateStr.toInt(), descriptionStr, sellerStr)
                    itemsViewModel.add(item)
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

}