package com.mobizonetech.aeon_wallet_cursor.data.repository

import android.content.Context
import android.content.res.Resources
import com.mobizonetech.aeon_wallet_cursor.R
import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for WelcomeRepositoryImpl
 * 
 * Tests cover:
 * - Success scenarios with valid data
 * - Error scenarios with exceptions
 * - Resource string retrieval
 * - Data mapping and transformation
 */
@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var resources: Resources
    private lateinit var repository: WelcomeRepositoryImpl

    @Before
    fun setup() {
        context = mockk()
        resources = mockk()
        
        every { context.resources } returns resources
        
        repository = WelcomeRepositoryImpl(context)
    }

    @Test
    fun `getWelcomeSlides returns Success with valid slides data`() = runTest {
        // Given
        setupMockStrings()

        // When
        val result = repository.getWelcomeSlides()

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        
        val slides = (result as Result.Success).data
        assertThat(slides).hasSize(5)
        
        // Verify first slide
        assertThat(slides[0].id).isEqualTo(0)
        assertThat(slides[0].title).isEqualTo("Slide 0 Title")
        assertThat(slides[0].description).isEqualTo("Slide 0 Description")
        assertThat(slides[0].icon).isEqualTo("₿")
        assertThat(slides[0].iconBackgroundColor).isEqualTo(0xFF6200EE)
        assertThat(slides[0].features).hasSize(3)
        assertThat(slides[0].features[0]).isEqualTo("Feature 0.0")
    }

    @Test
    fun `getWelcomeSlides returns all five slides with correct IDs`() = runTest {
        // Given
        setupMockStrings()

        // When
        val result = repository.getWelcomeSlides()

        // Then
        val slides = (result as Result.Success).data
        
        assertThat(slides.map { it.id }).containsExactly(0, 1, 2, 3, 4).inOrder()
    }

    @Test
    fun `getWelcomeSlides uses correct icon background colors`() = runTest {
        // Given
        setupMockStrings()

        // When
        val result = repository.getWelcomeSlides()

        // Then
        val slides = (result as Result.Success).data
        
        assertThat(slides[0].iconBackgroundColor).isEqualTo(0xFF6200EE)
        assertThat(slides[1].iconBackgroundColor).isEqualTo(0xFFB00020)
        assertThat(slides[2].iconBackgroundColor).isEqualTo(0xFF03DAC6)
        assertThat(slides[3].iconBackgroundColor).isEqualTo(0xFF018786)
        assertThat(slides[4].iconBackgroundColor).isEqualTo(0xFF6200EE)
    }

    @Test
    fun `getWelcomeSlides each slide has three features`() = runTest {
        // Given
        setupMockStrings()

        // When
        val result = repository.getWelcomeSlides()

        // Then
        val slides = (result as Result.Success).data
        
        slides.forEach { slide ->
            assertThat(slide.features).hasSize(3)
        }
    }

    @Test
    fun `getWelcomeSlides returns Error when resources throw exception`() = runTest {
        // Given
        val exceptionMessage = "Resource not found"
        every { context.resources } throws Resources.NotFoundException(exceptionMessage)

        // When
        val result = repository.getWelcomeSlides()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        
        val error = result as Result.Error
        assertThat(error.message).contains(exceptionMessage)
        assertThat(error.throwable).isInstanceOf(Resources.NotFoundException::class.java)
    }

    @Test
    fun `getWelcomeSlides returns Error when getString throws exception`() = runTest {
        // Given
        val exceptionMessage = "String resource not found"
        every { context.resources } returns resources
        every { resources.getString(R.string.slide_0_title) } throws RuntimeException(exceptionMessage)

        // When
        val result = repository.getWelcomeSlides()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        
        val error = result as Result.Error
        assertThat(error.message).isNotEmpty()
        assertThat(error.throwable).isInstanceOf(RuntimeException::class.java)
    }

    @Test
    fun `getWelcomeSlides returns Error with default message when exception message is null`() = runTest {
        // Given
        every { context.resources } throws NullPointerException()

        // When
        val result = repository.getWelcomeSlides()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        
        val error = result as Result.Error
        assertThat(error.message).isEqualTo("Unknown error occurred while loading slides")
    }

    @Test
    fun `getWelcomeSlides accesses context resources correctly`() = runTest {
        // Given
        setupMockStrings()

        // When
        repository.getWelcomeSlides()

        // Then
        verify(exactly = 1) { context.resources }
    }

    @Test
    fun `getWelcomeSlides retrieves all required string resources`() = runTest {
        // Given
        setupMockStrings()

        // When
        repository.getWelcomeSlides()

        // Then - Verify slide 0 strings are retrieved
        verify(exactly = 1) { resources.getString(R.string.slide_0_title) }
        verify(exactly = 1) { resources.getString(R.string.slide_0_description) }
        verify(exactly = 1) { resources.getString(R.string.slide_0_icon) }
        verify(exactly = 1) { resources.getString(R.string.slide_0_feature_0) }
        verify(exactly = 1) { resources.getString(R.string.slide_0_feature_1) }
        verify(exactly = 1) { resources.getString(R.string.slide_0_feature_2) }
        
        // Verify slide 4 strings are retrieved
        verify(exactly = 1) { resources.getString(R.string.slide_4_title) }
        verify(exactly = 1) { resources.getString(R.string.slide_4_description) }
        verify(exactly = 1) { resources.getString(R.string.slide_4_icon) }
    }

    @Test
    fun `getWelcomeSlides second slide has correct data`() = runTest {
        // Given
        setupMockStrings()

        // When
        val result = repository.getWelcomeSlides()

        // Then
        val slide = (result as Result.Success).data[1]
        
        assertThat(slide.id).isEqualTo(1)
        assertThat(slide.title).isEqualTo("Slide 1 Title")
        assertThat(slide.description).isEqualTo("Slide 1 Description")
        assertThat(slide.icon).isEqualTo("🔒")
        assertThat(slide.features).containsExactly(
            "Feature 1.0",
            "Feature 1.1",
            "Feature 1.2"
        ).inOrder()
    }

    @Test
    fun `getWelcomeSlides third slide has correct data`() = runTest {
        // Given
        setupMockStrings()

        // When
        val result = repository.getWelcomeSlides()

        // Then
        val slide = (result as Result.Success).data[2]
        
        assertThat(slide.id).isEqualTo(2)
        assertThat(slide.title).isEqualTo("Slide 2 Title")
        assertThat(slide.icon).isEqualTo("📈")
    }

    @Test
    fun `getWelcomeSlides returns Success result type`() = runTest {
        // Given
        setupMockStrings()

        // When
        val result = repository.getWelcomeSlides()

        // Then
        assertThat(result).isNotInstanceOf(Result.Loading::class.java)
        assertThat(result).isNotInstanceOf(Result.Error::class.java)
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    // Helper function to setup mock string resources
    private fun setupMockStrings() {
        // Slide 0
        every { resources.getString(R.string.slide_0_title) } returns "Slide 0 Title"
        every { resources.getString(R.string.slide_0_description) } returns "Slide 0 Description"
        every { resources.getString(R.string.slide_0_icon) } returns "₿"
        every { resources.getString(R.string.slide_0_feature_0) } returns "Feature 0.0"
        every { resources.getString(R.string.slide_0_feature_1) } returns "Feature 0.1"
        every { resources.getString(R.string.slide_0_feature_2) } returns "Feature 0.2"

        // Slide 1
        every { resources.getString(R.string.slide_1_title) } returns "Slide 1 Title"
        every { resources.getString(R.string.slide_1_description) } returns "Slide 1 Description"
        every { resources.getString(R.string.slide_1_icon) } returns "🔒"
        every { resources.getString(R.string.slide_1_feature_0) } returns "Feature 1.0"
        every { resources.getString(R.string.slide_1_feature_1) } returns "Feature 1.1"
        every { resources.getString(R.string.slide_1_feature_2) } returns "Feature 1.2"

        // Slide 2
        every { resources.getString(R.string.slide_2_title) } returns "Slide 2 Title"
        every { resources.getString(R.string.slide_2_description) } returns "Slide 2 Description"
        every { resources.getString(R.string.slide_2_icon) } returns "📈"
        every { resources.getString(R.string.slide_2_feature_0) } returns "Feature 2.0"
        every { resources.getString(R.string.slide_2_feature_1) } returns "Feature 2.1"
        every { resources.getString(R.string.slide_2_feature_2) } returns "Feature 2.2"

        // Slide 3
        every { resources.getString(R.string.slide_3_title) } returns "Slide 3 Title"
        every { resources.getString(R.string.slide_3_description) } returns "Slide 3 Description"
        every { resources.getString(R.string.slide_3_icon) } returns "📊"
        every { resources.getString(R.string.slide_3_feature_0) } returns "Feature 3.0"
        every { resources.getString(R.string.slide_3_feature_1) } returns "Feature 3.1"
        every { resources.getString(R.string.slide_3_feature_2) } returns "Feature 3.2"

        // Slide 4
        every { resources.getString(R.string.slide_4_title) } returns "Slide 4 Title"
        every { resources.getString(R.string.slide_4_description) } returns "Slide 4 Description"
        every { resources.getString(R.string.slide_4_icon) } returns "🚀"
        every { resources.getString(R.string.slide_4_feature_0) } returns "Feature 4.0"
        every { resources.getString(R.string.slide_4_feature_1) } returns "Feature 4.1"
        every { resources.getString(R.string.slide_4_feature_2) } returns "Feature 4.2"
    }
}

