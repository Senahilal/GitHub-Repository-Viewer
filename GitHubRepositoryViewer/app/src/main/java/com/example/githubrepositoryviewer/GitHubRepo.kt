//GitHubRepo.kt (Data Model)
package com.example.githubrepositoryviewer

data class GitHubRepo(
    val id: Int,
    val name: String,
    val description: String?,
    val html_url: String
)