package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetWelcomeSlidesUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for WelcomeViewModel
 * 
 * Tests cover:
 * - Initial state and loading
 * - Success scenarios
 * - Error scenarios
 * - Page navigation
 * - User actions (next, skip, get started, sign in)
 */
@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getWelcomeSlidesUseCase: GetWelcomeSlidesUseCase
    private lateinit var viewModel: WelcomeViewModel
    
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getWelcomeSlidesUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() {
        // Given
        coEvery { getWelcomeSlidesUseCase() } returns Result.Loading

        // When
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)

        // Then
        val state = viewModel.uiState.value
        assertThat(state.isLoading).isTrue()
        assertThat(state.slides).isEmpty()
        assertThat(state.currentPage).isEqualTo(0)
        assertThat(state.error).isNull()
    }

    @Test
    fun `loadSlides updates state with success result`() = runTest {
        // Given
        val mockSlides = listOf(
            createMockSlide(0),
            createMockSlide(1),
            createMockSlide(2)
        )
        coEvery { getWelcomeSlidesUseCase() } returns Result.Success(mockSlides)

        // When
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.slides).hasSize(3)
        assertThat(state.slides).isEqualTo(mockSlides)
        assertThat(state.error).isNull()
    }

    @Test
    fun `loadSlides updates state with error result`() = runTest {
        // Given
        val errorMessage = "Failed to load slides"
        coEvery { getWelcomeSlidesUseCase() } returns Result.Error(errorMessage)

        // When
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.slides).isEmpty()
        assertThat(state.error).isEqualTo(errorMessage)
    }

    @Test
    fun `loadSlides calls use case exactly once`() = runTest {
        // Given
        coEvery { getWelcomeSlidesUseCase() } returns Result.Success(emptyList())

        // When
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { getWelcomeSlidesUseCase() }
    }

    @Test
    fun `onPageChanged updates current page`() = runTest {
        // Given
        coEvery { getWelcomeSlidesUseCase() } returns Result.Success(listOf(createMockSlide(0)))
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onPageChanged(2)

        // Then
        assertThat(viewModel.uiState.value.currentPage).isEqualTo(2)
    }

    @Test
    fun `onNextClick increments current page when canNavigateNext is true`() = runTest {
        // Given
        val mockSlides = listOf(createMockSlide(0), createMockSlide(1), createMockSlide(2))
        coEvery { getWelcomeSlidesUseCase() } returns Result.Success(mockSlides)
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value.currentPage).isEqualTo(0)

        // When
        viewModel.onNextClick()

        // Then
        assertThat(viewModel.uiState.value.currentPage).isEqualTo(1)
    }

    @Test
    fun `onNextClick does not increment when on last page`() = runTest {
        // Given
        val mockSlides = listOf(createMockSlide(0), createMockSlide(1))
        coEvery { getWelcomeSlidesUseCase() } returns Result.Success(mockSlides)
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onPageChanged(1) // Go to last page

        // When
        viewModel.onNextClick()

        // Then
        assertThat(viewModel.uiState.value.currentPage).isEqualTo(1)
    }

    @Test
    fun `onSkipClick navigates to last page`() = runTest {
        // Given
        val mockSlides = listOf(
            createMockSlide(0),
            createMockSlide(1),
            createMockSlide(2),
            createMockSlide(3)
        )
        coEvery { getWelcomeSlidesUseCase() } returns Result.Success(mockSlides)
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value.currentPage).isEqualTo(0)

        // When
        viewModel.onSkipClick()

        // Then
        assertThat(viewModel.uiState.value.currentPage).isEqualTo(3) // Last page
    }

    @Test
    fun `onSkipClick does nothing when slides are empty`() = runTest {
        // Given
        coEvery { getWelcomeSlidesUseCase() } returns Result.Success(emptyList())
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onSkipClick()

        // Then
        assertThat(viewModel.uiState.value.currentPage).isEqualTo(0)
    }

    @Test
    fun `isOnLastPage returns true when on last page`() = runTest {
        // Given
        val mockSlides = listOf(createMockSlide(0), createMockSlide(1), createMockSlide(2))
        coEvery { getWelcomeSlidesUseCase() } returns Result.Success(mockSlides)
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onPageChanged(2)

        // Then
        assertThat(viewModel.uiState.value.isOnLastPage).isTrue()
    }

    @Test
    fun `isOnLastPage returns false when not on last page`() = runTest {
        // Given
        val mockSlides = listOf(createMockSlide(0), createMockSlide(1), createMockSlide(2))
        coEvery { getWelcomeSlidesUseCase() } returns Result.Success(mockSlides)
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onPageChanged(1)

        // Then
        assertThat(viewModel.uiState.value.isOnLastPage).isFalse()
    }

    @Test
    fun `canNavigateNext returns true when not on last page`() = runTest {
        // Given
        val mockSlides = listOf(createMockSlide(0), createMockSlide(1), createMockSlide(2))
        coEvery { getWelcomeSlidesUseCase() } returns Result.Success(mockSlides)
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onPageChanged(0)

        // Then
        assertThat(viewModel.uiState.value.canNavigateNext).isTrue()
    }

    @Test
    fun `canNavigateNext returns false when on last page`() = runTest {
        // Given
        val mockSlides = listOf(createMockSlide(0), createMockSlide(1))
        coEvery { getWelcomeSlidesUseCase() } returns Result.Success(mockSlides)
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onPageChanged(1)

        // Then
        assertThat(viewModel.uiState.value.canNavigateNext).isFalse()
    }

    @Test
    fun `canNavigateSkip returns true when not on last page`() = runTest {
        // Given
        val mockSlides = listOf(createMockSlide(0), createMockSlide(1), createMockSlide(2))
        coEvery { getWelcomeSlidesUseCase() } returns Result.Success(mockSlides)
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onPageChanged(1)

        // Then
        assertThat(viewModel.uiState.value.canNavigateSkip).isTrue()
    }

    @Test
    fun `canNavigateSkip returns false when on last page`() = runTest {
        // Given
        val mockSlides = listOf(createMockSlide(0), createMockSlide(1))
        coEvery { getWelcomeSlidesUseCase() } returns Result.Success(mockSlides)
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onPageChanged(1)

        // Then
        assertThat(viewModel.uiState.value.canNavigateSkip).isFalse()
    }

    @Test
    fun `onGetStartedClick does not crash`() = runTest {
        // Given
        coEvery { getWelcomeSlidesUseCase() } returns Result.Success(listOf(createMockSlide(0)))
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When/Then - should not throw exception
        viewModel.onGetStartedClick()
    }

    @Test
    fun `onSignInClick does not crash`() = runTest {
        // Given
        coEvery { getWelcomeSlidesUseCase() } returns Result.Success(listOf(createMockSlide(0)))
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When/Then - should not throw exception
        viewModel.onSignInClick()
    }

    @Test
    fun `multiple page changes are tracked correctly`() = runTest {
        // Given
        val mockSlides = listOf(
            createMockSlide(0),
            createMockSlide(1),
            createMockSlide(2),
            createMockSlide(3)
        )
        coEvery { getWelcomeSlidesUseCase() } returns Result.Success(mockSlides)
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onNextClick()
        assertThat(viewModel.uiState.value.currentPage).isEqualTo(1)
        
        viewModel.onNextClick()
        assertThat(viewModel.uiState.value.currentPage).isEqualTo(2)
        
        viewModel.onNextClick()
        assertThat(viewModel.uiState.value.currentPage).isEqualTo(3)
    }

    @Test
    fun `error state preserves empty slides list`() = runTest {
        // Given
        coEvery { getWelcomeSlidesUseCase() } returns Result.Error("Error occurred")

        // When
        viewModel = WelcomeViewModel(getWelcomeSlidesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.slides).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.error).isNotNull()
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

