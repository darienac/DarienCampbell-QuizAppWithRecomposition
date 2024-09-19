package com.example.dariencampbell_quizappwithrecomposition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dariencampbell_quizappwithrecomposition.ui.theme.DarienCampbellQuizAppWithRecompositionTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DarienCampbellQuizAppWithRecompositionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppLayout(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AppLayout(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val questions = arrayOf(
        "What is 1+1?",
        "Which ocean is the largest?",
        "How many continents are there?"
    )
    val answers = arrayOf(
        "2",
        "Pacific",
        "7"
    )

    var answer by remember {mutableStateOf("")}
    var currentQuestionIdx by remember {mutableIntStateOf(0)}
    var currentQuestion by remember {mutableStateOf("")}
    var quizIsComplete by remember {mutableStateOf(false)}
    currentQuestion = questions[currentQuestionIdx]


    Scaffold(modifier = Modifier.fillMaxSize(), snackbarHost={SnackbarHost(hostState=snackbarHostState)}) { innerPadding ->
        Column(modifier = Modifier.padding(16.dp).padding(innerPadding).displayCutoutPadding().safeDrawingPadding(), horizontalAlignment = Alignment.CenterHorizontally) {
            if (quizIsComplete) {
                Button(onClick={
                    answer = ""
                    currentQuestionIdx = 0
                    quizIsComplete = false
                }) {
                    Text("Restart")
                }
            } else {
                ElevatedCard(modifier=Modifier.weight(1f).padding(0.dp, 0.dp, 0.dp, 16.dp)) {
                    Row(verticalAlignment=Alignment.CenterVertically, modifier=Modifier.fillMaxSize()) {
                        Text(text=currentQuestion, textAlign=TextAlign.Center, modifier=Modifier.fillMaxWidth())
                    }
                }
                Row(verticalAlignment=Alignment.CenterVertically) {
                    OutlinedTextField(value=answer, onValueChange={answer = it}, label={Text("Answer")}, modifier=Modifier.weight(1f).padding(0.dp, 0.dp, 16.dp, 0.dp))
                    Button(onClick={
                        if (answer == answers[currentQuestionIdx]) {
                            answer = ""
                            if (currentQuestionIdx+1 >= answers.size) {
                                quizIsComplete = true
                                currentQuestionIdx = 0
                                scope.launch {
                                    snackbarHostState.showSnackbar("Quiz Complete! Restart?", withDismissAction=true)
                                }
                            } else {
                                currentQuestionIdx++
                                scope.launch {
                                    snackbarHostState.showSnackbar("Correct!", withDismissAction=true)
                                }
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Incorrect!", withDismissAction=true)
                            }
                        }
                    }) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DarienCampbellQuizAppWithRecompositionTheme {
        AppLayout()
    }
}