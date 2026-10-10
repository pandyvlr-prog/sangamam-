package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.entity.MemberEntity
import com.example.data.repository.CommunityRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("சங்கமம்", appName)
    }

    @Test
    fun `indian currency formatting`() {
        assertEquals("₹5,48,000", CommunityRepository.formatRupee(548000.0))
        assertEquals("₹1,000", CommunityRepository.formatRupee(1000.0))
        assertEquals("₹10,000", CommunityRepository.formatRupee(10000.0))
        assertEquals("₹1,00,000", CommunityRepository.formatRupee(100000.0))
    }

    @Test
    fun `chit fund mathematical model verification`() {
        val chitValue = 100000.0
        val memberCount = 20
        val normalDue = 5000.0
        val winningBid = 15000.0
        val commissionPercent = 5.0

        val commission = chitValue * (commissionPercent / 100.0)
        val memberDistribution = winningBid - commission
        val perMemberBenefit = memberDistribution / memberCount
        val finalMonthlyDue = normalDue - perMemberBenefit
        val totalExpectedCollection = finalMonthlyDue * memberCount
        val winnerPayout = chitValue - winningBid

        assertEquals(5000.0, commission, 0.01)
        assertEquals(10000.0, memberDistribution, 0.01)
        assertEquals(500.0, perMemberBenefit, 0.01)
        assertEquals(4500.0, finalMonthlyDue, 0.01)
        assertEquals(90000.0, totalExpectedCollection, 0.01)
        assertEquals(85000.0, winnerPayout, 0.01)
    }

    @Test
    fun `loan interest calculation on outstanding principal`() {
        val principal = 10000.0
        val monthlyRate = 1.5 // 1.5% per month
        val firstMonthInterest = principal * (monthlyRate / 100.0)
        assertEquals(150.0, firstMonthInterest, 0.01)

        val repayment = 2000.0
        val principalRepaid = repayment - firstMonthInterest
        val remainingPrincipal = principal - principalRepaid
        assertEquals(8150.0, remainingPrincipal, 0.01)

        val nextMonthInterest = remainingPrincipal * (monthlyRate / 100.0)
        assertEquals(122.25, nextMonthInterest, 0.01)
    }

    @Test
    fun `age calculation from date of birth`() {
        val age = CommunityRepository.calculateAge("15-05-1985")
        assertEquals(41, age)
    }

    @Test
    fun `room database member insertion and retrieval`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getDatabase(context, this)
        val testMember = MemberEntity(
            memberId = "M999",
            name = "Vellore Test Member",
            mobile = "+91 99999 88888",
            email = "test@devangar.org",
            dob = "10-10-1992",
            address = "Thorapadi, Vellore",
            occupation = "Textile Merchant",
            isActive = true
        )
        db.memberDao().insertMember(testMember)
        val retrieved = db.memberDao().getMemberByIdDirect("M999")
        assertNotNull(retrieved)
        assertEquals("Vellore Test Member", retrieved?.name)
        assertEquals("+91 99999 88888", retrieved?.mobile)
    }

    @Test
    fun `viewmodel add member updates member count`() = runBlocking {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val vm = com.example.ui.CommunityViewModel(app)
        val initialCount = vm.repository.getMemberCount()
        println("DEBUG: initialCount=$initialCount")
        try {
            val totalCount = vm.repository.getMemberCount()
            val nextId = "M" + (totalCount + 1).toString().padStart(3, '0')
            val newMember = MemberEntity(
                memberId = nextId,
                name = "Direct Test Member",
                mobile = "+91 91234 56789",
                email = "newmember@test.com",
                dob = "15-05-1990",
                address = "Thorapadi"
            )
            val insertedId = vm.repository.insertMember(newMember)
            println("DEBUG: insertedId=$insertedId")
        } catch (e: Exception) {
            println("DEBUG: exception during insert: $e")
            e.printStackTrace()
        }
        val newCount = vm.repository.getMemberCount()
        println("DEBUG: newCount=$newCount")
        org.junit.Assert.assertEquals(initialCount + 1, newCount)
    }
}
