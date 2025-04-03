//RepoViewModel.kt
package com.example.githubrepositoryviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RepoViewModel : ViewModel() {

    private val _repoState = MutableStateFlow<RepoState>(RepoState.Initial)
    val repoState: StateFlow<RepoState> = _repoState

    private var currentPage = 1
    private var currentUsername = ""

    // all the repositories that have been loaded
    private val loadedRepos = mutableListOf<GitHubRepo>()

    //calling this when the user searches for a GitHub username
    fun searchRepos(username: String) {
        viewModelScope.launch {
            _repoState.value = RepoState.Loading
            try {

                currentUsername = username
                currentPage = 1
                val repos = ApiClient.apiService.getUserRepos(username, page = currentPage)

                //clear previous results
                loadedRepos.clear()

                //add new ones
                loadedRepos.addAll(repos)
                _repoState.value = RepoState.Success(loadedRepos.toList())
            } catch (e: Exception) {
                _repoState.value = RepoState.Error(e.message ?: "Unknown error")
            }
        }
    }

    //calling this when the user clicks load more button
    fun loadMoreRepos() {
        viewModelScope.launch {
            try {
                currentPage++
                val moreRepos = ApiClient.apiService.getUserRepos(currentUsername, page = currentPage)
                loadedRepos.addAll(moreRepos)
                _repoState.value = RepoState.Success(loadedRepos.toList())
            } catch (e: Exception) {
                _repoState.value = RepoState.Error(e.message ?: "Failed to load more repos")
            }
        }
    }

    sealed class RepoState {
        object Initial : RepoState()
        object Loading : RepoState()
        data class Success(val repos: List<GitHubRepo>) : RepoState()
        data class Error(val message: String) : RepoState()
    }
}