package com.example.scientificcalculator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextEditor(navController: NavHostController, noteIndex: Int? = null ) {
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val formatter = remember { TextFormatter() }

    LaunchedEffect(Unit) {
      if (noteIndex != null && noteIndex < NoteRepository.notes.size) {
            text = NoteRepository.notes[noteIndex]
        }
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("notepadList") }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = { Text("Text Editor") },
                actions = {
                    IconButton(onClick = {
                        if (text.isNotBlank()) {
                            if (noteIndex == null) {
                                NoteRepository.notes.add(text)
                            } else {
                                NoteRepository.notes[noteIndex] = text
                            }
                            navController.navigate("notepadList")
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.save),
                            contentDescription = "Save"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.imePadding(),
                actions = {
                    IconButton(onClick = { formatter.toggleBold() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.format_bold),
                            contentDescription = "Bold"
                        )
                    }
                    IconButton(onClick = { formatter.toggleItalic() }) {
                        Icon(
                            painter = painterResource(R.drawable.format_italic),
                            contentDescription = "Italic"
                        )
                    }
                    IconButton(onClick = { formatter.toggleUnderline() }) {
                        Icon(
                            painter = painterResource(R.drawable.format_underlined),
                            contentDescription = "Underline"
                        )
                    }
                    IconButton(onClick = { formatter.increaseTextSize() }) {
                        Icon(
                            painter = painterResource(R.drawable.text_increase),
                            contentDescription = "Text Increase"
                        )
                    }
                    IconButton(onClick = { formatter.decreaseTextSize() }) {
                        Icon(
                            painter = painterResource(R.drawable.text_decrease),
                            contentDescription = "Text Decrease"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Type here...") },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .focusRequester(focusRequester),
            singleLine = false,
            textStyle = TextStyle(
                fontWeight = if (formatter.isBold) FontWeight.Bold else FontWeight.Normal,
                fontStyle = if (formatter.isItalic) FontStyle.Italic else FontStyle.Normal,
                textDecoration = if (formatter.isUnderlined) TextDecoration.Underline else TextDecoration.None,
                fontSize = formatter.textSize.sp
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotepadList(navController: NavHostController) {
    val notes = NoteRepository.notes

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("menu") }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text("Text Editor") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("textEditor") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("+", color = MaterialTheme.colorScheme.onPrimary, fontSize = 24.sp)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp)
        ) {
            if (notes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No notes yet", color = MaterialTheme.colorScheme.onBackground)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(notes.size) { index ->
                        val note = notes[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline,
                                    MaterialTheme.shapes.medium
                                )
                                .clickable {
                                    navController.navigate("textEditor/$index")
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Text(
                                text = note,
                                modifier = Modifier.padding(16.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}


object NoteRepository {
    val notes = mutableStateListOf<String>()
}