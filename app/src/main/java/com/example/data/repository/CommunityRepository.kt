package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommunityRepository(private val db: AppDatabase) {

    // DAOs
    val members: Flow<List<MemberEntity>> = db.memberDao().getAllMembers()
    val funds: Flow<List<FundEntity>> = db.fundDao().getAllFunds()
    val ledgerEntries: Flow<List<ConsolidatedLedgerEntity>> = db.ledgerDao().getAllLedgerEntries()
    val expenses: Flow<List<ExpenseEntity>> = db.expenseDao().getAllExpenses()
    val businessFunds: Flow<List<BusinessFundEntity>> = db.businessFundDao().getAllBusinessFunds()
    val memberLoans: Flow<List<MemberLoanEntity>> = db.loanDao().getAllLoans()
    val chitFunds: Flow<List<ChitFundEntity>> = db.chitDao().getAllChitFunds()
    val giftDistributions: Flow<List<GiftDistributionEntity>> = db.giftDao().getAllDistributions()
    val officeBearers: Flow<List<OfficeBearerEntity>> = db.officeBearerDao().getAllOfficeBearers()
    val meetings: Flow<List<MeetingEntity>> = db.meetingDao().getAllMeetings()
    val galleryItems: Flow<List<GalleryItemEntity>> = db.galleryDao().getAllGalleryItems()
    val settings: Flow<CommunitySettingsEntity?> = db.settingsDao().getSettings()

    fun getMemberById(memberId: String): Flow<MemberEntity?> = db.memberDao().getMemberById(memberId)
    fun getFamilyForMember(memberId: String): Flow<List<FamilyMemberEntity>> = db.familyMemberDao().getFamilyForMember(memberId)
    fun getCollectionsForMember(memberId: String): Flow<List<CollectionRecordEntity>> = db.fundDao().getCollectionsForMember(memberId)
    fun getCollectionsForFundAndMonth(fundCode: String, monthYear: String): Flow<List<CollectionRecordEntity>> =
        db.fundDao().getCollectionsForFundAndMonth(fundCode, monthYear)

    fun getReturnsForBusinessFund(fundId: Long): Flow<List<BusinessFundReturnEntity>> =
        db.businessFundDao().getReturnsForFund(fundId)

    fun getRepaymentsForLoan(loanId: Long): Flow<List<LoanRepaymentEntity>> =
        db.loanDao().getRepaymentsForLoan(loanId)

    fun getAuctionsForChit(chitId: Long): Flow<List<ChitAuctionEntity>> =
        db.chitDao().getAuctionsForChit(chitId)

    fun getRecipientsForDistribution(distId: Long): Flow<List<GiftRecipientEntity>> =
        db.giftDao().getRecipientsForDistribution(distId)

    // Member Operations
    suspend fun getMemberCount(): Int = db.memberDao().getMemberCount()
    suspend fun getActiveMemberCount(): Int = db.memberDao().getActiveMemberCount()

    suspend fun getNextMemberId(): String {
        val ids = db.memberDao().getAllMemberIds()
        var maxNum = 0
        for (id in ids) {
            val numStr = id.removePrefix("M").trim()
            val num = numStr.toIntOrNull() ?: 0
            if (num > maxNum) maxNum = num
        }
        val nextNum = maxNum + 1
        return "M" + nextNum.toString().padStart(3, '0')
    }

    suspend fun insertMember(member: MemberEntity) = db.memberDao().insertMember(member)
    suspend fun updateMember(member: MemberEntity) = db.memberDao().updateMember(member)
    suspend fun deleteMember(member: MemberEntity) = db.memberDao().deleteMember(member)

    suspend fun insertFamilyMember(item: FamilyMemberEntity) = db.familyMemberDao().insertFamilyMember(item)
    suspend fun updateFamilyMember(item: FamilyMemberEntity) = db.familyMemberDao().updateFamilyMember(item)
    suspend fun deleteFamilyMember(item: FamilyMemberEntity) = db.familyMemberDao().deleteFamilyMember(item)

    // Expense Operations with Consolidated Ledger Integration
    suspend fun addExpense(
        date: String,
        category: String,
        amount: Double,
        description: String,
        paymentMethod: String,
        notes: String
    ): Long {
        val currentBalance = getCurrentLedgerBalance()
        val newBalance = currentBalance - amount

        val ledgerId = db.ledgerDao().insertLedgerEntry(
            ConsolidatedLedgerEntity(
                date = date,
                details = "$category: $description".trimEnd(':', ' '),
                debit = amount,
                credit = 0.0,
                balance = newBalance,
                category = category,
                referenceType = "EXPENSE",
                referenceId = "EXP_${System.currentTimeMillis()}"
            )
        )

        val expense = ExpenseEntity(
            date = date,
            category = category,
            amount = amount,
            description = description,
            paymentMethod = paymentMethod,
            notes = notes,
            ledgerId = ledgerId
        )
        return db.expenseDao().insertExpense(expense)
    }

    suspend fun updateExpense(expense: ExpenseEntity) {
        val existingExpense = db.expenseDao().getExpenseById(expense.id)
        db.expenseDao().updateExpense(expense)

        if (existingExpense != null && expense.ledgerId > 0) {
            val existingLedger = db.ledgerDao().getLedgerEntryById(expense.ledgerId)
            if (existingLedger != null) {
                val amountDiff = expense.amount - existingExpense.amount
                val updatedLedger = existingLedger.copy(
                    date = expense.date,
                    details = "${expense.category}: ${expense.description}".trimEnd(':', ' '),
                    debit = expense.amount,
                    category = expense.category,
                    balance = existingLedger.balance - amountDiff
                )
                db.ledgerDao().updateLedgerEntry(updatedLedger)
            }
        }
    }

    suspend fun deleteExpense(expenseId: Long) {
        val expense = db.expenseDao().getExpenseById(expenseId)
        if (expense != null) {
            if (expense.ledgerId > 0) {
                db.ledgerDao().deleteLedgerEntry(expense.ledgerId)
            }
            db.expenseDao().deleteExpense(expenseId)
        }
    }

    // Monthly Collection Operations with Consolidated Ledger Integration
    suspend fun saveMonthlyCollection(
        fundCode: String,
        fundTitle: String,
        monthYear: String,
        date: String,
        records: List<CollectionRecordEntity>
    ) {
        // 1. Insert/update individual member collection records
        db.fundDao().insertCollectionRecords(records)

        // 2. Calculate consolidated sum of payments
        val totalPaidThisSave = records.filter { it.isPaid }.sumOf { it.amount }
        if (totalPaidThisSave > 0) {
            val paidCount = records.count { it.isPaid }
            val refId = "COLL_${fundCode}_$monthYear"
            val existingLedger = db.ledgerDao().getLedgerByReference("COLLECTION", refId)

            val currentBalance = getCurrentLedgerBalance()

            if (existingLedger != null) {
                // Update existing consolidated ledger row
                val newCredit = totalPaidThisSave
                val diff = newCredit - existingLedger.credit
                val updatedLedger = existingLedger.copy(
                    details = "$fundTitle ($paidCount Members)",
                    credit = newCredit,
                    balance = existingLedger.balance + diff
                )
                db.ledgerDao().updateLedgerEntry(updatedLedger)
            } else {
                // Insert new consolidated ledger row
                val newBalance = currentBalance + totalPaidThisSave
                db.ledgerDao().insertLedgerEntry(
                    ConsolidatedLedgerEntity(
                        date = date,
                        details = "$fundTitle ($paidCount Members)",
                        debit = 0.0,
                        credit = totalPaidThisSave,
                        balance = newBalance,
                        category = "Collection",
                        referenceType = "COLLECTION",
                        referenceId = refId
                    )
                )
            }
        }
    }

    // Business Fund Assistance & Returns
    suspend fun giveBusinessFund(
        memberId: String,
        memberName: String,
        amount: Double,
        date: String,
        purpose: String,
        notes: String
    ): Long {
        val currentBalance = getCurrentLedgerBalance()
        val newBalance = currentBalance - amount

        val ledgerId = db.ledgerDao().insertLedgerEntry(
            ConsolidatedLedgerEntity(
                date = date,
                details = "Business Fund Support ($memberId - $memberName)",
                debit = amount,
                credit = 0.0,
                balance = newBalance,
                category = "Business Fund",
                referenceType = "BUSINESS_GIVEN",
                referenceId = "BF_${System.currentTimeMillis()}"
            )
        )

        return db.businessFundDao().insertBusinessFund(
            BusinessFundEntity(
                memberId = memberId,
                amountGiven = amount,
                amountReturned = 0.0,
                dateGiven = date,
                purpose = purpose,
                notes = notes,
                status = "Active"
            )
        )
    }

    suspend fun recordBusinessFundReturn(
        businessFundId: Long,
        returnAmount: Double,
        returnDate: String,
        notes: String
    ): Long {
        val bf = db.businessFundDao().getBusinessFundById(businessFundId) ?: return -1
        val newReturned = bf.amountReturned + returnAmount
        val isClosed = newReturned >= bf.amountGiven

        db.businessFundDao().updateBusinessFund(
            bf.copy(
                amountReturned = newReturned,
                status = if (isClosed) "Closed" else "Active"
            )
        )

        val currentBalance = getCurrentLedgerBalance()
        val newBalance = currentBalance + returnAmount

        val ledgerId = db.ledgerDao().insertLedgerEntry(
            ConsolidatedLedgerEntity(
                date = returnDate,
                details = "Business Fund Return (${bf.memberId} - Partial/Full)",
                debit = 0.0,
                credit = returnAmount,
                balance = newBalance,
                category = "Business Fund",
                referenceType = "BUSINESS_RETURNED",
                referenceId = "BFR_${System.currentTimeMillis()}"
            )
        )

        return db.businessFundDao().insertReturn(
            BusinessFundReturnEntity(
                businessFundId = businessFundId,
                returnDate = returnDate,
                returnAmount = returnAmount,
                notes = notes,
                ledgerId = ledgerId
            )
        )
    }

    // Member Loans & Repayments
    suspend fun issueMemberLoan(
        memberId: String,
        memberName: String,
        principalAmount: Double,
        date: String,
        purpose: String,
        monthlyInterestRate: Double,
        notes: String
    ): Long {
        val currentBalance = getCurrentLedgerBalance()
        val newBalance = currentBalance - principalAmount

        val ledgerId = db.ledgerDao().insertLedgerEntry(
            ConsolidatedLedgerEntity(
                date = date,
                details = "Member Loan Issued ($memberId - $memberName)",
                debit = principalAmount,
                credit = 0.0,
                balance = newBalance,
                category = "Loan",
                referenceType = "LOAN_ISSUED",
                referenceId = "LOAN_${System.currentTimeMillis()}"
            )
        )

        return db.loanDao().insertLoan(
            MemberLoanEntity(
                memberId = memberId,
                principalAmount = principalAmount,
                outstandingPrincipal = principalAmount,
                loanDate = date,
                purpose = purpose,
                monthlyInterestRate = monthlyInterestRate,
                status = "Active",
                notes = notes
            )
        )
    }

    suspend fun recordLoanRepayment(
        loanId: Long,
        totalPayment: Double,
        repaymentDate: String,
        monthYear: String,
        notes: String
    ): Long {
        val loan = db.loanDao().getLoanById(loanId) ?: return -1
        // Interest calculated on outstanding principal:
        val interestDue = (loan.outstandingPrincipal * (loan.monthlyInterestRate / 100.0)).coerceAtLeast(0.0)
        val interestPaid = totalPayment.coerceAtMost(interestDue)
        val principalPaid = (totalPayment - interestPaid).coerceAtLeast(0.0).coerceAtMost(loan.outstandingPrincipal)
        val remainingPrincipal = (loan.outstandingPrincipal - principalPaid).coerceAtLeast(0.0)
        val isClosed = remainingPrincipal <= 0.0

        db.loanDao().updateLoan(
            loan.copy(
                outstandingPrincipal = remainingPrincipal,
                status = if (isClosed) "Closed" else "Active"
            )
        )

        val currentBalance = getCurrentLedgerBalance()
        val newBalance = currentBalance + totalPayment

        val ledgerId = db.ledgerDao().insertLedgerEntry(
            ConsolidatedLedgerEntity(
                date = repaymentDate,
                details = "Loan Repayment (${loan.memberId} Int: ₹${interestPaid.toInt()} Prin: ₹${principalPaid.toInt()})",
                debit = 0.0,
                credit = totalPayment,
                balance = newBalance,
                category = "Loan Repayment",
                referenceType = "LOAN_REPAID",
                referenceId = "REPAY_${System.currentTimeMillis()}"
            )
        )

        return db.loanDao().insertRepayment(
            LoanRepaymentEntity(
                loanId = loanId,
                repaymentDate = repaymentDate,
                totalPayment = totalPayment,
                interestPaid = interestPaid,
                principalPaid = principalPaid,
                remainingPrincipal = remainingPrincipal,
                monthYear = monthYear,
                notes = notes,
                ledgerId = ledgerId
            )
        )
    }

    // Chit Funds Operations
    suspend fun createChitFund(chit: ChitFundEntity): Long {
        return db.chitDao().insertChitFund(chit)
    }

    suspend fun recordChitAuctionResult(
        chitId: Long,
        cycleMonth: Int,
        auctionDate: String,
        winningBidAmount: Double,
        winningMemberId: String,
        winningMemberName: String
    ): Long {
        val chit = db.chitDao().getChitFundByIdDirect(chitId) ?: return -1

        // Mathematical model from spec:
        // E.g. Chit Value ₹1,00,000, 20 members, normal monthly contribution ₹5,000
        // Winning bid (thallu) = ₹15,000
        // Community commission = 5% of ₹1,00,000 = ₹5,000
        // Member dividend distribution = ₹15,000 - ₹5,000 = ₹10,000
        // Per-member benefit = ₹10,000 / 20 = ₹500
        // Final monthly payable per member = ₹5,000 - ₹500 = ₹4,500
        // Total expected collection = 20 * ₹4,500 = ₹90,000
        // Winner payout = ₹1,00,000 - ₹15,000 = ₹85,000
        val communityCommission = (chit.chitValue * (chit.commissionPercent / 100.0))
        val memberDividend = (winningBidAmount - communityCommission).coerceAtLeast(0.0)
        val perMemberBenefit = memberDividend / chit.memberCount
        val finalMonthlyDue = chit.monthlyDue - perMemberBenefit
        val totalExpectedCollection = finalMonthlyDue * chit.memberCount
        val winnerPayout = chit.chitValue - winningBidAmount

        val auction = ChitAuctionEntity(
            chitId = chitId,
            cycleMonth = cycleMonth,
            auctionDate = auctionDate,
            winningBidAmount = winningBidAmount,
            winningMemberId = winningMemberId,
            winningMemberName = winningMemberName,
            communityCommission = communityCommission,
            memberDividend = memberDividend,
            perMemberBenefit = perMemberBenefit,
            finalMonthlyDue = finalMonthlyDue,
            totalExpectedCollection = totalExpectedCollection,
            winnerPayout = winnerPayout,
            isConfirmed = true,
            isPayoutRecorded = false
        )
        return db.chitDao().insertAuction(auction)
    }

    suspend fun recordChitWinnerPayout(auction: ChitAuctionEntity, chitName: String): Long {
        val currentBalance = getCurrentLedgerBalance()
        val newBalance = currentBalance - auction.winnerPayout

        val ledgerId = db.ledgerDao().insertLedgerEntry(
            ConsolidatedLedgerEntity(
                date = auction.auctionDate,
                details = "$chitName Month ${auction.cycleMonth} Payout (${auction.winningMemberName})",
                debit = auction.winnerPayout,
                credit = 0.0,
                balance = newBalance,
                category = "Chit Payout",
                referenceType = "CHIT_PAYOUT",
                referenceId = "CHIT_PAYOUT_${auction.id}"
            )
        )

        db.chitDao().updateAuction(auction.copy(isPayoutRecorded = true))
        return ledgerId
    }

    // Festival Gift Distribution
    suspend fun createGiftDistribution(dist: GiftDistributionEntity): Long {
        val distId = db.giftDao().insertDistribution(dist)
        // Auto-assign to all members
        val allMembers = db.memberDao().getAllMembers().firstOrNull() ?: emptyList()
        val recipients = allMembers.map {
            GiftRecipientEntity(
                distributionId = distId,
                memberId = it.memberId,
                status = "PENDING",
                dateGiven = ""
            )
        }
        db.giftDao().insertRecipients(recipients)
        return distId
    }

    suspend fun markGiftGiven(recipient: GiftRecipientEntity, dateGiven: String) {
        db.giftDao().updateRecipient(
            recipient.copy(
                status = "GIVEN",
                dateGiven = dateGiven
            )
        )
        // Update distribution summary counts
        val distList = db.giftDao().getAllDistributions().firstOrNull()
        val dist = distList?.find { it.id == recipient.distributionId }
        if (dist != null) {
            val recs = db.giftDao().getRecipientsForDistribution(dist.id).firstOrNull() ?: emptyList()
            val givenCount = recs.count { it.status == "GIVEN" }
            val pendingCount = recs.count { it.status == "PENDING" }
            db.giftDao().updateDistribution(
                dist.copy(totalGiven = givenCount, totalPending = pendingCount)
            )
        }
    }

    suspend fun markAllGiftsGiven(distId: Long, dateGiven: String) {
        val recs = db.giftDao().getRecipientsForDistribution(distId).firstOrNull() ?: emptyList()
        recs.forEach { r ->
            if (r.status != "GIVEN") {
                db.giftDao().updateRecipient(r.copy(status = "GIVEN", dateGiven = dateGiven))
            }
        }
        val dist = db.giftDao().getAllDistributions().firstOrNull()?.find { it.id == distId }
        if (dist != null) {
            db.giftDao().updateDistribution(
                dist.copy(totalGiven = recs.size, totalPending = 0)
            )
        }
    }

    // Helper: current running balance from consolidated ledger
    private suspend fun getCurrentLedgerBalance(): Double {
        val entries = db.ledgerDao().getAllLedgerEntries().firstOrNull()
        return entries?.firstOrNull()?.balance ?: 548000.0
    }

    // Formatting currency in Indian Rupee format
    companion object {
        fun formatRupee(amount: Double): String {
            val rounded = amount.toLong()
            return "₹" + formatIndianNumber(rounded)
        }

        fun formatRupee(amount: Long): String {
            return "₹" + formatIndianNumber(amount)
        }

        private fun formatIndianNumber(number: Long): String {
            val numStr = kotlin.math.abs(number).toString()
            if (numStr.length <= 3) {
                return if (number < 0) "-$numStr" else numStr
            }
            val lastThree = numStr.substring(numStr.length - 3)
            val rest = numStr.substring(0, numStr.length - 3)
            val parts = mutableListOf<String>()
            var i = rest.length
            while (i > 0) {
                val start = (i - 2).coerceAtLeast(0)
                parts.add(0, rest.substring(start, i))
                i -= 2
            }
            val formatted = parts.joinToString(",") + "," + lastThree
            return if (number < 0) "-$formatted" else formatted
        }

        fun calculateAge(dobStr: String): Int {
            return try {
                val parts = dobStr.split("-")
                if (parts.size == 3) {
                    val year = parts[2].toInt()
                    val currentYear = 2026 // Based on 2026 temporal timeline
                    (currentYear - year).coerceAtLeast(0)
                } else 35
            } catch (e: Exception) {
                35
            }
        }
    }
}
