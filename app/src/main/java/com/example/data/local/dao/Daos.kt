package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM members ORDER BY memberId ASC")
    fun getAllMembers(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE memberId = :memberId LIMIT 1")
    fun getMemberById(memberId: String): Flow<MemberEntity?>

    @Query("SELECT * FROM members WHERE memberId = :memberId LIMIT 1")
    suspend fun getMemberByIdDirect(memberId: String): MemberEntity?

    @Query("SELECT COUNT(*) FROM members")
    suspend fun getMemberCount(): Int

    @Query("SELECT COUNT(*) FROM members WHERE isActive = 1")
    suspend fun getActiveMemberCount(): Int

    @Query("SELECT memberId FROM members")
    suspend fun getAllMemberIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: MemberEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(members: List<MemberEntity>)

    @Update
    suspend fun updateMember(member: MemberEntity)

    @Delete
    suspend fun deleteMember(member: MemberEntity)
}

@Dao
interface FamilyMemberDao {
    @Query("SELECT * FROM family_members WHERE memberId = :memberId")
    fun getFamilyForMember(memberId: String): Flow<List<FamilyMemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamilyMember(item: FamilyMemberEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamilyMembers(items: List<FamilyMemberEntity>)

    @Update
    suspend fun updateFamilyMember(item: FamilyMemberEntity)

    @Delete
    suspend fun deleteFamilyMember(item: FamilyMemberEntity)
}

@Dao
interface MeetingDao {
    @Query("SELECT * FROM meetings ORDER BY id DESC")
    fun getAllMeetings(): Flow<List<MeetingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeeting(meeting: MeetingEntity): Long

    @Query("SELECT * FROM attendance WHERE meetingId = :meetingId")
    fun getAttendanceForMeeting(meetingId: Long): Flow<List<AttendanceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(items: List<AttendanceEntity>)

    @Query("SELECT * FROM attendance WHERE memberId = :memberId")
    fun getAttendanceForMember(memberId: String): Flow<List<AttendanceEntity>>
}

@Dao
interface FundDao {
    @Query("SELECT * FROM funds ORDER BY id ASC")
    fun getAllFunds(): Flow<List<FundEntity>>

    @Query("SELECT * FROM funds WHERE fundCode = :code LIMIT 1")
    fun getFundByCode(code: String): Flow<FundEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFund(fund: FundEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFunds(funds: List<FundEntity>)

    @Update
    suspend fun updateFund(fund: FundEntity)

    @Query("SELECT * FROM collection_records WHERE fundCode = :fundCode ORDER BY id DESC")
    fun getCollectionsForFund(fundCode: String): Flow<List<CollectionRecordEntity>>

    @Query("SELECT * FROM collection_records WHERE fundCode = :fundCode AND monthYear = :monthYear")
    fun getCollectionsForFundAndMonth(fundCode: String, monthYear: String): Flow<List<CollectionRecordEntity>>

    @Query("SELECT * FROM collection_records WHERE memberId = :memberId ORDER BY id DESC")
    fun getCollectionsForMember(memberId: String): Flow<List<CollectionRecordEntity>>

    @Query("SELECT * FROM collection_records")
    fun getAllCollections(): Flow<List<CollectionRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollectionRecords(records: List<CollectionRecordEntity>)

    @Query("DELETE FROM collection_records WHERE id = :id")
    suspend fun deleteCollectionRecord(id: Long)
}

@Dao
interface LedgerDao {
    @Query("SELECT * FROM consolidated_ledger ORDER BY id DESC")
    fun getAllLedgerEntries(): Flow<List<ConsolidatedLedgerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLedgerEntry(entry: ConsolidatedLedgerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLedgerEntries(entries: List<ConsolidatedLedgerEntity>)

    @Update
    suspend fun updateLedgerEntry(entry: ConsolidatedLedgerEntity)

    @Query("DELETE FROM consolidated_ledger WHERE id = :id")
    suspend fun deleteLedgerEntry(id: Long)

    @Query("SELECT * FROM consolidated_ledger WHERE id = :id LIMIT 1")
    suspend fun getLedgerEntryById(id: Long): ConsolidatedLedgerEntity?

    @Query("SELECT * FROM consolidated_ledger WHERE referenceType = :refType AND referenceId = :refId LIMIT 1")
    suspend fun getLedgerByReference(refType: String, refId: String): ConsolidatedLedgerEntity?
}

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenses(expenses: List<ExpenseEntity>)

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpense(id: Long)

    @Query("SELECT * FROM expenses WHERE id = :id LIMIT 1")
    suspend fun getExpenseById(id: Long): ExpenseEntity?
}

@Dao
interface BusinessFundDao {
    @Query("SELECT * FROM business_funds ORDER BY id DESC")
    fun getAllBusinessFunds(): Flow<List<BusinessFundEntity>>

    @Query("SELECT * FROM business_funds WHERE id = :id LIMIT 1")
    suspend fun getBusinessFundById(id: Long): BusinessFundEntity?

    @Query("SELECT * FROM business_funds WHERE memberId = :memberId")
    fun getBusinessFundsForMember(memberId: String): Flow<List<BusinessFundEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBusinessFund(fund: BusinessFundEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBusinessFunds(funds: List<BusinessFundEntity>)

    @Update
    suspend fun updateBusinessFund(fund: BusinessFundEntity)

    @Query("SELECT * FROM business_fund_returns WHERE businessFundId = :fundId ORDER BY id DESC")
    fun getReturnsForFund(fundId: Long): Flow<List<BusinessFundReturnEntity>>

    @Query("SELECT * FROM business_fund_returns ORDER BY id DESC")
    fun getAllReturns(): Flow<List<BusinessFundReturnEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReturn(record: BusinessFundReturnEntity): Long

    @Query("DELETE FROM business_fund_returns WHERE id = :id")
    suspend fun deleteReturn(id: Long)
}

@Dao
interface LoanDao {
    @Query("SELECT * FROM member_loans ORDER BY id DESC")
    fun getAllLoans(): Flow<List<MemberLoanEntity>>

    @Query("SELECT * FROM member_loans WHERE id = :id LIMIT 1")
    suspend fun getLoanById(id: Long): MemberLoanEntity?

    @Query("SELECT * FROM member_loans WHERE memberId = :memberId")
    fun getLoansForMember(memberId: String): Flow<List<MemberLoanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoan(loan: MemberLoanEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoans(loans: List<MemberLoanEntity>)

    @Update
    suspend fun updateLoan(loan: MemberLoanEntity)

    @Query("SELECT * FROM loan_repayments WHERE loanId = :loanId ORDER BY id DESC")
    fun getRepaymentsForLoan(loanId: Long): Flow<List<LoanRepaymentEntity>>

    @Query("SELECT * FROM loan_repayments ORDER BY id DESC")
    fun getAllRepayments(): Flow<List<LoanRepaymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepayment(repayment: LoanRepaymentEntity): Long

    @Update
    suspend fun updateRepayment(repayment: LoanRepaymentEntity)

    @Query("DELETE FROM loan_repayments WHERE id = :id")
    suspend fun deleteRepayment(id: Long)
}

@Dao
interface ChitDao {
    @Query("SELECT * FROM chit_funds ORDER BY id DESC")
    fun getAllChitFunds(): Flow<List<ChitFundEntity>>

    @Query("SELECT * FROM chit_funds WHERE id = :id LIMIT 1")
    fun getChitFundById(id: Long): Flow<ChitFundEntity?>

    @Query("SELECT * FROM chit_funds WHERE id = :id LIMIT 1")
    suspend fun getChitFundByIdDirect(id: Long): ChitFundEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChitFund(chit: ChitFundEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChitFunds(chits: List<ChitFundEntity>)

    @Update
    suspend fun updateChitFund(chit: ChitFundEntity)

    @Query("SELECT * FROM chit_auctions WHERE chitId = :chitId ORDER BY cycleMonth ASC")
    fun getAuctionsForChit(chitId: Long): Flow<List<ChitAuctionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuction(auction: ChitAuctionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuctions(auctions: List<ChitAuctionEntity>)

    @Update
    suspend fun updateAuction(auction: ChitAuctionEntity)

    @Query("SELECT * FROM chit_member_payments WHERE chitId = :chitId AND cycleMonth = :cycleMonth")
    fun getPaymentsForCycle(chitId: Long, cycleMonth: Int): Flow<List<ChitMemberPaymentEntity>>

    @Query("SELECT * FROM chit_member_payments WHERE memberId = :memberId")
    fun getPaymentsForMember(memberId: String): Flow<List<ChitMemberPaymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayments(payments: List<ChitMemberPaymentEntity>)

    @Update
    suspend fun updatePayment(payment: ChitMemberPaymentEntity)
}

@Dao
interface GiftDao {
    @Query("SELECT * FROM gift_distributions ORDER BY id DESC")
    fun getAllDistributions(): Flow<List<GiftDistributionEntity>>

    @Query("SELECT * FROM gift_distributions WHERE id = :id LIMIT 1")
    fun getDistributionById(id: Long): Flow<GiftDistributionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistribution(dist: GiftDistributionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistributions(dists: List<GiftDistributionEntity>)

    @Update
    suspend fun updateDistribution(dist: GiftDistributionEntity)

    @Query("SELECT * FROM gift_recipients WHERE distributionId = :distId")
    fun getRecipientsForDistribution(distId: Long): Flow<List<GiftRecipientEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipients(recipients: List<GiftRecipientEntity>)

    @Update
    suspend fun updateRecipient(recipient: GiftRecipientEntity)
}

@Dao
interface GalleryDao {
    @Query("SELECT * FROM gallery_items ORDER BY id DESC")
    fun getAllGalleryItems(): Flow<List<GalleryItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGalleryItem(item: GalleryItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGalleryItems(items: List<GalleryItemEntity>)

    @Query("DELETE FROM gallery_items WHERE id = :id")
    suspend fun deleteGalleryItem(id: Long)
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM community_settings WHERE id = 1 LIMIT 1")
    fun getSettings(): Flow<CommunitySettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSettings(settings: CommunitySettingsEntity)
}

@Dao
interface OfficeBearerDao {
    @Query("SELECT * FROM office_bearers ORDER BY id ASC")
    fun getAllOfficeBearers(): Flow<List<OfficeBearerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfficeBearers(bearers: List<OfficeBearerEntity>)

    @Update
    suspend fun updateOfficeBearer(bearer: OfficeBearerEntity)
}
