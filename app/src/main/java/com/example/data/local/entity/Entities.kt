package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val memberId: String, // e.g. "M001"
    val name: String, // e.g. "Suresh M"
    val mobile: String,
    val email: String = "",
    val dob: String, // "15-05-1985"
    val gender: String = "Male",
    val weddingDate: String = "", // "10-06-2010"
    val bloodGroup: String = "B+ Positive",
    val education: String = "B.Com",
    val occupation: String = "Textile Business",
    val address: String = "No. 45, Gandhi Bazaar Street, Thorapadi, Vellore, Tamil Nadu - 632002",
    val googleMapsLink: String = "",
    val notes: String = "",
    val isActive: Boolean = true,
    val isOfficeBearer: Boolean = false,
    val designation: String = "",
    val termPeriod: String = "2024-2027",
    val avatarInitials: String = "",
    val avatarColorHex: String = "#0D5C3F"
)

@Entity(tableName = "family_members")
data class FamilyMemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val memberId: String,
    val name: String,
    val relationship: String, // Spouse, Son, Daughter, Parent, Other
    val dob: String, // "12-08-1990"
    val education: String = "",
    val weddingAnniversary: String = ""
)

@Entity(tableName = "office_bearers")
data class OfficeBearerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val memberId: String,
    val name: String,
    val designation: String, // President, Vice President, Treasurer, Secretary
    val phone: String,
    val term: String = "2024-2027",
    val avatarInitials: String = ""
)

@Entity(tableName = "meetings")
data class MeetingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val meetingDate: String, // "12-10-2026"
    val meetingTime: String = "10:00 AM",
    val venue: String = "Devangar Community Hall, Thorapadi, Vellore",
    val agenda: String = "Monthly general body meeting and accounts review",
    val notes: String = "",
    val isCompleted: Boolean = false
)

@Entity(tableName = "attendance")
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val meetingId: Long,
    val memberId: String,
    val isPresent: Boolean,
    val markedDate: String
)

@Entity(tableName = "funds")
data class FundEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fundCode: String, // "COMMUNITY_FUND", "SAVINGS_FESTIVAL_2026", etc.
    val title: String,
    val category: String, // "COMMUNITY", "SAVINGS", "FESTIVAL", "TOUR"
    val description: String = "",
    val isFixed: Boolean = true,
    val fixedAmount: Double = 100.0,
    val startDate: String = "01-01-2026",
    val status: String = "Active", // "Active", "Closed"
    val connectionType: String = "COMBINED", // "COMBINED" (with Community Fund) or "SEPARATE"
    val totalCollected: Double = 0.0,
    val collectionRate: Int = 0
)

@Entity(tableName = "collection_records")
data class CollectionRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fundCode: String,
    val memberId: String,
    val monthYear: String, // e.g. "10-2026"
    val amount: Double,
    val paymentDate: String,
    val isPaid: Boolean = true,
    val notes: String = ""
)

@Entity(tableName = "consolidated_ledger")
data class ConsolidatedLedgerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // e.g. "08-10-2026"
    val details: String,
    val debit: Double = 0.0,
    val credit: Double = 0.0,
    val balance: Double = 0.0,
    val category: String = "General",
    val referenceType: String = "MANUAL", // COLLECTION, EXPENSE, LOAN_ISSUED, LOAN_REPAID, BUSINESS_GIVEN, BUSINESS_RETURNED, CHIT_COLLECTION, CHIT_PAYOUT, MANUAL
    val referenceId: String = ""
)

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // "07-10-2026"
    val category: String, // "Tea & Snacks", "Hall Rent", "Printing", "Decoration", "Refreshments", etc.
    val amount: Double,
    val description: String = "",
    val paymentMethod: String = "Cash",
    val notes: String = "",
    val ledgerId: Long = 0
)

@Entity(tableName = "business_funds")
data class BusinessFundEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val memberId: String,
    val amountGiven: Double,
    val amountReturned: Double = 0.0,
    val dateGiven: String,
    val purpose: String,
    val notes: String = "",
    val status: String = "Active" // Active, Closed
)

@Entity(tableName = "business_fund_returns")
data class BusinessFundReturnEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val businessFundId: Long,
    val returnDate: String,
    val returnAmount: Double,
    val notes: String = "",
    val ledgerId: Long = 0
)

@Entity(tableName = "member_loans")
data class MemberLoanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val memberId: String,
    val principalAmount: Double,
    val outstandingPrincipal: Double,
    val loanDate: String,
    val purpose: String,
    val monthlyInterestRate: Double = 1.5, // 1.5% per month (₹1.50 per ₹100 per month)
    val status: String = "Active", // Active, Closed
    val notes: String = ""
)

@Entity(tableName = "loan_repayments")
data class LoanRepaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val loanId: Long,
    val repaymentDate: String,
    val totalPayment: Double,
    val interestPaid: Double,
    val principalPaid: Double,
    val remainingPrincipal: Double,
    val monthYear: String,
    val notes: String = "",
    val ledgerId: Long = 0
)

@Entity(tableName = "chit_funds")
data class ChitFundEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chitName: String, // e.g. "Devangar Gold Chit 2026"
    val chitValue: Double, // e.g. 100000.0
    val memberCount: Int = 20,
    val durationMonths: Int = 20,
    val monthlyDue: Double, // 5000.0
    val commissionPercent: Double = 5.0, // 5%
    val startDate: String,
    val auctionDay: String = "10th of every month",
    val status: String = "Bidding Open", // Bidding Open, Active, Completed
    val currentCycleMonth: Int = 1,
    val notes: String = ""
)

@Entity(tableName = "chit_auctions")
data class ChitAuctionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chitId: Long,
    val cycleMonth: Int,
    val auctionDate: String,
    val winningBidAmount: Double, // e.g. 15000.0 (Thallu)
    val winningMemberId: String,
    val winningMemberName: String,
    val communityCommission: Double, // e.g. 5000.0
    val memberDividend: Double, // e.g. 10000.0
    val perMemberBenefit: Double, // e.g. 500.0
    val finalMonthlyDue: Double, // e.g. 4500.0
    val totalExpectedCollection: Double, // e.g. 90000.0
    val winnerPayout: Double, // e.g. 85000.0
    val isConfirmed: Boolean = false,
    val isPayoutRecorded: Boolean = false
)

@Entity(tableName = "chit_member_payments")
data class ChitMemberPaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chitId: Long,
    val cycleMonth: Int,
    val memberId: String,
    val amount: Double,
    val isPaid: Boolean = false,
    val paymentDate: String = ""
)

@Entity(tableName = "gift_distributions")
data class GiftDistributionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val festivalName: String, // "Festival Gift - Deepavali 2026"
    val year: Int = 2026,
    val giftType: String = "CASH", // CASH or PRODUCT
    val cashAmount: Double = 1000.0,
    val giftItemName: String = "Traditional Gift Box",
    val notes: String = "Annual Deepavali Community Distribution",
    val totalEligible: Int = 150,
    val totalGiven: Int = 120,
    val totalPending: Int = 30
)

@Entity(tableName = "gift_recipients")
data class GiftRecipientEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val distributionId: Long,
    val memberId: String,
    val status: String = "PENDING", // GIVEN, PENDING
    val dateGiven: String = ""
)

@Entity(tableName = "gallery_items")
data class GalleryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // Festivals, Tours, Meetings, Community
    val date: String,
    val photoUrl: String,
    val caption: String
)

@Entity(tableName = "community_settings")
data class CommunitySettingsEntity(
    @PrimaryKey val id: Int = 1,
    val communityName: String = "சங்கமம் – DEVANGAR COMMUNITY",
    val address: String = "No. 12, Sannathi Street, Thorapadi, Vellore, Tamil Nadu - 632002",
    val contactPhone: String = "+91 98765 43210",
    val defaultLoanInterestRate: Double = 1.5,
    val tapSoundEnabled: Boolean = true,
    val tapSoundType: String = "Soft",
    val pinLockEnabled: Boolean = false,
    val pinCode: String = ""
)
