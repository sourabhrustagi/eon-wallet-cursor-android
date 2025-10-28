package com.mobizonetech.aeon_wallet_cursor.domain.usecase

import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide
import com.mobizonetech.aeon_wallet_cursor.domain.repository.WelcomeRepository
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for GetWelcomeSlidesUseCase
 * 
 * Tests cover:
 * - Success scenarios
 * - Error scenarios
 * - Repository interaction
 * - Result type propagation
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GetWelcomeSlidesUseCaseTest {

    private lateinit var repository: WelcomeRepository
    private lateinit var useCase: GetWelcomeSlidesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetWelcomeSlidesUseCase(repository)
    }

    @Test
    fun `invoke returns Success when repository returns Success`() = runTest {
        // Given
        val mockSlides = listOf(
            createMockSlide(0),
            createMockSlide(1),
            createMockSlide(2)
        )
        coEvery { repository.getWelcomeSlides() } returns Result.Success(mockSlides)

        // When
        val result = useCase()

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val data = (result as Result.Success).data
        assertThat(data).hasSize(3)
        assertThat(data).isEqualTo(mockSlides)
    }

    @Test
    fun `invoke returns Error when repository returns Error`() = runTest {
        // Given
        val errorMessage = "Failed to load slides"
        val exception = RuntimeException(errorMessage)
        coEvery { repository.getWelcomeSlides() } returns Result.Error(errorMessage, exception)

        // When
        val result = useCase()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val error = result as Result.Error
        assertThat(error.message).isEqualTo(errorMessage)
        assertThat(error.throwable).isEqualTo(exception)
    }

    @Test
    fun `invoke returns Loading when repository returns Loading`() = runTest {
        // Given
        coEvery { repository.getWelcomeSlides() } returns Result.Loading

        // When
        val result = useCase()

        // Then
        assertThat(result).isInstanceOf(Result.Loading::class.java)
    }

    @Test
    fun `invoke calls repository getWelcomeSlides once`() = runTest {
        // Given
        val mockSlides = listOf(createMockSlide(0))
        coEvery { repository.getWelcomeSlides() } returns Result.Success(mockSlides)

        // When
        useCase()

        // Then
        coVerify(exactly = 1) { repository.getWelcomeSlides() }
    }

    @Test
    fun `invoke returns empty list when repository returns empty Success`() = runTest {
        // Given
        coEvery { repository.getWelcomeSlides() } returns Result.Success(emptyList())

        // When
        val result = useCase()

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val data = (result as Result.Success).data
        assertThat(data).isEmpty()
    }

    @Test
    fun `invoke propagates all slide data correctly`() = runTest {
        // Given
        val mockSlide = WelcomeSlide(
            id = 1,
            title = "Test Title",
            description = "Test Description",
            icon = "🚀",
            iconBackgroundColor = 0xFF6200EE,
            features = listOf("Feature 1", "Feature 2")
        )
        coEvery { repository.getWelcomeSlides() } returns Result.Success(listOf(mockSlide))

        // When
        val result = useCase()

        // Then
        val data = (result as Result.Success).data
        val slide = data.first()
        
        assertThat(slide.id).isEqualTo(1)
        assertThat(slide.title).isEqualTo("Test Title")
        assertThat(slide.description).isEqualTo("Test Description")
        assertThat(slide.icon).isEqualTo("🚀")
        assertThat(slide.iconBackgroundColor).isEqualTo(0xFF6200EE)
        assertThat(slide.features).containsExactly("Feature 1", "Feature 2")
    }

    @Test
    fun `invoke can be called multiple times`() = runTest {
        // Given
        val mockSlides = listOf(createMockSlide(0))
        coEvery { repository.getWelcomeSlides() } returns Result.Success(mockSlides)

        // When
        val result1 = useCase()
        val result2 = useCase()
        val result3 = useCase()

        // Then
        assertThat(result1).isInstanceOf(Result.Success::class.java)
        assertThat(result2).isInstanceOf(Result.Success::class.java)
        assertThat(result3).isInstanceOf(Result.Success::class.java)
        
        coVerify(exactly = 3) { repository.getWelcomeSlides() }
    }

    @Test
    fun `invoke handles repository error without crashing`() = runTest {
        // Given
        coEvery { repository.getWelcomeSlides() } returns Result.Error("Error", null)

        // When
        val result = useCase()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        // Should not throw exception
    }

    @Test
    fun `invoke returns correct result type for different scenarios`() = runTest {
        // Given - Success
        coEvery { repository.getWelcomeSlides() } returns Result.Success(listOf(createMockSlide(0)))
        
        // When
        val successResult = useCase()
        
        // Then
        assertThat(successResult).isInstanceOf(Result.Success::class.java)
        
        // Given - Error
        coEvery { repository.getWelcomeSlides() } returns Result.Error("Error")
        
        // When
        val errorResult = useCase()
        
        // Then
        assertThat(errorResult).isInstanceOf(Result.Error::class.java)
    }

    // Helper function to create mock slide
    private fun createMockSlide(id: Int) = WelcomeSlide(
        id = id,
        title = "Slide $id",
        description = "Description $id",
        icon = "🔒",
        iconBackgroundColor = 0xFF6200EE,
        features = listOf("Feature 1", "Feature 2", "Feature 3")
    )
}

