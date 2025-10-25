package com.mobizonetech.aeon_wallet_cursor.utils

object RandomNameGenerator {
    private val firstNames = listOf(
        "Alex", "Jordan", "Taylor", "Casey", "Morgan", "Riley", "Avery", "Quinn",
        "Blake", "Cameron", "Drew", "Emery", "Finley", "Hayden", "Jamie", "Kendall",
        "Logan", "Parker", "Reese", "Sage", "Skyler", "Sydney", "Tatum", "River",
        "Phoenix", "Rowan", "Sawyer", "Aspen", "Brooklyn", "Dakota", "Harper", "Indigo"
    )
    
    private val lastNames = listOf(
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
        "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas",
        "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson", "White",
        "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Walker", "Young"
    )
    
    fun generateRandomName(): String {
        val firstName = firstNames.random()
        val lastName = lastNames.random()
        return "$firstName $lastName"
    }
    
    fun generateRandomNames(count: Int): List<String> {
        return (1..count).map { generateRandomName() }.distinct()
    }
}
