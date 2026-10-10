package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.*
import com.example.data.repository.CommunityRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CommunityViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application, viewModelScope)
    val repository = CommunityRepository(db)

    // Reactive streams
    val members: StateFlow<List<MemberEntity>> = repository.members
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val funds: StateFlow<List<FundEntity>> = repository.funds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val ledgerEntries: StateFlow<List<ConsolidatedLedgerEntity>> = repository.ledgerEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenses: StateFlow<List<ExpenseEntity>> = repository.expenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val businessFunds: StateFlow<List<BusinessFundEntity>> = repository.businessFunds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val memberLoans: StateFlow<List<MemberLoanEntity>> = repository.memberLoans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chitFunds: StateFlow<List<ChitFundEntity>> = repository.chitFunds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val giftDistributions: StateFlow<List<GiftDistributionEntity>> = repository.giftDistributions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val officeBearers: StateFlow<List<OfficeBearerEntity>> = repository.officeBearers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val meetings: StateFlow<List<MeetingEntity>> = repository.meetings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val galleryItems: StateFlow<List<GalleryItemEntity>> = repository.galleryItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<CommunitySettingsEntity?> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Navigation and screen state
    private val _currentScreen = MutableStateFlow("HOME") // HOME, MEMBERS, ADD, FUNDS, MORE, MEMBER_DETAILS, MONTHLY_COLLECTION, LEDGER, EXPENSES, CHIT_DETAILS, GIFT_DISTRIBUTION, COMMUNITY_FUND_DETAILS, SAVINGS_FUNDS, BUSINESS_FUNDS, MEMBER_LOANS, ATTENDANCE, GALLERY, SETTINGS, OFFICE_BEARERS
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    private val _screenStack = MutableStateFlow(listOf("HOME"))

    // Selected member for detail view
    private val _selectedMemberId = MutableStateFlow<String?>("M001")
    val selectedMemberId: StateFlow<String?> = _selectedMemberId.asStateFlow()

    val selectedMember: StateFlow<MemberEntity?> = _selectedMemberId
        .flatMapLatest { id ->
            if (id != null) repository.getMemberById(id) else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val selectedMemberFamily: StateFlow<List<FamilyMemberEntity>> = _selectedMemberId
        .flatMapLatest { id ->
            if (id != null) repository.getFamilyForMember(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedMemberCollections: StateFlow<List<CollectionRecordEntity>> = _selectedMemberId
        .flatMapLatest { id ->
            if (id != null) repository.getCollectionsForMember(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected chit for detail view
    private val _selectedChitId = MutableStateFlow<Long?>(1L)
    val selectedChitId: StateFlow<Long?> = _selectedChitId.asStateFlow()

    val selectedChitAuctions: StateFlow<List<ChitAuctionEntity>> = _selectedChitId
        .flatMapLatest { id ->
            if (id != null) repository.getAuctionsForChit(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected gift distribution
    private val _selectedGiftDistId = MutableStateFlow<Long?>(1L)
    val selectedGiftDistId: StateFlow<Long?> = _selectedGiftDistId.asStateFlow()

    val selectedGiftRecipients: StateFlow<List<GiftRecipientEntity>> = _selectedGiftDistId
        .flatMapLatest { id ->
            if (id != null) repository.getRecipientsForDistribution(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search and filters
    val memberSearchQuery = MutableStateFlow("")
    val memberFilterState = MutableStateFlow("ALL") // ALL, ACTIVE, INACTIVE

    val expenseSearchQuery = MutableStateFlow("")

    val giftSearchQuery = MutableStateFlow("")
    val giftFilterState = MutableStateFlow("ALL") // ALL, GIVEN, PENDING

    // Notification toast / message
    val userNotification = MutableStateFlow<String?>(null)

    fun navigateTo(screen: String) {
        val current = _screenStack.value
        _screenStack.value = current + screen
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        val stack = _screenStack.value
        if (stack.size > 1) {
            val newStack = stack.dropLast(1)
            _screenStack.value = newStack
            _currentScreen.value = newStack.last()
            return true
        }
        return false
    }

    fun openMemberDetails(memberId: String) {
        _selectedMemberId.value = memberId
        navigateTo("MEMBER_DETAILS")
    }

    fun openMemberTransactions(memberId: String) {
        _selectedMemberId.value = memberId
        navigateTo("MEMBER_TRANSACTIONS")
    }

    fun openChitDetails(chitId: Long) {
        _selectedChitId.value = chitId
        navigateTo("CHIT_DETAILS")
    }

    fun openGiftDistribution(distId: Long) {
        _selectedGiftDistId.value = distId
        navigateTo("GIFT_DISTRIBUTION")
    }

    // Actions
    fun addMember(
        name: String,
        mobile: String,
        email: String = "",
        dob: String = "15-05-1990",
        weddingDate: String = "",
        bloodGroup: String = "B+ Positive",
        education: String = "Graduate",
        occupation: String = "Business",
        address: String = "Thorapadi, Vellore",
        gender: String = "Male",
        isOfficeBearer: Boolean = false,
        designation: String = "",
        termPeriod: String = "2024-2027",
        notes: String = ""
    ) {
        viewModelScope.launch {
            val totalCount = repository.getMemberCount()
            val nextNum = totalCount + 1
            val nextId = "M" + nextNum.toString().padStart(3, '0')
            val newMember = MemberEntity(
                memberId = nextId,
                name = name.trim(),
                mobile = mobile.trim(),
                email = email.trim(),
                dob = dob.trim(),
                weddingDate = weddingDate.trim(),
                bloodGroup = bloodGroup,
                education = education.trim(),
                occupation = occupation.trim(),
                address = address.trim(),
                notes = notes.trim(),
                isActive = true,
                isOfficeBearer = isOfficeBearer,
                designation = designation.trim(),
                termPeriod = termPeriod.trim(),
                avatarInitials = name.trim().take(2).uppercase()
            )
            repository.insertMember(newMember)
            _selectedMemberId.value = nextId
            userNotification.value = "Member ${newMember.name} ($nextId) registered successfully"
        }
    }

    fun updateMember(member: MemberEntity) {
        viewModelScope.launch {
            repository.updateMember(member)
            userNotification.value = "Member details updated"
        }
    }

    fun addFamilyMember(memberId: String, name: String, relationship: String, dob: String, education: String) {
        viewModelScope.launch {
            repository.insertFamilyMember(
                FamilyMemberEntity(
                    memberId = memberId,
                    name = name,
                    relationship = relationship,
                    dob = dob,
                    education = education
                )
            )
            userNotification.value = "Family member added"
        }
    }

    fun deleteFamilyMember(item: FamilyMemberEntity) {
        viewModelScope.launch {
            repository.deleteFamilyMember(item)
            userNotification.value = "Family member removed"
        }
    }

    fun recordExpense(
        date: String,
        category: String,
        amount: Double,
        description: String,
        paymentMethod: String,
        notes: String
    ) {
        viewModelScope.launch {
            repository.addExpense(date, category, amount, description, paymentMethod, notes)
            userNotification.value = "Expense of ₹${amount.toInt()} recorded in Ledger"
        }
    }

    fun deleteExpense(id: Long) {
        viewModelScope.launch {
            repository.deleteExpense(id)
            userNotification.value = "Expense deleted and ledger updated"
        }
    }

    fun saveMonthlyCollection(
        fundCode: String,
        fundTitle: String,
        monthYear: String,
        records: List<CollectionRecordEntity>
    ) {
        viewModelScope.launch {
            repository.saveMonthlyCollection(
                fundCode = fundCode,
                fundTitle = fundTitle,
                monthYear = monthYear,
                date = "10-10-2026",
                records = records
            )
            userNotification.value = "Monthly Collection saved and consolidated in Ledger"
        }
    }

    fun giveBusinessFund(memberId: String, memberName: String, amount: Double, purpose: String) {
        viewModelScope.launch {
            repository.giveBusinessFund(
                memberId = memberId,
                memberName = memberName,
                amount = amount,
                date = "10-10-2026",
                purpose = purpose,
                notes = "Interest-free community assistance"
            )
            userNotification.value = "Business assistance of ₹${amount.toInt()} provided"
        }
    }

    fun returnBusinessFund(businessFundId: Long, returnAmount: Double, notes: String) {
        viewModelScope.launch {
            repository.recordBusinessFundReturn(
                businessFundId = businessFundId,
                returnAmount = returnAmount,
                returnDate = "10-10-2026",
                notes = notes
            )
            userNotification.value = "Return of ₹${returnAmount.toInt()} recorded in Ledger"
        }
    }

    fun issueMemberLoan(memberId: String, memberName: String, amount: Double, purpose: String, interestRate: Double) {
        viewModelScope.launch {
            repository.issueMemberLoan(
                memberId = memberId,
                memberName = memberName,
                principalAmount = amount,
                date = "10-10-2026",
                purpose = purpose,
                monthlyInterestRate = interestRate,
                notes = "Approved by committee"
            )
            userNotification.value = "Loan of ₹${amount.toInt()} issued to $memberName"
        }
    }

    fun recordLoanRepayment(loanId: Long, totalPayment: Double, notes: String) {
        viewModelScope.launch {
            repository.recordLoanRepayment(
                loanId = loanId,
                totalPayment = totalPayment,
                repaymentDate = "10-10-2026",
                monthYear = "10-2026",
                notes = notes
            )
            userNotification.value = "Loan repayment of ₹${totalPayment.toInt()} recorded"
        }
    }

    fun createChitFund(name: String, value: Double, members: Int, duration: Int, commissionPercent: Double) {
        viewModelScope.launch {
            val due = value / members
            repository.createChitFund(
                ChitFundEntity(
                    chitName = name,
                    chitValue = value,
                    memberCount = members,
                    durationMonths = duration,
                    monthlyDue = due,
                    commissionPercent = commissionPercent,
                    startDate = "10-11-2026",
                    auctionDay = "10th of every month",
                    status = "Bidding Open",
                    currentCycleMonth = 1
                )
            )
            userNotification.value = "Chit fund scheme created"
        }
    }

    fun recordChitAuction(
        chitId: Long,
        cycleMonth: Int,
        winningBid: Double,
        winningMemberId: String,
        winningMemberName: String
    ) {
        viewModelScope.launch {
            repository.recordChitAuctionResult(
                chitId = chitId,
                cycleMonth = cycleMonth,
                auctionDate = "10-10-2026",
                winningBidAmount = winningBid,
                winningMemberId = winningMemberId,
                winningMemberName = winningMemberName
            )
            userNotification.value = "Auction confirmed for $winningMemberName"
        }
    }

    fun recordChitWinnerPayout(auction: ChitAuctionEntity, chitName: String) {
        viewModelScope.launch {
            repository.recordChitWinnerPayout(auction, chitName)
            userNotification.value = "Winner payout of ₹${auction.winnerPayout.toInt()} debited in Ledger"
        }
    }

    fun markGiftGiven(recipient: GiftRecipientEntity) {
        viewModelScope.launch {
            repository.markGiftGiven(recipient, "10-10-2026")
        }
    }

    fun markAllGiftsGiven(distId: Long) {
        viewModelScope.launch {
            repository.markAllGiftsGiven(distId, "10-10-2026")
            userNotification.value = "All eligible members marked as given"
        }
    }
}
