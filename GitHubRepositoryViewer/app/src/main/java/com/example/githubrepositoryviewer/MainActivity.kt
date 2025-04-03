package com.example.githubrepositoryviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.githubrepositoryviewer.ui.theme.GitHubRepositoryViewerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitHubRepositoryViewerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    RepoSearchScreen()
                }
            }
        }
    }
}

@Composable
fun RepoSearchScreen(viewModel: RepoViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    val state by viewModel.repoState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.height(20.dp))

        // Search Bar
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Enter GitHub Username") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                cursorColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Search button
        Button(
            onClick = { viewModel.searchRepos(username) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(15.dp))

        // based on state change what to show
        // credit to lecture examples
        when (state) {
            is RepoViewModel.RepoState.Initial -> {}
            is RepoViewModel.RepoState.Loading -> {
                CircularProgressIndicator()
            }
            is RepoViewModel.RepoState.Error -> {
                Text("Error: ${(state as RepoViewModel.RepoState.Error).message}")
            }
            is RepoViewModel.RepoState.Success -> {
                val repos = (state as RepoViewModel.RepoState.Success).repos
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(repos) { repo ->
                        RepoItem(repo = repo)
                    }
                }

                Button(
                    onClick = { viewModel.loadMoreRepos() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )

                ) {
                    Text("Load More")
                }
            }
        }
    }
}

@Composable
fun RepoItem(repo: GitHubRepo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xff413252), shape = MaterialTheme.shapes.medium) // background color

            .border(
                width = 1.dp,
                color = Color(0xFF4D3D60),
                shape = MaterialTheme.shapes.medium
            )
            .padding(25.dp)
    ) {
        Text(text = repo.name, style = MaterialTheme.typography.titleMedium, color = Color.White)
        Text(text = repo.description ?: "No description", color = Color.White)
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GitHubRepositoryViewerTheme {
    }
}