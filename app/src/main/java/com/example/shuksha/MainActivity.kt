package com.example.shuksha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shuksha.presentation.RecipeCard
import com.example.shuksha.viewmodel.RecipeViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContent {

            val vm: RecipeViewModel =
                viewModel()

            LaunchedEffect(Unit) {
                vm.loadRecipes()
            }

            LazyColumn {

                items(vm.meals) { meal ->

                    RecipeCard(meal)
                }
            }
        }
    }
}